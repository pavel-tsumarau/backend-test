package com.revolut.backendtest.entity;

public class Transaction {
    private final String accountId;
    private final String message;
    private final long amount;
    private final String desAccountId;

    private Transaction(String accountId, String desAccountId, long amount, String message) {
        this.accountId = accountId;
        this.desAccountId = desAccountId;
        this.message = message;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMessage() {
        return message;
    }

    public long getAmount() {
        return amount;
    }

    public String getDesAccountId() {
        return desAccountId;
    }

    public TransactionBuilder toBuilder() {
        return new TransactionBuilder()
                .setAccountId(accountId)
                .setDesAccountId(desAccountId)
                .setAmount(amount)
                .setMessage(message);
    }

    public static class TransactionBuilder {
        private String accountId;
        private String message;
        private long amount;
        private String desAccountId;

        public TransactionBuilder() {
        }

        public TransactionBuilder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public TransactionBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public TransactionBuilder setAmount(long amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder setDesAccountId(String desAccountId) {
            this.desAccountId = desAccountId;
            return this;
        }

        public Transaction createTransaction() {
            return new Transaction(accountId, desAccountId, amount, message);
        }
    }
}
