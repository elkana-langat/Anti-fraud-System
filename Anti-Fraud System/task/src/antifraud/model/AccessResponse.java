package antifraud.model;

public class AccessResponse {

    private String status;

    public AccessResponse() {
    }

    public AccessResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
