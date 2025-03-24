package howudoin.howudoin.controller;

import howudoin.howudoin.auth.JWTService;
import howudoin.howudoin.business.abstracts.GroupService;
import howudoin.howudoin.business.request.*;
import howudoin.howudoin.entities.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final JWTService jwtService;

    public GroupController(GroupService groupService, JWTService jwtService) {
        this.groupService = groupService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestBody GroupRequestDto groupRequest, HttpServletRequest httpServletRequest) {
        String creatorUsername = extractUsernameFromToken(httpServletRequest);
        return groupService.createGroup(groupRequest.getGroupName(), groupRequest.getMembers(), creatorUsername);

    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<String> addMemberToGroup(
            @PathVariable String groupId,
            @RequestBody FriendDto dto, HttpServletRequest httpServletRequest) {

        try {

            String creatorUsername = extractUsernameFromToken(httpServletRequest);
            groupService.addMemberToGroup(groupId, dto.getReceiverName(), creatorUsername);
            return ResponseEntity.ok("User successfully added to group.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/send")
    public ResponseEntity<String> sendMessageToGroup(
            @PathVariable String groupId,
            @RequestBody GroupMessagesRequestDto messageRequest, HttpServletRequest httpServletRequest) {

        try {
            String username = extractUsernameFromToken(httpServletRequest);

            groupService.sendMessageToGroup(groupId, username, messageRequest.getContent());
            return ResponseEntity.ok("Message sent successfully to group members.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getGroupMessages(@RequestParam String groupId) {
        try {
            List<Message> messages = groupService.getGroupMessages(groupId);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of("Group not found."));
        }
    }

    @GetMapping("/members")
    public ResponseEntity<?> getGroupMembers(@RequestBody GroupIdRequest groupId, HttpServletRequest request) {
        try {
            String requesterUsername = extractUsernameFromToken(request);

            List<String> members = groupService.getGroupMembers(groupId.getGroupId(), requesterUsername);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/getMyGroups")
    public ResponseEntity<?> getMyGroups(HttpServletRequest httpServletRequest) {
        try {
            // Token'dan kullanıcı adını alıyoruz
            String username = extractUsernameFromToken(httpServletRequest);

            // Kullanıcının gruplarını servis katmanından çekiyoruz
            List<GroupDto> groups = groupService.getGroupsByUsername(username);

            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/details/{groupId}")
    public ResponseEntity<?> getGroupDetails(@PathVariable String groupId, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);

        // Grup detaylarını getir ve yanıt olarak dön
        GroupDetailsDto groupDetails = groupService.getGroupDetails(groupId, username);
        if (groupDetails != null) {
            return ResponseEntity.ok(groupDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Group not found or you are not a member of this group.");
        }
    }

    private String extractUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7); // Remove "Bearer " part.
        return jwtService.extractUserName(token);
    }


}
