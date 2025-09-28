package com.fiap.techchallenge.usuario_service.core.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompartilhadoService {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
