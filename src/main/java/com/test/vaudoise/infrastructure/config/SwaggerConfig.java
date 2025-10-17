package com.test.vaudoise.infrastructure.config;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vaudoiseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vaudoise API")
                        .description("API for managing clients and contracts (Vaudoise project test)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mohamed Aymen Tlili")
                                .email("aymentli@gmail.com")
                                .url("https://github.com/Mohamed-Aymen-TLILI/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local environment")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Full project documentation")
                        .url("https://github.com/Mohamed-Aymen-TLILI/vaudoise"));
    }
}
