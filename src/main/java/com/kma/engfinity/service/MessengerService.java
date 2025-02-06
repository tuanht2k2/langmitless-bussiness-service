package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditMessengerRequest;
import com.kma.engfinity.DTO.request.SearchMessengerByMembersRequest;
import com.kma.engfinity.DTO.request.SearchMessengerOfAccountRequest;
import com.kma.engfinity.DTO.request.SearchPersonalMessengerByMember;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.MessengerResponse;
import com.kma.engfinity.entity.Messenger;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EMessengerType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.MessengerRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class MessengerService {
    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    AccountMessengerService accountMessengerService;

    public Messenger create(EditMessengerRequest request) {
        if (request.getMembers().isEmpty()) throw new CustomException(EError.BAD_REQUEST);

        Messenger messenger = new Messenger();
        messenger.setName(request.getName());
        messenger.setCreatedAt(new Date());
        messenger.setType(request.getType());
        Messenger createdMessenger = messengerRepository.save(messenger);
        StringBuilder sql = new StringBuilder("(");
        request.getMembers().forEach(memberId -> {
            sql.append("('");
            sql.append(memberId);
            sql.append("', '");
            sql.append(createdMessenger.getId());
            sql.append("')");
        });
        sql.append(")");
        accountMessengerService.create(sql.toString());
        return createdMessenger;
    }

//    public ResponseEntity<?> getMessengerByMembers(SearchMessengerByMembersRequest request) {
//
//    }

//    public Messenger getMessenger(String id) {
//
//    }
}
