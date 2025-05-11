package com.kma.engfinity.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditMessengerRequest;
import com.kma.engfinity.DTO.response.MessageResponse;
import com.kma.engfinity.DTO.response.MessengerMemberResponse;
import com.kma.engfinity.DTO.response.MessengerProperties;
import com.kma.engfinity.DTO.response.MessengerResponse;
import com.kma.engfinity.constants.Constant.*;
import com.kma.engfinity.entity.AccountMessenger;
import com.kma.engfinity.entity.Messenger;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EMessengerType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountMessengerRepository;
import com.kma.engfinity.repository.MessengerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MessengerService {
    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountMessengerRepository accountMessengerRepository;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MessageService messageService;

    @Transactional
    public Messenger create(EditMessengerRequest request) {
        try {
            Account currentAccount = authService.getCurrentAccount();
            if (request.getType().equals(EMessengerType.GROUP) && request.getMembers().size() < 2) {
                throw new CustomException(EError.BAD_REQUEST);
            }

//            if (request.getType().equals(EMessengerType.PERSONAL)) {
//                List<String> messengerIds = accountMessengerRepository.findPersonalMessenger(request.getMembers().get(0), request.getMembers().get(1));
//                if (!ObjectUtils.isEmpty(messengerIds) && messengerIds.size() > 0) {
//                    return Response.getResponse(400, "MESSENGERS_ALREADY_EXISTS");
//                };
//            }

            Messenger messenger = objectMapper.convertValue(request, Messenger.class);
            messenger.setCreatedBy(currentAccount.getId());
            Messenger savedMessenger = messengerRepository.save(messenger);

            List<AccountMessenger> accountMessengers = request.getMembers().stream()
                    .map(accountId -> {
                        AccountMessenger accountMessenger = new AccountMessenger();
                        accountMessenger.setMessengerId(savedMessenger.getId());
                        accountMessenger.setAccountId(accountId);
                        return accountMessenger;
                    }).toList();
            accountMessengerRepository.saveAllAndFlush(accountMessengers);
            return messenger;
        } catch (Exception e) {
            log.error("An error occurred when create messenger: {}", e.getMessage());
            return null;
        }
    }

    public Response<Object> findPersonalMessenger (String accountId) {
        try {
            Account currentAccount = authService.getCurrentAccount();
            List<String> messengerIds = accountMessengerRepository.findPersonalMessenger(currentAccount.getId(), accountId);
            if (!ObjectUtils.isEmpty(messengerIds) && messengerIds.size() > 0) {
                return Response.getResponse(200, messengerRepository.findById(messengerIds.get(0)), Status.SUCCESS);
            }

            EditMessengerRequest request = new EditMessengerRequest();
            request.setType(EMessengerType.PERSONAL);
            request.setMembers(Arrays.asList(currentAccount.getId(), accountId));
            return Response.getResponse(200, create(request), Status.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when findPersonalMessenger: {}", e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }

    public Response<Object> searchMessengers () {
        try {
            Account currentAccount = authService.getCurrentAccount();

            List<MessengerResponse> messengers = new ArrayList<>();
            List<Object[]> messengerObjects = messengerRepository.searchMessengers(currentAccount.getId());
            for (Object[] object : messengerObjects) {
                String id = (String) object[0];
                String name = (String) object[1];
                String type = (String) object[2];
                Date updatedAt = (Date) object[3];
                String jsonMembers = (String) object[4];

                List<MessengerMemberResponse> members = objectMapper.readValue(jsonMembers, new TypeReference<List<MessengerMemberResponse>>() {});

                MessengerResponse messenger = new MessengerResponse();
                messenger.setId(id);
                messenger.setName(name);
                messenger.setType(type);
                messenger.setMembers(members);
                messenger.setUpdatedAt(updatedAt);

                MessengerProperties properties = getProperties(messenger);
                messenger.setName(properties.getName());
                messenger.setImage(properties.getImage());

                messengers.add(messenger);
            }

            return Response.getResponse(200, messengers, Status.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when searchMessengers: {}", e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }

    public Response<Object> details (String messengerId) {
        try {
            Messenger  messenger = messengerRepository.findById(messengerId).orElse(null);
            if (ObjectUtils.isEmpty(messenger)) {
                return Response.getResponse(ErrorCode.BAD_REQUEST, "Messenger does not existed!");
            }

            MessengerResponse messengerResponse = new MessengerResponse();
            messengerResponse.setId(messenger.getId());
            messengerResponse.setName(messenger.getName());
            messengerResponse.setCreatedAt(messenger.getCreatedAt());
            messengerResponse.setType(messenger.getType().toString());

            List<MessengerMemberResponse> members;
            if (ObjectUtils.isEmpty(redisTemplate.opsForValue().get(RedisKey.MESSENGER_MEMBERS + messengerId))) {
                members = getMembers(messengerId);
                redisTemplate.opsForValue().set(RedisKey.MESSENGER_MEMBERS + messengerId, objectMapper.writeValueAsString(members), 1, TimeUnit.HOURS);
            } else {
                String stringMembers = redisTemplate.opsForValue().get(RedisKey.MESSENGER_MEMBERS + messengerId);
                members = objectMapper.readValue(stringMembers, new TypeReference<List<MessengerMemberResponse>>(){} );
            }
            messengerResponse.setMembers(members);

            List<MessageResponse> messages = messageService.search(messengerId);
            messengerResponse.setMessages(messages);

            MessengerProperties properties = getProperties(messengerResponse);
            messengerResponse.setName(properties.getName());
            messengerResponse.setImage(properties.getImage());

            return Response.getResponse(ErrorCode.OK, messengerResponse, ErrorMessage.SUCCESS);
        } catch (Exception e) {
            log.error("An error occurred when messengerService.details, messengerId: {}, error: {}", messengerId, e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }

    private List<MessengerMemberResponse> getMembers (String messengerId) {
        try {
            List<Object[]> membersObject = messengerRepository.getMembers(messengerId);
            List<MessengerMemberResponse> members = new ArrayList<>();
            for (Object[] object : membersObject) {
                MessengerMemberResponse member = new MessengerMemberResponse();
                member.setId((String) object[0]);
                member.setName((String) object[1]);
                member.setProfileImage((String) object[2]);
                members.add(member);
            }

            return members;
        } catch (Exception e) {
            return null;
        }
    }

    private MessengerProperties getProperties (MessengerResponse messenger) {
        if (ObjectUtils.isEmpty(messenger) || ObjectUtils.isEmpty(messenger.getMembers())) {
            log.error("Messenger members cannot be empty!");
            throw new CustomException(EError.BAD_REQUEST);
        }
        MessengerProperties properties = new MessengerProperties();

        if (!ObjectUtils.isEmpty(messenger.getName())) {
            properties.setName(messenger.getName());
            return properties;
        };

        if (messenger.getType().equals(EMessengerType.GROUP.name())) {
            String name = "";
            for (MessengerMemberResponse member : messenger.getMembers()) {
                name += member.getName() + ", ";
            }
            name = name.substring(0, name.length() - 2);
            properties.setName(name);
//            properties.setImage();
            return properties;
        }

        Account currentAccount = authService.getCurrentAccount();
        MessengerMemberResponse anotherMember = messenger.getMembers()
                .stream()
                .filter(member -> !member.getId().equals(currentAccount.getId()))
                .findFirst()
                .orElse(null);
        if (ObjectUtils.isEmpty(anotherMember)) {
            log.error("Messenger members cannot be empty!");
            throw new CustomException(EError.BAD_REQUEST);
        }

        properties.setImage(anotherMember.getProfileImage());
        properties.setName(anotherMember.getName());
        return properties;
    }
}
