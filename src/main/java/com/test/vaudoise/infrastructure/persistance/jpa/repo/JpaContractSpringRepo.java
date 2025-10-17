package com.test.vaudoise.infrastructure.persistance.jpa.repo;

import com.test.vaudoise.infrastructure.persistance.jpa.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaContractSpringRepo extends JpaRepository<ContractEntity, UUID> {
    @Query("""
        SELECT c FROM ContractEntity c
        WHERE c.clientId = :clientId
        AND (c.endDate IS NULL OR c.endDate > CURRENT_DATE)
    """)
    List<ContractEntity> findActiveByClientId(@Param("clientId") UUID clientId);

    @Query("""
        SELECT c FROM ContractEntity c
        WHERE c.clientId = :clientId
          AND (c.endDate IS NULL OR c.endDate > CURRENT_DATE)
          AND c.lastUpdateDate > :updatedAfter
    """)
    List<ContractEntity> findByClientIdAndUpdatedAfter(
            @Param("clientId") UUID clientId,
            @Param("updatedAfter") LocalDateTime updatedAfter
    );
}
