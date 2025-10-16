package com.test.vaudoise.client;

import com.test.vaudoise.application.usecase.UpdateClientUseCase;
import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateClientUseCaseTest {private ClientRepositoryPort repo;
    private UpdateClientUseCase usecase;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(ClientRepositoryPort.class);
        usecase = new UpdateClientUseCase(repo);
    }

    @Test
    void should_update_person_fields_successfully() {
        var id = new ClientId(UUID.randomUUID());
        var existing = new Person(id, new Name("Old Name"), new Email("old@mail.com"), new Phone("+41791234567"), LocalDate.of(1991,2,18));
        var updated = new Person(id, new Name("New Name"), new Email("new@mail.com"), new Phone("+41795551234"), LocalDate.of(1991,2,18));

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Client.class))).thenReturn(updated);

        var result = usecase.execute(id, "New Name", "new@mail.com", "+41795551234");

        assertThat(result).isInstanceOf(Person.class);
        assertThat(((Person) result).name().value()).isEqualTo("New Name");
        assertThat(((Person) result).email().value()).isEqualTo("new@mail.com");
        assertThat(((Person) result).phone().value()).isEqualTo("+41795551234");

        verify(repo).save(any(Client.class));
    }

    @Test
    void should_update_company_fields_successfully() {
        var id = new ClientId(UUID.randomUUID());
        var existing = new Company(id, new Name("Vaudoise"), new Email("old@vaudoise.ch"), new Phone("+41215551234"), new CompanyIdentifier("vaa-123"));
        var updated = new Company(id, new Name("Vaudoise New"), new Email("new@vaudoise.ch"), new Phone("+41216667777"), new CompanyIdentifier("vaa-123"));

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Client.class))).thenReturn(updated);

        var result = usecase.execute(id, "Vaudoise New", "new@vaudoise.ch", "+41216667777");

        assertThat(result).isInstanceOf(Company.class);
        assertThat(((Company) result).companyIdentifier().value()).isEqualTo("vaa-123");
        assertThat(((Company) result).email().value()).isEqualTo("new@vaudoise.ch");
        assertThat(((Company) result).phone().value()).isEqualTo("+41216667777");
    }

    @Test
    void should_throw_not_found_if_client_does_not_exist() {
        var id = new ClientId(UUID.randomUUID());
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.execute(id, "Name", "email@mail.com", "+41791234567"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Client not found");

        verify(repo, never()).save(any());
    }

    @Test
    void should_not_save_if_no_change() {
        var id = new ClientId(UUID.randomUUID());
        var existing = new Person(id, new Name("Mohamed Aymen TLILI"), new Email("matlili@mail.com"), new Phone("+41791234567"), LocalDate.of(1991,2,18));

        when(repo.findById(id)).thenReturn(Optional.of(existing));

        var result = usecase.execute(id, "Mohamed Aymen TLILI", "matlili@mail.com", "+41791234567");

        assertThat(result).isSameAs(existing);
        verify(repo, never()).save(any());
    }
}
