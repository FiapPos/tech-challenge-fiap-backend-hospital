package com.fiap.techchallenge.appointment_service.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class CabecalhosEncaminhamento {
    public HttpHeaders montar(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        Object profileAttr = request.getAttribute("X-User-Profile");
        if (profileAttr != null) {
            headers.add("X-User-Profile", profileAttr.toString());
        }

        Object usernameAttr = request.getAttribute("X-Username");
        if (usernameAttr != null) {
            headers.add("X-Username", usernameAttr.toString());
        }

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && !auth.isBlank()) {
            headers.add(HttpHeaders.AUTHORIZATION, auth);
        }

        return headers;
    }
}
