package howudoin.howudoin.repository;

import howudoin.howudoin.entities.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group,String> {
    List<Group> findByMembersContaining(String username);
}
