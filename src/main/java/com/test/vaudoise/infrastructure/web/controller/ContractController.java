package com.test.vaudoise.infrastructure.web.controller;

import com.test.vaudoise.application.contractusecases.CreateContractUseCase;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.infrastructure.web.dto.contract.ContractResponse;
import com.test.vaudoise.infrastructure.web.dto.contract.CreateContractRequest;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractMapper;
import com.test.vaudoise.infrastructure.web.mapper.contract.ContractRequestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final CreateContractUseCase createContractUseCase;

    public ContractController(CreateContractUseCase createContractUseCase) {
        this.createContractUseCase = createContractUseCase;
    }

    @PostMapping("/create-new")
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody CreateContractRequest request) {
        Contract saved = createContractUseCase.execute(ContractRequestMapper.toDomain(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ContractMapper.toResponse(saved));
    }
}
