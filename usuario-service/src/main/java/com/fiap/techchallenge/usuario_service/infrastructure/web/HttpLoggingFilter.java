package com.fiap.techchallenge.usuario_service.infrastructure.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);
    private static final int MAX_PAYLOAD = 2048;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        // Wrap to cache request/response bodies safely
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            int status = wrappedResponse.getStatus();

            String requestBody = toBodyString(wrappedRequest.getContentAsByteArray(), request.getCharacterEncoding());
            String responseBody = toBodyString(wrappedResponse.getContentAsByteArray(),
                    response.getCharacterEncoding());

            if (log.isInfoEnabled()) {
                log.info("{} {}{} status={} time={}ms req={} resp={}",
                        method,
                        uri,
                        query != null ? ("?" + query) : "",
                        status,
                        duration,
                        truncate(requestBody),
                        truncate(responseBody));
            }

            // Important: copy cached body back to the real response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String toBodyString(byte[] content, String encoding) {
        if (content == null || content.length == 0)
            return "";
        Charset charset;
        try {
            charset = encoding != null ? Charset.forName(encoding) : StandardCharsets.UTF_8;
        } catch (Exception e) {
            charset = StandardCharsets.UTF_8;
        }
        String text = new String(content, charset).replaceAll("\n|\r", "");
        return text;
    }

    private String truncate(String text) {
        if (text == null)
            return "";
        if (text.length() <= MAX_PAYLOAD)
            return text;
        return text.substring(0, MAX_PAYLOAD) + "...";
    }
}
