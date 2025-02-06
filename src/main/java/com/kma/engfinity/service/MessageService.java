package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditMessageRequest;
import com.kma.engfinity.DTO.request.SearchMessageRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.MessageResponse;
import com.kma.engfinity.DTO.response.NotificationResponse;
import com.kma.engfinity.DTO.response.PublicAccountResponse;
import com.kma.engfinity.entity.Message;
import com.kma.engfinity.entity.Messenger;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.MessageRepository;
import com.kma.engfinity.repository.MessengerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    CommonService commonService;

    public ResponseEntity<?> create (EditMessageRequest request) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(request.getMessengerId());
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);

        Messenger messenger = optionalMessenger.get();
        messenger.setUpdatedAt(new Date());
        messengerRepository.save(messenger);

        Message message = new Message();
        Account currentAccount = authService.getCurrentAccount();
        message.setCreatedBy(currentAccount);
        message.setCreatedAt(new Date());
        message.setMessenger(messenger);
        message.setContent(request.getContent());
        Message createdMessage = messageRepository.save(message); // tạo tin nhắn

        String destination = "/topic/messengers/" + request.getMessengerId() + "/messages";
        messagingTemplate.convertAndSend(destination, messageToMessageResponse(createdMessage)); // lấy đường dẫn: kênh

        Set<Account> members = messenger.getMembers(); // lấy hết người dùng thuộc cuộc trò chuyện
        for (Account member : members) {
            if (!member.getId().equals(currentAccount.getId())) {
                String notificationDes = commonService.getAccountNotificationUrl(member.getId());
                NotificationResponse notification = new NotificationResponse();
                String notificationMessage = currentAccount.getName() + " đã gửi tin nhắn";
                notification.setMessage(notificationMessage);
                messagingTemplate.convertAndSend(notificationDes, notification);
            }
        }

        CommonResponse<?> response = new CommonResponse<>(200, null, "Send message successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MessageResponse messageToMessageResponse (Message message) {
        MessageResponse messageResponse = mapper.map(message, MessageResponse.class);
        messageResponse.setMessengerId(message.getMessenger().getId());
        PublicAccountResponse accountResponse = mapper.map(message.getCreatedBy(), PublicAccountResponse.class);
        messageResponse.setCreatedBy(accountResponse);
        return messageResponse;
    }

    public ResponseEntity<?> search(SearchMessageRequest request) {
        List<Message> messages = messageRepository.findAllByMessengerIdOrderByCreatedAtAsc(request.getMessengerId());
        List<MessageResponse> messageResponses = messages.stream().map(this::messageToMessageResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, messageResponses, "Search messages successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
