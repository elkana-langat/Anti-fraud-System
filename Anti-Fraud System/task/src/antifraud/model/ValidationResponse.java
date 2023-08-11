package antifraud.model;

public class ValidationResponse {

    private TransactionStatus result;

    public ValidationResponse() {
    }

    public ValidationResponse(TransactionStatus result) {
        this.result = result;
    }

    public TransactionStatus getResult() {
        return result;
    }

    public void setResult(TransactionStatus result) {
        this.result = result;
    }
}