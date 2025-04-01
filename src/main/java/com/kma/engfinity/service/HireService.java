package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.common.enums.EAccountStatus;
import com.kma.engfinity.DTO.request.EditHireRequest;
import com.kma.engfinity.DTO.request.EditRoomRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.HireResponse;
import com.kma.engfinity.DTO.response.PublicAccountResponse;
import com.kma.engfinity.entity.Hire;
import com.kma.engfinity.entity.Room;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EHireStatus;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.HireRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HireService {
    @Autowired
    private HireRepository hireRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${REDIS.IN_CALL_PREFIX}")
    private String inCallPrefix;

    @Value("${REDIS.CALLING_PREFIX}")
    private String callingPrefix;

    public ResponseEntity<?> create (EditHireRequest request) {
        try {
            Account account = authService.getCurrentAccount();
            Account teacher = accountService.getAccountById(request.getTeacherId());
            Long cost = request.getTotalTime() * (ObjectUtils.isEmpty(teacher.getCost()) ? 0 : teacher.getCost());
            if (account.getId().equals(teacher.getId())) {
                throw new CustomException(EError.BAD_REQUEST);
            }

            if (account.getBalance() < cost) {
                throw new CustomException(EError.NOT_ENOUGH_MONEY);
            }

            if (ObjectUtils.isEmpty(teacher)) {
                throw new CustomException(EError.USER_NOT_EXISTED);
            }

            checkIsUserBusy(account.getId(), request.getTeacherId());
            setCallStatus(account.getId(), request.getTeacherId(), callingPrefix);

            Hire hire = modelMapper.map(request, Hire.class);
            hire.setCreatedBy(account);
            hire.setTeacher(teacher);
            hire.setCost(cost);
            Hire createdHire = hireRepository.save(hire);
            HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
            String destination = "/topic/teachers/" + request.getTeacherId();
            webSocketService.sendData(destination, hireResponse);
            CommonResponse<?> response = new CommonResponse<>(200, createdHire, "Create hire successfully!");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            CommonResponse<?> response = new CommonResponse<>(400, null, e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CommonResponse<?> response = new CommonResponse<>(500, null, e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<?> updateStatus (EditHireRequest request) {
        try {
            Account account = authService.getCurrentAccount();
            Optional<Hire> optionalHire = hireRepository.findById(request.getId());
            if (optionalHire.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
            clearCallCache();
            Hire hire = optionalHire.get();
            hire.setStatus(request.getStatus());
            if (request.getStatus().equals(EHireStatus.ACCEPTED)) {
                EditRoomRequest editRoomRequest = new EditRoomRequest();
                editRoomRequest.setHireId(hire.getId());
                editRoomRequest.setExpectedCallDuration(request.getTotalTime());
                Room createdRoom = roomService.create(editRoomRequest);
                hire.setRoom(createdRoom);
                setCallStatus(account.getId(), request.getTeacherId(), inCallPrefix);
            }
            HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
            hireResponse.setTeacher(modelMapper.map(hireResponse.getTeacher(), PublicAccountResponse.class));
            hireResponse.setCreatedBy(modelMapper.map(hireResponse.getCreatedBy(), PublicAccountResponse.class));
            String destination = "/topic/teachers/" + request.getTeacherId();
            webSocketService.sendData(destination, hire);

            CommonResponse<?> response = new CommonResponse<>(200, hireResponse, "Update status successfully!");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.ok(new CommonResponse<>(400, null, e.getMessage()));
        } catch (Exception e) {
            log.error("An error happened when update hire status: {}", e.getMessage());
            return ResponseEntity.ok(new CommonResponse<>(500, null, e.getMessage()));
        }
    }

    @Scheduled(fixedRate = 5000) // Kiểm tra mỗi 5s
    public void checkMissedCalls() {
        try {
            Set<String> keys = redisTemplate.keys(callingPrefix + "*");

            if (!ObjectUtils.isEmpty(keys)) {
                for (String key : keys) {
                    if (redisTemplate.getExpire(key) == -2) { // Key đã hết hạn
                        Object receiver = redisTemplate.opsForValue().get(key);
                        if (ObjectUtils.isEmpty(receiver)) {
                            throw new CustomException(EError.BAD_REQUEST);
                        }
                        String caller = key.replace(callingPrefix, "");
                        List<Hire> hires = hireRepository.findByCreatedByAndTeacher(caller, receiver.toString());
                        Hire hire = hires.stream().findFirst().orElse(null);
                        if (ObjectUtils.isEmpty(hire)) {
                            log.error("An error occurred when checkMissedCalls, key: {}", key);
                            throw new CustomException(EError.BAD_REQUEST);
                        }
                        webSocketService.sendData("/topic/users/" + receiver, hire);
                    }
                }
            }
        } catch (Exception e) {
            log.error("An error happened when checkMissedCalls: {}", e.getMessage());
        }
    }

    public void clearCallCache () {
        try {
            Account caller = authService.getCurrentAccount();
            Object receiver = redisTemplate.opsForValue().get(inCallPrefix + caller.getId());
            if (!ObjectUtils.isEmpty(receiver)) {
                redisTemplate.delete(inCallPrefix + caller);
                redisTemplate.delete(inCallPrefix + receiver);
                redisTemplate.delete(callingPrefix + caller);
                redisTemplate.delete(callingPrefix + receiver);
            }
        } catch (Exception e) {
            log.error("An error happened when endCall: {}", e.getMessage());
        }
    }

    private void checkIsUserBusy (String callerId, String receiverId) {
        try {
            Object callerStatus = redisTemplate.opsForValue().get(inCallPrefix + callerId);
            Object receiverStatus = redisTemplate.opsForValue().get(inCallPrefix + receiverId);
            Object callingStatus = redisTemplate.opsForValue().get(callingPrefix + callerId);
            Object callIncomingStatus = redisTemplate.opsForValue().get(callingPrefix + receiverId);

            if (!ObjectUtils.isEmpty(callerStatus) || !ObjectUtils.isEmpty(receiverStatus)
                    || !ObjectUtils.isEmpty(callingStatus) || !ObjectUtils.isEmpty(callIncomingStatus))
                throw new CustomException(EError.USER_IN_CALL);
        } catch (Exception e) {
            log.error("An error occurred when checkIsUserBusy: {}", e.getMessage());
        }
    }

    private void setCallStatus (String callerId, String receiverId, String prefix) {
        try {
            if (prefix.equals(inCallPrefix)) {
                redisTemplate.opsForValue().set(prefix + callerId, receiverId);
                redisTemplate.opsForValue().set(prefix + receiverId, callerId);
            } else {
                redisTemplate.opsForValue().set(prefix + callerId, receiverId, 15, TimeUnit.SECONDS);
                redisTemplate.opsForValue().set(prefix + receiverId, callerId, 15, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("An error occurred when setCallStatus: {}", e.getMessage());
        }
    }
}
