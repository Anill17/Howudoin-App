package howudoin.howudoin.business.abstracts;

import howudoin.howudoin.business.request.GroupDetailsDto;
import howudoin.howudoin.business.request.GroupDto;
import howudoin.howudoin.entities.Message;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {

    public ResponseEntity<String> createGroup(String groupName, List<String> members, String creatorUsername);

    void addMemberToGroup(String groupId, String newMemberUsername, String creatorUsername);

    void sendMessageToGroup(String groupId, String senderUsername, String content);

    List<Message> getGroupMessages(String groupId);

    List<String> getGroupMembers(String groupId, String requesterUsername);

    List<GroupDto> getGroupsByUsername(String username);

    GroupDetailsDto getGroupDetails(String groupId, String username);
}
