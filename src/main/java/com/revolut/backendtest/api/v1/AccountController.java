package com.revolut.backendtest.api.v1;

import com.revolut.backendtest.api.v1.dto.AccountDTO;
import com.revolut.backendtest.api.v1.dto.CreateAccountDTO;
import com.revolut.backendtest.api.v1.dto.CreateTransactionDTO;
import com.revolut.backendtest.api.v1.dto.TransactionDTO;
import com.revolut.backendtest.entity.Account;
import com.revolut.backendtest.entity.Transaction;
import com.revolut.backendtest.exceptions.AccountNotFoundException;
import com.revolut.backendtest.service.AccountService;
import io.helidon.webserver.*;

import java.util.Objects;

public class AccountController implements Service {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void update(Routing.Rules rules) {
        rules
                .post("/", Handler.create(CreateAccountDTO.class, this::createAccount))
                .get("/{id}", this::getAccountInfo)
                .post("/{id}/transactions", Handler.create(CreateTransactionDTO.class, this::processTransaction))
                .get("/{id}/transactions", this::getTransactions);
    }

    private void processTransaction(ServerRequest serverRequest, ServerResponse serverResponse,
                                    CreateTransactionDTO createTransactionDTO) {
        String accountId = serverRequest.path().param("id");
        if (Objects.equals(accountId, createTransactionDTO.getDesAccountId())) {
            serverResponse.status(403).send();
        } else {
            try {
                Transaction result = accountService.process(new Transaction.TransactionBuilder()
                        .setAccountId(accountId)
                        .setDesAccountId(createTransactionDTO.getDesAccountId())
                        .setAmount(createTransactionDTO.getAmount())
                        .setMessage(createTransactionDTO.getMessage())
                        .createTransaction());

                serverResponse.send(TransactionDTO.fromTransaction(result));
            } catch (AccountNotFoundException ex) {
                serverResponse.status(404).send();
            }
        }
    }

    private void getTransactions(ServerRequest serverRequest, ServerResponse serverResponse) {
        Account account = accountService.getAccount(serverRequest.path().param("id"));
        if (account == null) {
            serverResponse.status(404).send();
        } else {
            serverResponse.send(account.getTransactions());
        }

    }

    private void getAccountInfo(ServerRequest serverRequest, ServerResponse serverResponse) {
        Account account = accountService.getAccount(serverRequest.path().param("id"));
        if (account != null) {
            serverResponse.send(AccountDTO.fromAccount(account));
        } else {
            serverResponse.status(404).send();
        }
    }

    private void createAccount(ServerRequest serverRequest, ServerResponse serverResponse, CreateAccountDTO createAccountDTO) {
        Account account = accountService.create(createAccountDTO != null && createAccountDTO.isCredit());
        serverResponse.send(AccountDTO.fromAccount(account));
    }

}
