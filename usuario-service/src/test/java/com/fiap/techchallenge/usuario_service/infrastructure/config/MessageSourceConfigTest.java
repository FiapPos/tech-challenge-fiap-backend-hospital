package com.fiap.techchallenge.usuario_service.infrastructure.config;

import com.fiap.techchallenge.usuario_service.infrastructure.config.MessageSourceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class MessageSourceConfigTest {
    private final MessageSourceConfig messageSourceConfig = new MessageSourceConfig();

    @Test
    void deveCriarMessageSourceComConfiguracaoCorreta() {
        MessageSource messageSource = messageSourceConfig.messageSource();

        assertNotNull(messageSource);
        assertInstanceOf(ResourceBundleMessageSource.class, messageSource);
    }

    @Test
    void deveCriarValidatorComMessageSource() {
        LocalValidatorFactoryBean validator = messageSourceConfig.getValidator();

        assertNotNull(validator);
    }
}