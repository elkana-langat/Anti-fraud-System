package antifraud.model;

public class SuspiciousIPResponse {

    private String status;

    public SuspiciousIPResponse() {
    }

    public SuspiciousIPResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
