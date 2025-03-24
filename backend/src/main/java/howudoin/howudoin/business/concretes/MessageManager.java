package howudoin.howudoin.business.concretes;

import howudoin.howudoin.business.abstracts.FriendRequestService;
import howudoin.howudoin.business.abstracts.MessageService;
import howudoin.howudoin.business.request.MessagesDto;
import howudoin.howudoin.entities.Message;
import howudoin.howudoin.repository.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageManager implements MessageService {

    private final MessageRepository messageRepository;
    private final FriendRequestService friendRequestService;

    public MessageManager(MessageRepository messageRepository, FriendRequestService friendRequestService) {
        this.messageRepository = messageRepository;
        this.friendRequestService = friendRequestService;
    }

    @Override
    public ResponseEntity<String> sendMessage(String senderUsername, String receiverUsername, String content) {
        // firstly check the friendship
        if (!friendRequestService.areFriends(senderUsername, receiverUsername)) {
            return ResponseEntity.badRequest().body("You can't send messages to this person. You are not friends.");
        }

        // send message to the friend
        Message message = new Message();
        message.setSenderUsername(senderUsername);
        message.setReceiverUsername(receiverUsername);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok("Message sent successfully.");
    }

    @Override
    public List<MessagesDto> getConversation(String username1, String username2) {
        // firstly check the friendship
        if (!friendRequestService.areFriends(username1, username2)) {
            throw new IllegalArgumentException("These two users are not friends.");
        }

        // call the chat history
        List<Message> messages = messageRepository.findConversationBetween(username1, username2);

        List<MessagesDto> messagesDtos = new ArrayList<>();

        for (Message message : messages) {
            MessagesDto dto = new MessagesDto();
            dto.setSenderUsername(message.getSenderUsername());
            dto.setReceiverUsername(message.getReceiverUsername());
            dto.setContent(message.getContent());
            dto.setTimestamp(message.getTimestamp());

            messagesDtos.add(dto); // add new DTO the the list.
        }
        return messagesDtos;
    }
}
