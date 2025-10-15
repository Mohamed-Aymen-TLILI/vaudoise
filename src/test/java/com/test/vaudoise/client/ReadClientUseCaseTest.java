package com.test.vaudoise.client;

import com.test.vaudoise.application.usecase.ReadClientUseCase;
import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ReadClientUseCaseTest {

    private final ClientRepositoryPort repo = Mockito.mock(ClientRepositoryPort.class);
    private final ReadClientUseCase usecase = new ReadClientUseCase(repo);

    @Test
    void should_return_client_when_exists() {
        var id = new ClientId(UUID.randomUUID());
        var client = new Person(id, new Name("Mohamed Aymen TLILI"), new Email("matlili@example.com"), new Phone("+41791234567"), LocalDate.of(1990, 1, 1));
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(client));

        var result = usecase.execute(id);
        assertThat(result).isEqualTo(client);
    }

    @Test
    void should_throw_when_client_not_found() {
        var id = new ClientId(UUID.randomUUID());
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.execute(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Client not found");
    }
}
