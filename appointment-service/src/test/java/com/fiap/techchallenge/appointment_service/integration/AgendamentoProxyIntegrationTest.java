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
public class AgendamentoProxyIntegrationTest {

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
    void criarAgendamento_preservaStatusEBodyDoOrchestrator() {
        stubFor(post(urlEqualTo("/api/agendamento/criacao"))
                .willReturn(aResponse()
                        .withStatus(422)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"unprocessable\"}")));

        String url = "http://localhost:" + port + "/api/agendamento/criacao";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String payload = "{\"dummy\":\"x\"}";

        ResponseEntity<String> resp = restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), String.class);

        assertThat(resp.getStatusCode().value()).isEqualTo(422);
        assertThat(resp.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(resp.getBody()).contains("unprocessable");
    }

    @Test
    void editarAgendamento_preservaStatusEBodyDoOrchestrator() {
        stubFor(put(urlEqualTo("/api/agendamento/edicao/123"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"forbidden\"}")));

        String url = "http://localhost:" + port + "/api/agendamento/edicao/123";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String payload = "{\"dummy\":\"y\"}";

        ResponseEntity<String> resp = restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT,
                new HttpEntity<>(payload, headers), String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(resp.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(resp.getBody()).contains("forbidden");
    }
}
