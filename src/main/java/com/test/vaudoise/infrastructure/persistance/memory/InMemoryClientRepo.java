package com.test.vaudoise.infrastructure.persistance.memory;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.Client;
import com.test.vaudoise.domain.model.Company;
import com.test.vaudoise.domain.model.CompanyIdentifier;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;

import java.util.Map;
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


}
