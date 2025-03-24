package howudoin.howudoin.business.concretes;

import howudoin.howudoin.business.abstracts.GroupService;
import howudoin.howudoin.business.request.GroupDetailsDto;
import howudoin.howudoin.business.request.GroupDto;
import howudoin.howudoin.entities.Group;
import howudoin.howudoin.entities.Message;
import howudoin.howudoin.repository.GroupRepository;
import howudoin.howudoin.repository.MessageRepository;
import howudoin.howudoin.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GroupManager implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public GroupManager(GroupRepository groupRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public ResponseEntity<String> createGroup(String groupName, List<String> members, String creatorUsername) {
        // check all the users if they are exist or not 
        boolean allMembersValid = members.stream()
                .allMatch(username -> userRepository.findByUsername(username).isPresent());

        if (!allMembersValid) {
            ResponseEntity.badRequest().body("all the users have to be exist on the system.");
        }

        // add user to group where the user is the first member of the group
        if (!members.contains(creatorUsername)) {
            members = new ArrayList<>(members); // create a new list , original list may be immutable 
            members.add(creatorUsername);
        }

        Group group = new Group();
        group.setGroupName(groupName);
        group.setMembers(members);

        groupRepository.save(group);
        return ResponseEntity.ok("the group is created ");
    }

    @Override
    public void addMemberToGroup(String groupId, String newMemberUsername, String creatorUsername){
        // call the group
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found."));

        if(!group.getMembers().contains(creatorUsername)){
            throw new IllegalArgumentException("No permission to add a member to the group because you are not a member of the group.");
        }
        // check if the user is registered or not 
        if (userRepository.findByUsername(newMemberUsername).isEmpty()) {
            throw new IllegalArgumentException("the user you try to add does not exist.");
        }


        // check if the user is already in the group or not 
        if (group.getMembers().contains(newMemberUsername)) {
            throw new IllegalArgumentException("This user is already in the group.");
        }



        // add the user to the group 
        group.getMembers().add(newMemberUsername);
        groupRepository.save(group);
    }

    @Override
    public void sendMessageToGroup(String groupId, String senderUsername, String content) {
        // call the group
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("group not found."));

    
        if (!group.getMembers().contains(senderUsername)) {
            throw new IllegalArgumentException("The person who sent the message is not a member of this group..");
        }

        // send message to the group members (except the sender)
        for (String member : group.getMembers()) {
            if (!member.equals(senderUsername)) {  // excluding the sender
                Message message = new Message();
                message.setSenderUsername(senderUsername);
                message.setReceiverUsername(member);
                message.setContent(content);
                message.setGroupId(groupId);
                message.setTimestamp(LocalDateTime.now());

                messageRepository.save(message);
            }
        }
    }

    @Override
    public List<Message> getGroupMessages(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found."));

        List<Message> messages = messageRepository.findByGroupId(groupId);

        return messages;
    }

    @Override
    public List<String> getGroupMembers(String groupId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found."));

        // Check if the user is a member of the group or not.
        if (!group.getMembers().contains(requesterUsername)) {
            throw new IllegalArgumentException("You are not allowed to access the group.");
        }

        return group.getMembers();
    }

    @Override
    public List<GroupDto> getGroupsByUsername(String username) {
        // Kullanıcının üyesi olduğu grupları veritabanından alıyoruz
        List<Group> groups = groupRepository.findByMembersContaining(username);

        // Grup bilgilerini DTO'ya dönüştürüyoruz
        return groups.stream()
                .map(group -> new GroupDto(group.getId(), group.getGroupName(), group.getMembers()))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDetailsDto getGroupDetails(String groupId, String username) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();

            // Kullanıcının grup üyesi olup olmadığını kontrol et
            if (!group.getMembers().contains(username)) {
                throw new IllegalArgumentException("You are not a member of this group.");
            }

            // DTO'ya detayları doldur
            GroupDetailsDto groupDetails = new GroupDetailsDto();
            groupDetails.setGroupId(group.getId());
            groupDetails.setGroupName(group.getGroupName());
            groupDetails.setCreatedAt(group.getCreatedAt());
            groupDetails.setMembers(group.getMembers());

            return groupDetails;
        }

        return null;
    }

}
