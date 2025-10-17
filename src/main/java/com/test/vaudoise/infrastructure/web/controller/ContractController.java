package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.contractusecases.CreateContractUseCase;
import com.test.vaudoise.application.contractusecases.FindContractsByClientUseCase;
import com.test.vaudoise.application.contractusecases.GetTotalActiveCostUseCase;
import com.test.vaudoise.application.contractusecases.UpdateContractCostUseCase;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.infrastructure.web.dto.contract.ContractResponse;
import com.test.vaudoise.infrastructure.web.dto.contract.CreateContractRequest;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractMapper;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractRequestMapper;
import com.test.vaudoise.infrastructure.web.mapper.contract.UpdateCostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Contracts", description = "Endpoints for managing insurance contracts")
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final CreateContractUseCase createContractUseCase;
    private final UpdateContractCostUseCase updateContractCostUseCase;
    private final FindContractsByClientUseCase findContractsByClientUseCase;
    private final GetTotalActiveCostUseCase getTotalActiveCostUseCase;

    public ContractController(CreateContractUseCase createContractUseCase, UpdateContractCostUseCase updateContractCostUseCase, FindContractsByClientUseCase findContractsByClientUseCase, GetTotalActiveCostUseCase getTotalActiveCostUseCase) {
        this.createContractUseCase = createContractUseCase;
        this.updateContractCostUseCase = updateContractCostUseCase;
        this.findContractsByClientUseCase = findContractsByClientUseCase;
        this.getTotalActiveCostUseCase = getTotalActiveCostUseCase;
    }

    @Operation(summary = "Create a new contract", description = "Creates a contract for a given client")
    @PostMapping("/create-new")
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody CreateContractRequest request) {
        Contract saved = createContractUseCase.execute(ContractRequestMapper.toDomain(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ContractMapper.toResponse(saved));
    }

    @Operation(summary = "Update the cost of an existing contract")
    @PutMapping("/{id}/cost")
    public ResponseEntity<ContractResponse> updateCost(
            @PathVariable UUID id,
            @RequestBody UpdateCostRequest request) {
        var updated = updateContractCostUseCase.execute(new ContractId(id), request.newCost());
        return ResponseEntity.ok(ContractMapper.toResponse(updated));
    }

    @Operation(summary = "Retrieve all contracts for a specific client")
    @GetMapping("/clients/{id}")
    public ResponseEntity<List<ContractResponse>> getContracts(
            @PathVariable UUID id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime updatedAfter) {

        var contracts = findContractsByClientUseCase.execute(new ClientId(id), updatedAfter);
        return ResponseEntity.ok(contracts.stream().map(ContractMapper::toResponse).toList());
    }

    @Operation(summary = "Get total active cost for a client")
    @GetMapping("/clients/{id}/contracts/total-active-cost")
    public ResponseEntity<BigDecimal> getTotalActiveCost(@PathVariable UUID id) {
        BigDecimal total = getTotalActiveCostUseCase.execute(new ClientId(id));
        return ResponseEntity.ok(total);
    }


}
