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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    CreateClientUseCase createClientUseCase(ClientRepositoryPort clientRepository) {
        return new CreateClientUseCase(clientRepository);
    }

    @Bean
    ReadClientUseCase readClientUseCase(ClientRepositoryPort clientRepository) {
        return new ReadClientUseCase(clientRepository);
    }

    @Bean
    UpdateClientUseCase updateClientUseCase(ClientRepositoryPort clientRepository) {
        return new UpdateClientUseCase(clientRepository);
    }

    @Bean
    DeleteClientUseCase deleteClientUseCase(ClientRepositoryPort clientRepository, ContractRepositoryPort contractRepositoryPort) {
        return new DeleteClientUseCase(clientRepository, contractRepositoryPort);
    }

    @Bean
    CreateContractUseCase createContractUseCase(ContractRepositoryPort contractRepositoryPort, ClientRepositoryPort clientRepository) {
        return new CreateContractUseCase(contractRepositoryPort, clientRepository);
    }

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