package com.revolut.backendtest.repository;

import com.revolut.backendtest.entity.Account;

public interface AccountRepository {
    Account save(Account account);
    Account get(String id);
}
