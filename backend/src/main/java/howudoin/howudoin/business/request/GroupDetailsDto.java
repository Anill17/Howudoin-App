package howudoin.howudoin.business.request;

import java.time.LocalDateTime;
import java.util.List;

public class GroupDetailsDto {

    private String groupId;
    private String groupName;
    private LocalDateTime createdAt;
    private List<String> members;

    // Getter ve Setter'lar
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
