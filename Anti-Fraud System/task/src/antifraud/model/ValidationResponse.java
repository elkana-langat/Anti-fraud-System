package antifraud.model;

public class ValidationResponse {

    private String result;

    private String info;

    public ValidationResponse() {
    }

    public ValidationResponse(String result, String info) {
        this.result = result;
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}