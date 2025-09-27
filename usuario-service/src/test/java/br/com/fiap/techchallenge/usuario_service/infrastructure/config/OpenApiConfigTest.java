package br.com.fiap.techchallenge.usuario_service.infrastructure.config;

import br.com.fiap.techchallenge.infrastructure.config.OpenApiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void deveInstanciarOpenApiConfig() {
        OpenApiConfig openApiConfig = new OpenApiConfig();
        assertNotNull(openApiConfig);
    }

    @Test
    void deveSerConfiguracaoSpring() {
        OpenApiConfig openApiConfig = new OpenApiConfig();
        Configuration annotation = openApiConfig.getClass().getAnnotation(Configuration.class);
        assertNotNull(annotation);
    }

    @Test
    void deveSerCarregadoPeloSpringContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(OpenApiConfig.class);
        context.refresh();

        OpenApiConfig openApiConfig = context.getBean(OpenApiConfig.class);
        assertNotNull(openApiConfig);

        context.close();
    }

    @Test
    void deveTerAnotacaoOpenAPIDefinition() {
        OpenApiConfig openApiConfig = new OpenApiConfig();
        io.swagger.v3.oas.annotations.OpenAPIDefinition annotation = openApiConfig.getClass()
                .getAnnotation(io.swagger.v3.oas.annotations.OpenAPIDefinition.class);
        assertNotNull(annotation);
    }
}