package howudoin.howudoin.business.concretes;

import howudoin.howudoin.business.abstracts.FriendRequestService;
import howudoin.howudoin.entities.Friend;
import howudoin.howudoin.entities.FriendRequest;
import howudoin.howudoin.entities.User;
import howudoin.howudoin.repository.FriendRepository;
import howudoin.howudoin.repository.FriendRequestRepository;
import howudoin.howudoin.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestManager implements FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendRequestManager(FriendRequestRepository friendRequestRepository, UserRepository userRepository, FriendRepository friendRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    @Override
    public ResponseEntity<String> sendFriendRequest(String senderUsername, String receiverName) {


        boolean requestExists = friendRequestRepository.existsBySenderUsernameAndReceiverName(senderUsername, receiverName);

        if (requestExists) {
            return ResponseEntity.badRequest().body("A friend request has already been sent to that user.");
        }
        // find the reciever on the name 
        User receiver = userRepository.findByUsername(receiverName)
                .orElseThrow(() -> new IllegalArgumentException(receiverName + " is not exist"));

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderUsername(senderUsername);
        friendRequest.setReceiverName(receiverName);
        friendRequestRepository.save(friendRequest);
        return ResponseEntity.ok("Friend request sent");
    }

    @Override
    public ResponseEntity<String> updateFriendRequestStatus(String senderUsername, String receiverName) {

        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findBySenderUsernameAndReceiverName(senderUsername, receiverName);

        if (friendRequestOpt.isPresent()) {
            // if the request is exist, save the friend object and remove the request
            Friend friend = new Friend();
            friend.setFriend1(senderUsername);
            friend.setFriend2(receiverName);

            friendRepository.save(friend);
            friendRequestRepository.delete(friendRequestOpt.get()); // remove the request

            return ResponseEntity.ok("Friend request accepted");
        } else {
            // if there is no friend request
            return ResponseEntity.badRequest().body("There is no such a friend request.");
        }
    }

    @Override
    public List<String> getFriends(String username) {
        // find all the friends
        List<Friend> friends = friendRepository.findByFriend1OrFriend2(username, username);

        List<String> friendList = new ArrayList<>();

        for (Friend friend : friends) {
            // if the user is friend1 add the friend to vice versa.
            if (friend.getFriend1().equals(username)) {
                friendList.add(friend.getFriend2());
            } else {
                friendList.add(friend.getFriend1());
            }
        }

        return friendList;
    }

    public boolean areFriends(String username1, String username2) {
        List<Friend> friends = friendRepository.findByFriend1OrFriend2(username1, username1);
        return friends.stream().anyMatch(friend ->
                (friend.getFriend1().equals(username1) && friend.getFriend2().equals(username2)) ||
                        (friend.getFriend1().equals(username2) && friend.getFriend2().equals(username1))
        );
    }

    @Override
    public ResponseEntity<List<String>> arkadaslikIstekleriniGetir(String username) {

        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverName(username);
        List<String> friendRequestList = new ArrayList<>();
        for(FriendRequest friendRequest : friendRequests){
            friendRequestList.add(friendRequest.getSenderUsername());
        }

        return ResponseEntity.ok(friendRequestList);
    }

    @Override
    public ResponseEntity<List<String>> arkadaslikIstekleriniGetir2(String username) {
        List<FriendRequest> friendRequests = friendRequestRepository.findBySenderUsername(username);
        List<String> friendRequestList = new ArrayList<>();
        for(FriendRequest friendRequest : friendRequests){
            friendRequestList.add(friendRequest.getReceiverName());
        }
        return ResponseEntity.ok(friendRequestList);
    }


}
