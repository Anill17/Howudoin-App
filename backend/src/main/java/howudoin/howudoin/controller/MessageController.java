package howudoin.howudoin.controller;

import howudoin.howudoin.auth.JWTService;
import howudoin.howudoin.business.abstracts.MessageService;
import howudoin.howudoin.business.request.FriendDto;
import howudoin.howudoin.business.request.MessagesDto;
import howudoin.howudoin.business.request.MessagesRequestDto;
import howudoin.howudoin.entities.Friend;
import howudoin.howudoin.entities.Message;
import howudoin.howudoin.repository.FriendRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final JWTService jwtService;
    private final MessageService messageService;

    public MessageController(JWTService jwtService, MessageService messageService) {
        this.jwtService = jwtService;
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessagesRequestDto messageRequest,HttpServletRequest httpServletRequest) {

        String senderUserName = extractUsernameFromToken(httpServletRequest);
        return messageService.sendMessage(
                senderUserName,
                messageRequest.getReceiverUsername(),
                messageRequest.getContent()
        );
    }

    @GetMapping("/getmessages")
    public ResponseEntity<?> getConversation(
            @RequestParam String receiverName, // Parametreyi URL parametresi olarak alıyoruz
            HttpServletRequest httpServletRequest
    ) {
        try {
            String username = extractUsernameFromToken(httpServletRequest);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token validation failed");
            }

            List<MessagesDto> messages = messageService.getConversation(username, receiverName);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }

    private String extractUsernameFromToken(HttpServletRequest request) {
        // Get Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7); // Remove "Bearer " part 

        // Token authentication and extracvting the username 
        // For example: parseable from JWTTokenProvider
        return jwtService.extractUserName(token);
    }


}
