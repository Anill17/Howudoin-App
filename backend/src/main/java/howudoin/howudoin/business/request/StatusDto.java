package howudoin.howudoin.business.request;

public class StatusDto {

    private String receiverName;
    private String status;

    // Getters ve Setters
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
