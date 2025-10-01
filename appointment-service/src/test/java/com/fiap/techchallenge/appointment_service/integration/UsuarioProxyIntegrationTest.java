package com.fiap.techchallenge.appointment_service.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "orchestrator-service.url=http://localhost:18080" })
public class UsuarioProxyIntegrationTest {

    private static WireMockServer wireMock;

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    static void startWireMock() {
        wireMock = new WireMockServer(18080);
        wireMock.start();
        WireMock.configureFor("localhost", 18080);
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMock != null)
            wireMock.stop();
    }

    @Test
    void criarUsuario_preservaStatusEBodyDoOrchestrator() {
        stubFor(post(urlEqualTo("/api/usuario/criacao"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"bad request\"}")));

        String url = "http://localhost:" + port + "/api/usuarios";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String payload = "{\"login\":\"u1\",\"senha\":\"pwd\"}";

        ResponseEntity<String> resp = restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(resp.getBody()).contains("bad request");
    }
}
