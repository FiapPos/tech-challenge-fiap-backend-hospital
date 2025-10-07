package com.fiap.techchallenge.appointment_service.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTest {

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        // Use a strong secret (>= 32 bytes) for HMAC-SHA
        String strongSecret = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXabcdefghijklmnopqrstuvwxyz012345";
        registry.add("jwt.secret", () -> strongSecret);
        registry.add("usuario-service.url", () -> "http://localhost:18080");
    }

    private static WireMockServer wireMock;

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    org.springframework.core.env.Environment env;

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
    void login_delegaParaServicoDeUsuario_eRetornaToken() {

        String jwtSecret = env.getProperty("jwt.secret", "mySecretKey");

        String jwt = Jwts.builder()
                .setSubject("paciente")
                .claim("perfil", "PACIENTE")
                .claim("roles", "ROLE_PACIENTE")
                .claim("userId", 123)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400_000))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();

        stubFor(post(urlEqualTo("/api/auth/login"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\":\"" + jwt + "\"}")));

        String url = "http://localhost:" + port + "/api/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String payload = "{\"login\":\"paciente\",\"senha\":\"senha123\",\"perfil\":\"PACIENTE\"}";

        ResponseEntity<String> resp = restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getHeaders().getContentType().toString()).contains("application/json");
        assertThat(resp.getBody()).contains("token");

        assertThat(resp.getBody()).contains(jwt);
    }
}
