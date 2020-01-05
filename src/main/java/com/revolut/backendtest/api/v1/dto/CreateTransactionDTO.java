package com.revolut.backendtest.api.v1.dto;

import java.util.Objects;

public class CreateTransactionDTO {
    private String message;
    private long amount;
    private String desAccountId;

    public CreateTransactionDTO() {
    }

    public CreateTransactionDTO(String desAccountId, long amount, String message) {
        this.message = message;
        this.amount = amount;
        this.desAccountId = desAccountId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDesAccountId() {
        return desAccountId;
    }

    public void setDesAccountId(String desAccountId) {
        this.desAccountId = desAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTransactionDTO that = (CreateTransactionDTO) o;
        return amount == that.amount &&
                Objects.equals(message, that.message) &&
                desAccountId.equals(that.desAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, amount, desAccountId);
    }
}
