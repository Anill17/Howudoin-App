package howudoin.howudoin.repository;

import howudoin.howudoin.entities.Friend;
import howudoin.howudoin.entities.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRepository extends MongoRepository<Friend,String> {
    List<Friend> findByFriend1OrFriend2(String friend1, String friend2);
}
