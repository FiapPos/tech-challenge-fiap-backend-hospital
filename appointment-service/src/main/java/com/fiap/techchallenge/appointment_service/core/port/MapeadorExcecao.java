package com.fiap.techchallenge.appointment_service.core.port;

import java.util.function.Supplier;

public interface MapeadorExcecao {
    <T> Supplier<T> envolver(Supplier<T> supplier);
}
