package com.test.vaudoise.contract;

import com.test.vaudoise.application.contractusecases.FindContractsByClientUseCase;
import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.client.*;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FindContractsByClientUseCaseTest {

    private ClientRepositoryPort clientRepo;
    private ContractRepositoryPort contractRepo;
    private FindContractsByClientUseCase usecase;

    @BeforeEach
    void setup() {
        clientRepo = Mockito.mock(ClientRepositoryPort.class);
        contractRepo = Mockito.mock(ContractRepositoryPort.class);
        usecase = new FindContractsByClientUseCase(clientRepo, contractRepo);
    }

    @Test
    void should_return_active_contracts_for_client() {
        var clientId = new ClientId(UUID.randomUUID());
        var client = new Person(clientId, new Name("Aymen"), new Email("matlili@mail.com"), new Phone("+41790000000"), LocalDate.of(1990, 1, 1));

        var c1 = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(100));
        var c2 = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(200));

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepo.findActiveByClientId(clientId)).thenReturn(List.of(c1, c2));

        var result = usecase.execute(clientId, null);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Contract::getCostAmount)
                .containsExactlyInAnyOrder(BigDecimal.valueOf(100), BigDecimal.valueOf(200));
    }

    @Test
    void filters_active_contracts_by_updatedAfter() {
        var clientId = new ClientId(UUID.randomUUID());
        var client = new Person(clientId, new Name("Aymen"), new Email("a@mail.com"), new Phone("+41790000000"),
                LocalDate.of(1990, 1, 1));

        var c = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(200));
        c.updateCost(BigDecimal.valueOf(250));

        var after = c.getLastUpdateDate().minusSeconds(1);

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepo.findByClientIdAndUpdatedAfter(clientId, after)).thenReturn(List.of(c));

        var result = usecase.execute(clientId, after);

        assertThat(result).extracting(Contract::getCostAmount).containsExactly(BigDecimal.valueOf(250));
        verify(contractRepo).findByClientIdAndUpdatedAfter(clientId, after);
        verify(contractRepo, never()).findActiveByClientId(any());
    }


    @Test
    void should_filter_contracts_by_update_date() {
        var clientId = new ClientId(UUID.randomUUID());
        var client = new Person(clientId, new Name("Aymen"), new Email("aymen@mail.com"), new Phone("+41790000000"), LocalDate.of(1990, 1, 1));

        var c1 = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(100));
        var c2 = new Contract(new ContractId(UUID.randomUUID()), clientId, LocalDate.now(), null, BigDecimal.valueOf(200));
        c2.updateCost(BigDecimal.valueOf(250));

        when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepo.findByClientIdAndUpdatedAfter(clientId, c2.getLastUpdateDate().minusSeconds(1)))
                .thenReturn(List.of(c2));

        var result = usecase.execute(clientId, c2.getLastUpdateDate().minusSeconds(1));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCostAmount()).isEqualTo(BigDecimal.valueOf(250));
    }

    @Test
    void should_throw_if_client_not_found() {
        var clientId = new ClientId(UUID.randomUUID());
        when(clientRepo.findById(clientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.execute(clientId, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Client not found");
    }
}
