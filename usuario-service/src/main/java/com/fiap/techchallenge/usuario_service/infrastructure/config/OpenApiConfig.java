package com.fiap.techchallenge.usuario_service.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Hospital API", version = "v1", description = "API responsável por autenticação, autorização e cadastro de usuários.", contact = @Contact(name = "Equipe Hospital", email = "javafiappos@gmail.com"), license = @License(name = "Apache 2.0", url = "http://springdoc.org")))
public class OpenApiConfig {
}
