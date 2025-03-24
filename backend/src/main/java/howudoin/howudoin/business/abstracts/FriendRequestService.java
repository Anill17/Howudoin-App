package howudoin.howudoin.business.abstracts;

import howudoin.howudoin.entities.FriendRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendRequestService {
    ResponseEntity<String> sendFriendRequest(String senderUsername, String receiverUsername);

    ResponseEntity<String> updateFriendRequestStatus(String senderUsername, String receiverName);

    List<String> getFriends(String receiverName);

    public boolean areFriends(String username1, String username2);

    ResponseEntity<List<String>> arkadaslikIstekleriniGetir(String username);

    ResponseEntity<List<String>> arkadaslikIstekleriniGetir2(String username);
}
