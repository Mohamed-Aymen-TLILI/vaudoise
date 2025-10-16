package com.test.vaudoise.infrastructure.config;

import com.test.vaudoise.application.clientusecases.CreateClientUseCase;
import com.test.vaudoise.application.clientusecases.DeleteClientUseCase;
import com.test.vaudoise.application.clientusecases.ReadClientUseCase;
import com.test.vaudoise.application.clientusecases.UpdateClientUseCase;
import com.test.vaudoise.application.contractusecases.CreateContractUseCase;
import com.test.vaudoise.application.contractusecases.FindContractsByClientUseCase;
import com.test.vaudoise.application.contractusecases.GetTotalActiveCostUseCase;
import com.test.vaudoise.application.contractusecases.UpdateContractCostUseCase;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import com.test.vaudoise.infrastructure.persistance.memory.InMemoryClientRepo;
import com.test.vaudoise.infrastructure.persistance.memory.InMemoryContractRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    ClientRepositoryPort clientRepository() {
        return new InMemoryClientRepo();
    }

    @Bean
    ContractRepositoryPort contractRepositoryPort() {
        return new InMemoryContractRepo();
    }

    @Bean
    CreateClientUseCase createClientUseCase(ClientRepositoryPort repo) {
        return new CreateClientUseCase(repo);
    }

    @Bean
    ReadClientUseCase readClientUseCase(ClientRepositoryPort repo) {
        return new ReadClientUseCase(repo);
    }

    @Bean
    UpdateClientUseCase updateClientUseCase(ClientRepositoryPort repo) {
        return new UpdateClientUseCase(repo);
    }

    @Bean
    CreateContractUseCase createContractUseCase(ContractRepositoryPort contractRepositoryPort, ClientRepositoryPort clientRepository) {
        return new CreateContractUseCase(contractRepositoryPort, clientRepository);
    }

    @Bean
    DeleteClientUseCase deleteClientUseCase(ClientRepositoryPort clientRepository, ContractRepositoryPort contractRepositoryPort ) {
        return new DeleteClientUseCase(clientRepository, contractRepositoryPort);
    };

    @Bean
    UpdateContractCostUseCase updateContractCostUseCase(ContractRepositoryPort contractRepositoryPort) {
        return new UpdateContractCostUseCase(contractRepositoryPort);
    }

    @Bean
    FindContractsByClientUseCase findContractsByClientUseCase(ContractRepositoryPort contractRepositoryPort, ClientRepositoryPort clientRepository) {
        return new FindContractsByClientUseCase(clientRepository, contractRepositoryPort);
    }

    @Bean
    GetTotalActiveCostUseCase getTotalActiveCostUseCase(ContractRepositoryPort contractRepositoryPort) {
        return new GetTotalActiveCostUseCase(contractRepositoryPort);
    }
}
