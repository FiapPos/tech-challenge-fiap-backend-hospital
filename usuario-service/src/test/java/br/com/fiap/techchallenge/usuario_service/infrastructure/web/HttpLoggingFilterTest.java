package br.com.fiap.techchallenge.usuario_service.infrastructure.web;

import br.com.fiap.techchallenge.infrastructure.web.HttpLoggingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HttpLoggingFilterTest {

    @Test
    void deveLogarEManterCorpoDaResposta() throws ServletException, IOException {
        HttpLoggingFilter filter = new HttpLoggingFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/teste");
        request.setQueryString("a=1&b=2");
        request.setContentType("application/json");
        request.setContent("{\"x\":1}".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {
            // Simula escrita no corpo da resposta
            res.setContentType("application/json");
            res.getWriter().write("{\"ok\":true}");
            ((HttpServletResponse) res).setStatus(200);
        };

        // Executa o filtro
        filter.doFilter(request, response, chain);

        // Verifica que a resposta realmente contém o corpo escrito após
        // copyBodyToResponse
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        assertEquals("{\"ok\":true}", response.getContentAsString());
    }

    @Test
    void naoDeveQuebrarComRequestSemCorpoENemComEncodingNula() throws ServletException, IOException {
        HttpLoggingFilter filter = new HttpLoggingFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/sem-corpo");
        request.setCharacterEncoding(null); // força caminho de fallback

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> ((HttpServletResponse) res).setStatus(204);

        // Executa o filtro
        filter.doFilter(request, response, chain);

        assertEquals(204, response.getStatus());
        assertEquals("", response.getContentAsString());
    }
}
