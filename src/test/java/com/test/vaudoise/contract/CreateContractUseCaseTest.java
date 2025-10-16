package com.test.vaudoise.contract;

import com.test.vaudoise.application.contractusecases.CreateContractUseCase;
import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.client.*;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreateContractUseCaseTest {

    private ContractRepositoryPort contractRepo;
    private ClientRepositoryPort clientRepo;
    private CreateContractUseCase useCase;

    @BeforeEach
    void setup() {
        contractRepo = mock(ContractRepositoryPort.class);
        clientRepo = mock(ClientRepositoryPort.class);
        useCase = new CreateContractUseCase(contractRepo, clientRepo);
    }

    @Test
    void should_create_contract_when_client_exists() {
        var clientId = new ClientId(UUID.randomUUID());
        var contract = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.TEN);

        var existingClient = new Person(
                clientId,
                new Name("Mohamed Aymen TLILI"),
                new Email("matlili@example.com"),
                new Phone("+41790000000"),
                LocalDate.of(1991, 2, 18)
        );
        when(clientRepo.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(contractRepo.save(contract)).thenReturn(contract);

        var result = useCase.execute(contract);

        assertEquals(contract, result);
        verify(contractRepo).save(contract);
    }

    @Test
    void should_create_contract_when_company_exists() {
        var clientId = new ClientId(UUID.randomUUID());
        var contract = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.TEN);

        var company = new Company(
                new ClientId(UUID.randomUUID()),
                new Name("Vaudoise Assurances"),
                new Email("contact@vaudoise.ch"),
                new Phone("+41761234567"),
                new CompanyIdentifier("vaa-123")
        );
        when(clientRepo.findById(clientId)).thenReturn(Optional.of(company));
        when(contractRepo.save(contract)).thenReturn(contract);

        var result = useCase.execute(contract);

        assertEquals(contract, result);
        verify(contractRepo).save(contract);
    }

    @Test
    void should_throw_when_client_does_not_exist() {
        var clientId = new ClientId(UUID.randomUUID());
        var contract = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.TEN);

        when(clientRepo.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(contract));
        verify(contractRepo, never()).save(any());
    }
}
