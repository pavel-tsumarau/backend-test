package com.revolut.backendtest;

import com.revolut.backendtest.api.v1.AccountController;
import com.revolut.backendtest.repository.LocalAccountRepository;
import com.revolut.backendtest.service.AccountService;
import com.revolut.backendtest.service.LocalAccountService;
import io.helidon.media.jsonb.server.JsonBindingSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

public class Application {
    public static void main(String[] args) throws Exception {

        WebServer ws = buildWebServer(defaultServerConfig());
        ws.start()
                .thenAccept(server ->
                        System.out.println("Server started at: http://localhost:" + server.port())
                );

    }

    public static AccountService bootstrapLocalAccountService() {
        return new LocalAccountService(new LocalAccountRepository());
    }

    public static ServerConfiguration defaultServerConfig() {
        return ServerConfiguration.builder()
                .port(8080).build();
    }

    public static WebServer buildWebServer(ServerConfiguration serverConfiguration) {
        Routing routing = Routing.builder()
                .register(JsonBindingSupport.create())
                .register("/api/v1/account", new AccountController(bootstrapLocalAccountService()))
                .build();
        return WebServer.create(serverConfiguration, routing);
    }
}
