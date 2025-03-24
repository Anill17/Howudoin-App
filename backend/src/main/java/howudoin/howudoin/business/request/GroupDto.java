package howudoin.howudoin.business.request;

import java.util.List;

public class GroupDto {
    private String id;
    private String groupName;
    private List<String> members;


    public GroupDto(String id, String groupName, List<String> members) {
        this.id = id;
        this.groupName = groupName;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
