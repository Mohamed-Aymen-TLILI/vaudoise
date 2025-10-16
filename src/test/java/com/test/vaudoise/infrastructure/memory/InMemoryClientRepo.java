package com.test.vaudoise.infrastructure.memory;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.Client;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.client.Company;
import com.test.vaudoise.domain.model.client.CompanyIdentifier;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryClientRepo implements ClientRepositoryPort {

        private final Map<UUID, Client> byId = new ConcurrentHashMap<>();
        private final Map<String, UUID> byCompanyId = new ConcurrentHashMap<>();

        @Override
        public Client save(Client c) {
            if (c instanceof Company co) {
                if (byCompanyId.containsKey(co.companyIdentifier().value())) {
                    throw new ValidationException("companyIdentifier already exists");
                }
                byCompanyId.put(co.companyIdentifier().value(), c.id().value());
            }
            byId.put(c.id().value(), c);
            return c;
        }

        @Override
        public boolean existsCompanyIdentifier(CompanyIdentifier companyId) {
            return byCompanyId.containsKey(companyId.value());
        }

        @Override
        public Optional<Client> findById(ClientId id) {
            return Optional.ofNullable(byId.get(id.value()));
        }

    @Override
    public Optional<Client> delete(ClientId id) {
        Client removed = byId.remove(id.value());
        if (removed instanceof Company co) {
            byCompanyId.remove(co.companyIdentifier().value());
        }
        return Optional.ofNullable(removed);
    }


}
