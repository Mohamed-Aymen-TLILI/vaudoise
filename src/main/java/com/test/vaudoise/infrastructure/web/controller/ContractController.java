package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.contractusecases.CreateContractUseCase;
import com.test.vaudoise.application.contractusecases.UpdateContractCostUseCase;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.infrastructure.web.dto.contract.ContractResponse;
import com.test.vaudoise.infrastructure.web.dto.contract.CreateContractRequest;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractMapper;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractRequestMapper;
import com.test.vaudoise.infrastructure.web.mapper.contract.UpdateCostRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final CreateContractUseCase createContractUseCase;
    private final UpdateContractCostUseCase updateContractCostUseCase;

    public ContractController(CreateContractUseCase createContractUseCase, UpdateContractCostUseCase updateContractCostUseCase) {
        this.createContractUseCase = createContractUseCase;
        this.updateContractCostUseCase = updateContractCostUseCase;
    }

    @PostMapping("/create-new")
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody CreateContractRequest request) {
        Contract saved = createContractUseCase.execute(ContractRequestMapper.toDomain(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ContractMapper.toResponse(saved));
    }

    @PutMapping("/{id}/cost")
    public ResponseEntity<ContractResponse> updateCost(
            @PathVariable UUID id,
            @RequestBody UpdateCostRequest request) {
        var updated = updateContractCostUseCase.execute(new ContractId(id), request.newCost());
        return ResponseEntity.ok(ContractMapper.toResponse(updated));
    }
}
