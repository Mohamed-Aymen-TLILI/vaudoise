package com.test.vaudoise.infrastructure.persistance.jpa.repo;

import com.test.vaudoise.infrastructure.persistance.jpa.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaClientSpringRepo extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByCompanyIdentifier(String companyIdentifier);
}
