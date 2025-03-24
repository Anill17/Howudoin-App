package howudoin.howudoin.repository;

import howudoin.howudoin.entities.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends MongoRepository<FriendRequest,String> {
    boolean existsBySenderUsernameAndReceiverName(String senderUsername, String receiverName);
    Optional<FriendRequest> findBySenderUsernameAndReceiverName(String senderUsername, String receiverName);


    List<FriendRequest> findBySenderUsername(String username);

    List<FriendRequest> findByReceiverName(String username);
}
