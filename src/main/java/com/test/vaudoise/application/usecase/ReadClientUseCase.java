package com.test.vaudoise.application.usecase;

import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.domain.model.Client;
import com.test.vaudoise.domain.model.ClientId;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;

import java.util.Optional;

public class ReadClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public ReadClientUseCase(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    public Client execute(ClientId id) {
        Optional<Client> client = clientRepositoryPort.findById(id);
        return client.orElseThrow(() -> new NotFoundException("Client not found"));
    }
}
