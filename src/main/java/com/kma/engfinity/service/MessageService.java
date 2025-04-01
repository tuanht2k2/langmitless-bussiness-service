package com.kma.engfinity.service;

import com.kma.common.dto.response.ChatbotResponse;
import com.kma.common.entity.Account;
import com.kma.engfinity.repository.MessageRepository;
import com.kma.engfinity.repository.MessengerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
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

//    public ResponseEntity<?> create (EditMessageRequest request) {
//        Optional<Messenger> optionalMessenger = messengerRepository.findById(request.getMessengerId());
//        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
//
//        Messenger messenger = optionalMessenger.get();
//        messenger.setUpdatedAt(new Date());
//        messengerRepository.save(messenger);
//
//        Message message = new Message();
//        Account currentAccount = authService.getCurrentAccount();
//        message.setCreatedBy(currentAccount);
//        message.setCreatedAt(new Date());
//        message.setMessenger(messenger);
//        message.setContent(request.getContent());
//        Message createdMessage = messageRepository.save(message); // tạo tin nhắn
//
//        String destination = "/topic/messengers/" + request.getMessengerId() + "/messages";
//        messagingTemplate.convertAndSend(destination, messageToMessageResponse(createdMessage)); // lấy đường dẫn: kênh
//
//        Set<Account> members = messenger.getMembers(); // lấy hết người dùng thuộc cuộc trò chuyện
//        for (Account member : members) {
//            if (!member.getId().equals(currentAccount.getId())) {
//                String notificationDes = commonService.getAccountNotificationUrl(member.getId());
//                NotificationResponse notification = new NotificationResponse();
//                String notificationMessage = currentAccount.getName() + " đã gửi tin nhắn";
//                notification.setMessage(notificationMessage);
//                messagingTemplate.convertAndSend(notificationDes, notification);
//            }
//        }
//
//        CommonResponse<?> response = new CommonResponse<>(200, null, "Send message successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

//    private MessageResponse messageToMessageResponse (Message message) {
//        MessageResponse messageResponse = mapper.map(message, MessageResponse.class);
//        messageResponse.setMessengerId(message.getMessenger().getId());
//        PublicAccountResponse accountResponse = mapper.map(message.getCreatedBy(), PublicAccountResponse.class);
//        messageResponse.setCreatedBy(accountResponse);
//        return messageResponse;
//    }

//    public ResponseEntity<?> search(SearchMessageRequest request) {
//        List<Message> messages = messageRepository.findAllByMessengerIdOrderByCreatedAtAsc(request.getMessengerId());
//        List<MessageResponse> messageResponses = messages.stream().map(this::messageToMessageResponse).toList();
//        CommonResponse<?> response = new CommonResponse<>(200, messageResponses, "Search messages successfully!");
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    public void sendChatbotMessage (ChatbotResponse chatbotResponse) {
        try {
            Account account = authService.getCurrentAccount();
            String destination = "/topic/chatbot/" + account.getId() + "/messages";
            messagingTemplate.convertAndSend(destination, chatbotResponse);
        } catch (Exception e) {
            log.error("An error happened when send chatbot message: {}", e.getMessage());
        }
    }


}
