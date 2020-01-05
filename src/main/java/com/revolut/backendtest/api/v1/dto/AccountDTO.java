package com.revolut.backendtest.api.v1.dto;

import com.revolut.backendtest.entity.Account;

import java.util.Objects;

public class AccountDTO {
    private final boolean credit;
    private final long amount;
    private final String id;

    public AccountDTO(String id, long amount, boolean credit) {
        this.id = id;
        this.amount = amount;
        this.credit = credit;
    }

    public boolean isCredit() {
        return credit;
    }

    public long getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public static AccountDTO fromAccount(Account account) {
        return new AccountDTO(account.getId(), account.getAmount(), account.isCredit());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return credit == that.credit &&
                amount == that.amount &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credit, amount, id);
    }
}
