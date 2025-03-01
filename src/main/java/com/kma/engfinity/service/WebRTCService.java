package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.VideoCallDto;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.entity.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WebRTCService {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessengerService messengerService;

    @Autowired
    CommonService commonService;

    @Autowired
    AuthService authService;

    public ResponseEntity<?> startVideoCall(VideoCallDto request) {
        return handleVideoCall(request, "Start video call successfully!");
    }

    public ResponseEntity<?> acceptVideoCall(VideoCallDto request) {
        return handleVideoCall(request, "Accept video call successfully!");
    }

    private ResponseEntity<?> handleVideoCall(VideoCallDto request, String successMessage) {
//        Messenger messenger = messengerService.s_get(request.getMessengerId());
//        Account currentAccount = authService.getCurrentAccount();
//        request.setSenderId(currentAccount.getId());
//
//        Set<Account> members = messenger.getMembers();
//        members.stream()
//                .filter(member -> !member.getId().equals(currentAccount.getId()))
//                .forEach(member -> {
//                    String destination = commonService.getMessengerWebRTCUrl(member.getId());
//                    messagingTemplate.convertAndSend(destination, request);
//                });

        CommonResponse<?> response = new CommonResponse<>(200, null, successMessage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
