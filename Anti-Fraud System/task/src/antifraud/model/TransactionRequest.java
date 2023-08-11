package antifraud.model;

import jakarta.validation.constraints.NotNull;

public class TransactionRequest {

    @NotNull
    private Long amount;

    public TransactionRequest() {
    }

    public TransactionRequest(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}