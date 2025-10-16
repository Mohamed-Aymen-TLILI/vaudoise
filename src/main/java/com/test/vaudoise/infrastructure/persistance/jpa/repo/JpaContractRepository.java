package com.test.vaudoise.infrastructure.persistance.jpa.repo;

import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.domain.ports.ContractRepositoryPort;
import com.test.vaudoise.infrastructure.persistance.jpa.mapper.ContractEntityMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaContractRepository implements ContractRepositoryPort {

    private final JpaContractSpringRepo repo;

    public JpaContractRepository(JpaContractSpringRepo repo) {
        this.repo = repo;
    }

    @Override
    public Contract save(Contract contract) {
        var entity = ContractEntityMapper.toEntity(contract);
        var saved = repo.save(entity);
        return ContractEntityMapper.toDomain(saved);
    }

    @Override
    public List<Contract> findActiveByClientId(ClientId clientId) {
        return repo.findActiveByClientId(clientId.value()).stream()
                .map(ContractEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Contract> findById(ContractId id) {
        return repo.findById(id.value()).map(ContractEntityMapper::toDomain);
    }

    @Override
    public List<Contract> findByClientIdAndUpdatedAfter(ClientId clientId, LocalDateTime updatedAfter) {
        return repo.findByClientIdAndUpdatedAfter(clientId.value(), updatedAfter).stream()
                .map(ContractEntityMapper::toDomain)
                .toList();
    }
}
