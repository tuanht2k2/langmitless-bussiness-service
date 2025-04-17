package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditHireRequest;
import com.kma.engfinity.DTO.request.EditMultiAccountBalanceRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.HireResponse;
import com.kma.engfinity.DTO.response.PublicAccountResponse;
import com.kma.engfinity.entity.Hire;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
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

            Hire hire = modelMapper.map(request, Hire.class);
            hire.setCreatedBy(account);
            hire.setTeacher(teacher);
            hire.setCost(cost);
            hire.setStatus(EHireStatus.PENDING);
            hire.setTotalTime(request.getTotalTime() * 3600);
            Hire createdHire = hireRepository.save(hire);
            HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
            String destination = "/topic/teachers/" + request.getTeacherId();
            webSocketService.sendData(destination, hireResponse);
            setCallStatus(account.getId(), request.getTeacherId(), callingPrefix);
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

    @Transactional
    public ResponseEntity<?> updateStatus (EditHireRequest request) {
        try {
            Optional<Hire> optionalHire = hireRepository.findById(request.getId());
            if (optionalHire.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
            clearCallCache();
            Hire hire = optionalHire.get();
            if (hire.getStatus().equals(EHireStatus.ENDED)) throw new CustomException(EError.CALL_ENDED);
            hire.setStatus(request.getStatus());
            if (request.getStatus().equals(EHireStatus.ACCEPTED)) {
                setCallStatus(hire.getCreatedBy().getId(), hire.getTeacher().getId(), inCallPrefix);
            }

            if (request.getStatus().equals(EHireStatus.ENDED)) {
                Float actualTime = (float) ((new Date().getTime() - hire.getCreatedAt().getTime())/1000);
                hire.setActualTime(actualTime);
                if (actualTime >= hire.getTotalTime()/3) {
                    EditMultiAccountBalanceRequest balanceRequest = new EditMultiAccountBalanceRequest();
                    balanceRequest.setBalance(hire.getCost());
                    balanceRequest.setSenderIds(Arrays.asList(hire.getCreatedBy().getId()));
                    balanceRequest.setReceiverIds(Arrays.asList(hire.getTeacher().getId()));
                    accountService.updateMultiAccountBalance(balanceRequest);
                }
            }

            HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
            hireResponse.setTeacher(modelMapper.map(hireResponse.getTeacher(), PublicAccountResponse.class));
            hireResponse.setCreatedBy(modelMapper.map(hireResponse.getCreatedBy(), PublicAccountResponse.class));
            String destination = "/topic/teachers/" + hire.getTeacher().getId();
            webSocketService.sendData(destination, hire);
            hireRepository.save(hire);
            CommonResponse<?> response = new CommonResponse<>(200, hireResponse, "Update status successfully!");
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.ok(new CommonResponse<>(400, null, e.getMessage()));
        } catch (Exception e) {
            log.error("An error happened when update hire status: {}", e.getMessage());
            return ResponseEntity.ok(new CommonResponse<>(500, null, e.getMessage()));
        }
    }

    public void checkMissedCalls(String expired) {
        try {
            String[] split = expired.split("calling:");
            String receiver = split[split.length - 1];
            log.info("Receiver: {}", receiver);
            if (ObjectUtils.isEmpty(receiver)) {
                throw new CustomException(EError.BAD_REQUEST);
            }
            List<Hire> hires = hireRepository.findByTeacher(receiver);
            Hire hire = hires.stream().findFirst().orElse(null);
            if (!ObjectUtils.isEmpty(hire) && hire.getStatus().equals(EHireStatus.PENDING)) {
                hire.setStatus(EHireStatus.REJECTED);
                hireRepository.saveAndFlush(hire);
                webSocketService.sendData("/topic/teachers/" + receiver, hire);
            }
        } catch (Exception e) {
            log.error("An error happened when checkMissedCalls: {}", e.getMessage());
        }
    }

    public Response<Object> getDetail (String id) {
        try {
            HireResponse hire = hireRepository.getDetail(id);
            if (ObjectUtils.isEmpty(hire)) {
                throw new CustomException(EError.BAD_REQUEST);
            }
            return Response.getResponse(200, hire, "Get data successfully!");
        } catch (Exception e) {
            return Response.getResponse(500, e.getMessage());
        }
    }

    private void clearCallCache () {
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
                log.info("Set in call key: Caller: {}, Receiver: {}", callerId, receiverId);
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
