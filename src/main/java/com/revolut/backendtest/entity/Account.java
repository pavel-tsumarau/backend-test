package com.revolut.backendtest.entity;

import net.jcip.annotations.Immutable;

import java.util.Collection;
import java.util.Collections;

@Immutable
public class Account {
    private final String id;
    private final long amount;
    private final boolean credit;
    private final Collection<Transaction> transactions;

    public Account(String id, long amount, boolean credit, Collection<Transaction> transactions) {
        this.id = id;
        this.amount = amount;
        this.credit = credit;
        if (transactions == null) {
            transactions = Collections.emptyList();
        }
        this.transactions = Collections.unmodifiableCollection(transactions);
    }

    public String getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isCredit() {
        return credit;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public AccountBuilder toBuilder() {
        return new AccountBuilder()
                .setId(id)
                .setAmount(amount)
                .setCredit(credit)
                .setTransactions(transactions);
    }

    public static class AccountBuilder {
        private String id;
        private long amount = 0L;
        private boolean credit;
        private Collection<Transaction> transactions = null;

        public AccountBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public AccountBuilder setAmount(long amount) {
            this.amount = amount;
            return this;
        }

        public AccountBuilder setCredit(boolean credit) {
            this.credit = credit;
            return this;
        }

        public AccountBuilder setTransactions(Collection<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Account createAccount() {
            return new Account(id, amount, credit, transactions);
        }
    }
}
