package com.revolut.backendtest.service;

import com.revolut.backendtest.entity.Account;
import com.revolut.backendtest.entity.Transaction;
import com.revolut.backendtest.exceptions.AccountNotFoundException;
import com.revolut.backendtest.exceptions.NotEnoughFoundsException;
import com.revolut.backendtest.repository.AccountRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LocalAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final ConcurrentHashMap<String, Object> looks = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public LocalAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(boolean credit) {
        return accountRepository.save(new Account.AccountBuilder()
                .setId("acc:" + idSequence.incrementAndGet())
                .setCredit(credit)
                .createAccount());
    }

    @Override
    public Transaction process(Transaction transaction) {

        int order = transaction.getAccountId().compareTo(transaction.getDesAccountId());

        synchronized (order < 0 ? getLock(transaction.getAccountId()) : getLock(transaction.getDesAccountId())) {
            synchronized (order > 0 ? getLock(transaction.getAccountId()) : getLock(transaction.getDesAccountId())) {
                Account account = accountRepository.get(transaction.getAccountId());
                Account desAccount = accountRepository.get(transaction.getDesAccountId());
                if (account == null || desAccount == null) {
                    throw new AccountNotFoundException("Account " +
                            (account == null ? transaction.getAccountId() : transaction.getDesAccountId())
                            + " not found");
                }
                if (account.isCredit() || account.getAmount() - transaction.getAmount() >= 0) {
                    updateAccount(account,
                            account.getAmount() - transaction.getAmount(),
                            transaction);
                } else {
                    throw new NotEnoughFoundsException("Account " + account.getId() + " doesn't have enough money");
                }

                updateAccount(desAccount,
                        desAccount.getAmount() + transaction.getAmount(),
                        transaction);
            }
        }
        return transaction;
    }

    private void updateAccount(Account account, long newAmount, Transaction transaction) {
        Collection<Transaction> transactions = new ArrayList<>(account.getTransactions().size() + 1);
        transactions.add(transaction);
        transactions.addAll(account.getTransactions());

        accountRepository.save(account.toBuilder()
                .setAmount(newAmount)
                .setTransactions(transactions)
                .createAccount());
    }

    private Object getLock(String id) {
        return looks.computeIfAbsent(id, ignored -> new Object());
    }

    @Override
    public Account getAccount(String id) {
        return accountRepository.get(id);
    }
}
