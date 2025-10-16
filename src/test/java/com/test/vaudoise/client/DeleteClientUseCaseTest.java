package com.test.vaudoise.client;

import com.test.vaudoise.application.clientusecases.DeleteClientUseCase;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class DeleteClientUseCaseTest {

    private ClientRepositoryPort clientRepo;
    private ContractRepositoryPort contractRepo;
    private DeleteClientUseCase useCase;

    @BeforeEach
    void setup() {
        clientRepo = mock(ClientRepositoryPort.class);
        contractRepo = mock(ContractRepositoryPort.class);
        useCase = new DeleteClientUseCase(clientRepo, contractRepo);
    }

    @Test
    void should_delete_client_and_close_active_contracts() {
        var clientId = new ClientId(UUID.randomUUID());
        var client = new Person(clientId, new Name("Aymen"), new Email("aymen@example.com"), new Phone("+41790000000"), LocalDate.of(1990, 1, 1));

        var contract = spy(new Contract(
                new ContractId(UUID.randomUUID()),
                clientId,
                LocalDate.now(),
                null,
                BigDecimal.valueOf(100)
        ));
        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepo.delete(clientId)).thenReturn(Optional.of(client));
        when(contractRepo.findActiveByClientId(clientId)).thenReturn(List.of(contract));

        useCase.execute(clientId);

        verify(contractRepo).save(contract);
        verify(clientRepo).delete(clientId);
    }

    @Test
    void should_throw_when_client_not_found() {
        var clientId = new ClientId(UUID.randomUUID());
        when(clientRepo.delete(clientId)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(clientId));

        verify(contractRepo, never()).findActiveByClientId(any());
    }
}
