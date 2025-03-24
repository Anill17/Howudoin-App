package howudoin.howudoin.repository;

import howudoin.howudoin.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {

    @Query("{ $or: [ { 'senderUsername': ?0, 'receiverUsername': ?1 }, { 'senderUsername': ?1, 'receiverUsername': ?0 } ] }")
    List<Message> findConversationBetween(String username1, String username2);

    List<Message> findByGroupId(String groupId);
}
