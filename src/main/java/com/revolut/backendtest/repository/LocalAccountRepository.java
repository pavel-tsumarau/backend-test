package com.revolut.backendtest.repository;

import com.revolut.backendtest.entity.Account;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;
@ThreadSafe
public class LocalAccountRepository implements AccountRepository {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public Account get(String id) {
        return accounts.get(id);
    }
}
