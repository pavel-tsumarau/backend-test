package com.revolut.backendtest.service;

import com.revolut.backendtest.entity.Account;
import com.revolut.backendtest.entity.Transaction;

public interface AccountService {
    Account create(boolean credit);

    Transaction process(Transaction transaction);

    Account getAccount(String id);
}
