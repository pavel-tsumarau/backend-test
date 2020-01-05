package com.revolut.backendtest.api.v1.dto;

import com.revolut.backendtest.entity.Transaction;

import java.util.Objects;

public class TransactionDTO {
    private String accountId;
    private String message;
    private long amount;
    private String desAccountId;

    public TransactionDTO() {
    }

    public TransactionDTO(String accountId, String desAccountId, long amount, String message) {
        this.accountId = accountId;
        this.message = message;
        this.amount = amount;
        this.desAccountId = desAccountId;
    }

    public static TransactionDTO fromTransaction(Transaction result) {
        return new TransactionDTO(result.getAccountId(),
                result.getDesAccountId(),
                result.getAmount(),
                result.getMessage());
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
        TransactionDTO that = (TransactionDTO) o;
        return amount == that.amount &&
                accountId.equals(that.accountId) &&
                Objects.equals(message, that.message) &&
                desAccountId.equals(that.desAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, message, amount, desAccountId);
    }
}
