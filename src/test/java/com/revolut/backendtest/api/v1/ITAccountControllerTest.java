package com.revolut.backendtest.api.v1;

import com.revolut.backendtest.Application;
import com.revolut.backendtest.api.v1.dto.AccountDTO;
import com.revolut.backendtest.api.v1.dto.CreateAccountDTO;
import com.revolut.backendtest.api.v1.dto.CreateTransactionDTO;
import com.revolut.backendtest.api.v1.dto.TransactionDTO;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ITAccountControllerTest {
    private WebServer ws;
    private RequestSpecification baseRequestSpec;

    @BeforeAll
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        int SERVER_PORT = 5591;
        ws = Application.buildWebServer(ServerConfiguration.builder()
                .port(SERVER_PORT).build());
        ws.start().toCompletableFuture().get(10, TimeUnit.SECONDS);

        baseRequestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(SERVER_PORT)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    void createAccount() {
        given(baseRequestSpec)
                .body("{}")
                .post("/api/v1/account/")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("id", notNullValue())
                .body("amount", equalTo(0))
                .body("credit", equalTo(false))
                .log().all();
    }

    @Test
    void createCreditAccount() {
        given(baseRequestSpec)
                .body(new CreateAccountDTO(true))
                .post("/api/v1/account/")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("id", notNullValue())
                .body("amount", equalTo(0))
                .body("credit", equalTo(true))
                .log().all();
    }

    @Test
    void createAccount_invalidRequest() {
        given(baseRequestSpec)
                .body("{]")
                .post("/api/v1/account/")
                .then()
                .statusCode(500)
                .log().all();
    }

    @Test
    void getAccount() {
        AccountDTO account = createAccount(false);

        AccountDTO response = getAccount(account.getId());

        assertEquals(account, response);
    }

    @Test
    void getAccount_accountNotFound() {
        given(baseRequestSpec)
                .get("/api/v1/account/" + "404_error")
                .then()
                .statusCode(404)
                .log().all();
    }

    @Test
    void createTransaction() {
        AccountDTO fromAccount = createAccount(true);
        AccountDTO toAccount = createAccount(false);
        long transferAmount = 100_000L;

        createTransaction(fromAccount.getId(),
                new CreateTransactionDTO(toAccount.getId(), transferAmount, null));

        assertEquals(-transferAmount, getAccount(fromAccount.getId()).getAmount());
        assertEquals(transferAmount, getAccount(toAccount.getId()).getAmount());
    }

    @Test
    void createTransaction_notEnoughMoney() {
        AccountDTO fromAccount = createAccount(false);
        AccountDTO toAccount = createAccount(false);
        long transferAmount = 100_000L;
        given(baseRequestSpec)
                .body(new CreateTransactionDTO(toAccount.getId(), transferAmount, null))
                .post("/api/v1/account/" + fromAccount.getId() + "/transactions")
                .then()
                .statusCode(500)
                .log().all();
    }

    @Test
    void createTransaction_accountNotFound() {
        AccountDTO fromAccount = createAccount(true);
        long transferAmount = 100_000L;

        given(baseRequestSpec)
                .body( new CreateTransactionDTO(UUID.randomUUID().toString(), transferAmount, null))
                .post("/api/v1/account/" + fromAccount.getId() + "/transactions")
                .then()
                .statusCode(404)
                .log().all();
    }

    @Test
    void getTransactions() {
        AccountDTO fromAccount = createAccount(true);
        AccountDTO toAccount = createAccount(false);
        long transferAmount = 100_000L;
        TransactionDTO transaction = createTransaction(fromAccount.getId(),
                new CreateTransactionDTO(toAccount.getId(), transferAmount, null));


        assertTrue(getTransactions(fromAccount.getId()).contains(transaction));
        assertTrue(getTransactions(toAccount.getId()).contains(transaction));
    }

    private AccountDTO createAccount(boolean credit) {
        return given(baseRequestSpec)
                .body(new CreateAccountDTO(credit))
                .post("/api/v1/account/")
                .then()
                .extract().body().as(AccountDTO.class);
    }

    private AccountDTO getAccount(String accountId) {
        return given(baseRequestSpec)
                .get("/api/v1/account/" + accountId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().body().as(AccountDTO.class);
    }

    private TransactionDTO createTransaction(String fromAccountId, CreateTransactionDTO createTransactionDTO) {
        return given(baseRequestSpec)
                .body(createTransactionDTO)
                .post("/api/v1/account/" + fromAccountId + "/transactions")
                .then()
                .statusCode(200)
                .extract().body().as(TransactionDTO.class);
    }

    private List<TransactionDTO> getTransactions(String accountId) {
        List<TransactionDTO> transactions = given(baseRequestSpec)
                .get("/api/v1/account/" + accountId + "/transactions")
                .then()
                .statusCode(200)
                .extract().body().as(new TypeRef<List<TransactionDTO>>() {
                });
        return transactions;
    }

    @AfterAll
    void shutDown() throws InterruptedException, ExecutionException, TimeoutException {
        ws.shutdown()
                .toCompletableFuture()
                .get(10, TimeUnit.SECONDS);
    }
}