package com.test.vaudoise.application.clientusecases;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.client.*;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;

public class UpdateClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public UpdateClientUseCase(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    public Client execute(ClientId id, String name, String email, String phone) {
        Client existing = clientRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Client updated = existing.update(
                new Name(name),
                new Email(email),
                new Phone(phone)
        );
        if (existing.equals(updated)) return existing;

        return clientRepositoryPort.save(updated);
    }
}
