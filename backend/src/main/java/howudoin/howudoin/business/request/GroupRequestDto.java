package howudoin.howudoin.business.request;

import java.util.List;

public class GroupRequestDto {

    private String groupName;
    private List<String> members;

    // Getters ve Setters
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
