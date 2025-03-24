package howudoin.howudoin.controller;

import howudoin.howudoin.auth.JWTService;
import howudoin.howudoin.business.abstracts.FriendRequestService;
import howudoin.howudoin.business.request.FriendDto;
import howudoin.howudoin.business.request.StatusDto;
import howudoin.howudoin.entities.FriendRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("friends")
public class FriendRequestController {


    private final FriendRequestService friendRequestService;
    private final JWTService jwtService;
    public FriendRequestController(FriendRequestService friendRequestService, JWTService jwtService) {
        this.friendRequestService = friendRequestService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendDto dto, HttpServletRequest httpServletRequest) {
        // get teh user name from JWT
        String senderUsername = extractUsernameFromToken(httpServletRequest);

        return friendRequestService.sendFriendRequest(senderUsername, dto.getReceiverName());
    }

    @PostMapping("/accept")
    public ResponseEntity<String> updateFriendRequestStatus( @RequestBody FriendDto dto,HttpServletRequest httpServletRequest) {
        String receiverName = extractUsernameFromToken(httpServletRequest);
        String senderUsername = dto.getReceiverName();

        // update the status 
        return friendRequestService.updateFriendRequestStatus(senderUsername, receiverName);
    }

    @GetMapping("/getArkadaslikIstekleri")
    public ResponseEntity<List<String>> arkadaslikIstekleriniGetir(HttpServletRequest httpServletRequest){

        String username= extractUsernameFromToken(httpServletRequest);
        return friendRequestService.arkadaslikIstekleriniGetir(username);
    }

    @GetMapping("/getArkadaslikIstekleri2")
    public ResponseEntity<List<String>> arkadaslikIstekleriniGetir2(HttpServletRequest httpServletRequest){

        String username= extractUsernameFromToken(httpServletRequest);
        return friendRequestService.arkadaslikIstekleriniGetir2(username);
    }


    @GetMapping()
    public ResponseEntity<List<String>> getFriends(HttpServletRequest httpServletRequest) {
        String username = extractUsernameFromToken(httpServletRequest);

        List<String> friends = friendRequestService.getFriends(username);
        return ResponseEntity.ok(friends);
    }

    private String extractUsernameFromToken(HttpServletRequest request) {
        // Get the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7); // Remove the "Bearer " part

        // Token authentication and extracvting the username 
        // For example: parseable from JWTTokenProvider
        return jwtService.extractUserName(token);
    }


}
