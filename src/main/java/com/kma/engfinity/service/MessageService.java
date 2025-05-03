package com.kma.engfinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kma.common.dto.response.ChatbotResponse;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditMessageRequest;
import com.kma.engfinity.DTO.response.MessageResponse;
import com.kma.engfinity.DTO.response.NotificationResponse;
import com.kma.engfinity.DTO.response.PublicAccountResponse;
import com.kma.engfinity.entity.AccountMessenger;
import com.kma.engfinity.entity.Message;
import com.kma.engfinity.entity.Messenger;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountMessengerRepository;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.MessageRepository;
import com.kma.engfinity.repository.MessengerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AccountMessengerRepository accountMessengerRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebSocketService webSocketService;

    @Autowired
    CommonService commonService;

    @Autowired
    FileService fileService;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Response<Object> create (EditMessageRequest request) {
        try {
            Messenger messenger = messengerRepository.findById(request.getMessengerId()).orElse(null);
            if (ObjectUtils.isEmpty(messenger)) {
                return Response.getResponse(400, "MESSENGER_NOT_EXIST");
            }

            Account currentAccount = authService.getCurrentAccount();

            Message message = new Message();
            message.setContent(request.getContent());
            message.setCreatedBy(currentAccount.getId());
            message.setCreatedAt(new Date());
            message.setMessengerId(messenger.getId());

            if (request.getFiles() != null && !request.getFiles().isEmpty()) {
                String fileUrl = fileService.getFileUrl(request.getFiles().get(0));
                message.setFileUrl(fileUrl);
            }

            Message createdMessage = messageRepository.save(message);
            messenger.setUpdatedAt(new Date());
            messengerRepository.save(messenger);

            String destination = "/topic/messengers/" + request.getMessengerId() + "/messages";
            webSocketService.sendData(destination, messageToMessageResponse(createdMessage));

            List<AccountMessenger> accountMessengers = accountMessengerRepository.findAllByMessengerId(messenger.getId());
            for (AccountMessenger accountMessenger : accountMessengers) {
                if (!currentAccount.getId().equals(accountMessenger.getAccountId())) {
                    String notificationDes = commonService.getAccountNotificationUrl(accountMessenger.getAccountId());
                    NotificationResponse notification = new NotificationResponse();
                    String notificationMessage = currentAccount.getName() + " đã gửi tin nhắn";
                    notification.setMessage(notificationMessage);
                    webSocketService.sendData(notificationDes, notification);
                }
            }

            return Response.getResponse(200, "Send message successfully");
        } catch (Exception e) {
            log.info("An error occurred when create message: {}", e.getMessage());
            return Response.getResponse(500, "An error occurred when create message: {}", e.getMessage());
        }
    }

    private MessageResponse messageToMessageResponse (Message message) {
        MessageResponse response = new MessageResponse();
        response.setContent(message.getContent());
        response.setId(message.getId());
        response.setCreatedAt(message.getCreatedAt());
        response.setMessengerId(message.getMessengerId());
        response.setFileUrl(message.getFileUrl());
        Account account = accountRepository.findById(message.getCreatedBy()).orElse(null);
        if (account == null) {
            throw new CustomException(EError.BAD_REQUEST);
        }
        PublicAccountResponse accountResponse = objectMapper.convertValue(account, PublicAccountResponse.class);
        response.setCreatedBy(accountResponse);
        return response;
    }

    public List<MessageResponse> search (String messengerId) {
        try {
            List<Object[]> messages = messageRepository.search(messengerId);
            List<MessageResponse> messageResponses = new ArrayList<>();
            for (Object[] messageObject : messages) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setId(messageObject[0].toString());
                messageResponse.setCreatedAt((Date) messageObject[2]);
                messageResponse.setMessengerId((String) messageObject[3]);
                messageResponse.setFileUrl((String) messageObject[4]);
                messageResponse.setContent((String) messageObject[5]);

                PublicAccountResponse createdBy = new PublicAccountResponse();
                createdBy.setId((String) messageObject[6]);
                createdBy.setName((String) messageObject[7]);
                createdBy.setProfileImage((String) messageObject[8]);
                messageResponse.setCreatedBy(createdBy);

                messageResponses.add(messageResponse);
            }
            return messageResponses;
        } catch (Exception e) {
            log.error("An error occurred when messageService.search, messengerId: {}, error: {}", messengerId, e.getMessage());
            return null;
        }
    }

    public void sendChatbotMessage (ChatbotResponse chatbotResponse) {
        try {
            Account account = authService.getCurrentAccount();
            String destination = "/topic/chatbot/" + account.getId() + "/messages";
            webSocketService.sendData(destination, chatbotResponse);
        } catch (Exception e) {
            log.error("An error happened when send chatbot message: {}", e.getMessage());
        }
    }
}
