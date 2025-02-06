package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.WebRTCDto;
import com.kma.engfinity.DTO.request.EditRoomRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.entity.Hire;
import com.kma.engfinity.entity.Room;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.HireRepository;
import com.kma.engfinity.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private HireRepository hireRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AccountService accountService;

    public Room create(EditRoomRequest request) {
        Account currentAccount = authService.getCurrentAccount();
        Optional<Hire> optionalHire = hireRepository.findById(request.getHireId());
        if (optionalHire.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Room room = new Room();
        room.setCreatedBy(currentAccount);
        room.setHire(optionalHire.get());
        room.setActualCallDuration(request.getActualCallDuration());
        return roomRepository.save(room);
    }

    public ResponseEntity<?> sendWebRTCData(WebRTCDto request) {
        String destination = "/topic/rooms/" + request.getRoomId() + "/video-call";
        messagingTemplate.convertAndSend(destination, request);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Start call successfully!");
        return ResponseEntity.ok(response);
    }
}