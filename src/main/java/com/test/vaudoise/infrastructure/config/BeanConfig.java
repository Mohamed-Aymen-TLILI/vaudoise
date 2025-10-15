package com.test.vaudoise.infrastructure.config;

import com.test.vaudoise.application.usecase.CreateClientUseCase;
import com.test.vaudoise.application.usecase.ReadClientUseCase;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.infrastructure.persistance.memory.InMemoryClientRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    ClientRepositoryPort clientRepository() {
        return new InMemoryClientRepo();
    }


    @Bean
    CreateClientUseCase createClientUseCase(ClientRepositoryPort repo) {
        return new CreateClientUseCase(repo);
    }

    @Bean
    ReadClientUseCase readClientUseCase(ClientRepositoryPort repo) {
        return new ReadClientUseCase(repo);
    }
}
