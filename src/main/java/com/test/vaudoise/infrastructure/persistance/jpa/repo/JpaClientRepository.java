package com.test.vaudoise.infrastructure.persistance.jpa.repo;

import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.Client;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.client.Company;
import com.test.vaudoise.domain.model.client.CompanyIdentifier;
import com.test.vaudoise.domain.ports.ClientRepositoryPort;
import com.test.vaudoise.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaClientRepository implements ClientRepositoryPort {

    private final JpaClientSpringRepo repo;

    public JpaClientRepository(JpaClientSpringRepo repo) {
        this.repo = repo;
    }

    @Override
    public Client save(Client client) {
        if (client instanceof Company co) {
            var found = repo.findByCompanyIdentifier(co.companyIdentifier().value());
            if (found.isPresent()) {
                var foundId = found.get().getId();
                if (!foundId.equals(co.id().value())) {
                    throw new ValidationException("companyIdentifier already exists");
                }
            }
        }
        var entity = ClientEntityMapper.toEntity(client);
        var saved = repo.save(entity);
        return ClientEntityMapper.toDomain(saved);
    }

    @Override
    public boolean existsCompanyIdentifier(CompanyIdentifier id) {
        return repo.findByCompanyIdentifier(id.value()).isPresent();
    }

    @Override
    public Optional<Client> findById(ClientId id) {
        return repo.findById(id.value()).map(ClientEntityMapper::toDomain);
    }

    @Override
    public Optional<Client> delete(ClientId id) {
        var clientOpt = repo.findById(id.value());
        clientOpt.ifPresent(c -> repo.deleteById(id.value()));
        return clientOpt.map(ClientEntityMapper::toDomain);
    }
}
