package com.kma.engfinity.service;

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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ResponseEntity<?> create (EditHireRequest request) {
        Account account = authService.getCurrentAccount();
        Hire hire = modelMapper.map(request, Hire.class);
        hire.setCreatedBy(account);
        Account teacher = accountService.getAccountById(request.getTeacherId());
        if (teacher.getStatus().equals(EAccountStatus.IN_CALL)) throw new CustomException(EError.USER_IN_CALL);
        hire.setTeacher(teacher);
        hire.setCost(request.getTotalTime() * teacher.getCost());
        Hire createdHire = hireRepository.save(hire);
        HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
        String destination = "/topic/teachers/" + request.getTeacherId();
        webSocketService.sendData(destination, hireResponse);
        CommonResponse<?> response = new CommonResponse<>(200, createdHire, "Create hire successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> updateStatus (EditHireRequest request) {
        Optional<Hire> optionalHire = hireRepository.findById(request.getId());
        if (optionalHire.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Hire hire = optionalHire.get();
        hire.setStatus(request.getStatus());
        if (request.getStatus().equals(EHireStatus.ACCEPTED)) {
            EditRoomRequest editRoomRequest = new EditRoomRequest();
            editRoomRequest.setHireId(hire.getId());
            editRoomRequest.setExpectedCallDuration(request.getTotalTime());
            Room createdRoom = roomService.create(editRoomRequest);
            hire.setRoom(createdRoom);
        }
        HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
        hireResponse.setTeacher(modelMapper.map(hireResponse.getTeacher(), PublicAccountResponse.class));
        hireResponse.setCreatedBy(modelMapper.map(hireResponse.getCreatedBy(), PublicAccountResponse.class));
        String destination = "/topic/teachers/" + request.getTeacherId();
        webSocketService.sendData(destination, hire);

        CommonResponse<?> response = new CommonResponse<>(200, hireResponse, "Update status successfully!");
        return ResponseEntity.ok(response);
    }
}
