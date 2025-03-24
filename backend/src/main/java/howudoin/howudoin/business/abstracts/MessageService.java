package howudoin.howudoin.business.abstracts;

import howudoin.howudoin.business.request.MessagesDto;
import howudoin.howudoin.entities.Message;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MessageService {
    ResponseEntity<String> sendMessage(String senderUsername, String receiverUsername, String content);

    List<MessagesDto> getConversation(String username, String receiverName);
}
