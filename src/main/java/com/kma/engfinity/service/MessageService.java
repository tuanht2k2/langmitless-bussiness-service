package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditMessageRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.MessageResponse;
import com.kma.engfinity.entity.Message;
import com.kma.engfinity.entity.Messenger;
import com.kma.engfinity.enums.EMessageType;
import com.kma.engfinity.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    CommonService commonService;

    @Autowired
    MessengerService messengerService;

    @Autowired
    private OpenAIService openAIService;

    public void create(EditMessageRequest request) throws Exception {
        Message message = mapper.map(request, Message.class);
        message.setCreatedBy(authService.getCurrentAccount().getId());
        Message createdMessage = messageRepository.save(message);
        sendMessageToSocket(createdMessage);

        if (request.getType().equals(EMessageType.CHATBOT)) {
            String answer = openAIService.getOpenAiResponse(request.getContent());
            Message chatbotMessage = new Message();
            chatbotMessage.setMessenger(request.getMessenger());
            chatbotMessage.setContent(answer);
            Message createdChatbotMessage = messageRepository.save(message);
            sendMessageToSocket(createdChatbotMessage);
        }
    }

    private void sendMessageToSocket (Message message) {
        MessageResponse response = mapper.map(message, MessageResponse.class);
        String destination = "/topic/messengers/" + message.getMessenger();
        messagingTemplate.convertAndSend(destination, response);
    }
}
