package com.test.vaudoise.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.vaudoise.application.contractusecases.CreateContractUseCase;
import com.test.vaudoise.application.contractusecases.UpdateContractCostUseCase;
import com.test.vaudoise.core.exception.NotFoundException;
import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.client.ClientId;
import com.test.vaudoise.domain.model.contrat.Contract;
import com.test.vaudoise.domain.model.contrat.ContractId;
import com.test.vaudoise.infrastructure.web.controller.ContractController;
import com.test.vaudoise.infrastructure.web.dto.contract.CreateContractRequest;
import com.test.vaudoise.infrastructure.web.mapper.contract.UpdateCostRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContractController.class)
public class ContractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateContractUseCase createContractUseCase;

    @MockitoBean
    private UpdateContractCostUseCase updateContractCostUseCase;

    @Test
    void should_create_contract_and_return_201() throws Exception {
        var contract = new Contract(
                new ContractId(UUID.randomUUID()),
                new ClientId(UUID.randomUUID()),
                LocalDate.now(),
                null,
                BigDecimal.valueOf(1000)
        );

        Mockito.when(createContractUseCase.execute(any())).thenReturn(contract);

        var req = new CreateContractRequest(
                contract.getClientId().value(),
                LocalDate.now(),
                null,
                BigDecimal.valueOf(1000)
        );

        mvc.perform(post("/api/contracts/create-new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.costAmount").value(1000));
    }

    @Test
    void should_return_404_when_client_not_found() throws Exception {
        var req = new CreateContractRequest(
                UUID.randomUUID(),
                LocalDate.now(),
                null,
                BigDecimal.valueOf(2000)
        );

        Mockito.when(createContractUseCase.execute(any()))
                .thenThrow(new NotFoundException("Client not found"));

        mvc.perform(post("/api/contracts/create-new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Client not found"));
    }

    @Test
    void should_return_400_when_cost_negative() throws Exception {
        var req = new CreateContractRequest(
                UUID.randomUUID(),
                LocalDate.now(),
                null,
                BigDecimal.valueOf(-5)
        );

        mvc.perform(post("/api/contracts/create-new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_missing_client_id() throws Exception {
        var req = new CreateContractRequest(
                null,
                LocalDate.now(),
                null,
                BigDecimal.valueOf(500)
        );

        mvc.perform(post("/api/contracts/create-new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    void should_update_contract_cost_and_return_200() throws Exception {
        var id = UUID.randomUUID();
        var contract = new Contract(
                new ContractId(id),
                new ClientId(UUID.randomUUID()),
                LocalDate.now(),
                null,
                BigDecimal.valueOf(500)
        );

        Mockito.when(updateContractCostUseCase.execute(any(), any())).thenReturn(contract);

        var req = new UpdateCostRequest(BigDecimal.valueOf(500));

        mvc.perform(put("/api/contracts/" + id + "/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.costAmount").value(500));
    }

    @Test
    void should_return_404_when_contract_not_found() throws Exception {
        var id = UUID.randomUUID();
        var req = new UpdateCostRequest(BigDecimal.valueOf(700));

        Mockito.when(updateContractCostUseCase.execute(any(), any()))
                .thenThrow(new NotFoundException("Contract not found"));

        mvc.perform(put("/api/contracts/" + id + "/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Contract not found"));
    }

    @Test
    void should_return_400_when_cost_is_negative() throws Exception {
        var id = UUID.randomUUID();
        var req = new UpdateCostRequest(BigDecimal.valueOf(-500));

        Mockito.when(updateContractCostUseCase.execute(any(), any()))
                .thenThrow(new ValidationException("Cost amount must be positive"));

        mvc.perform(put("/api/contracts/" + id + "/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Cost amount must be positive"));
    }

    @Test
    void should_update_contract_cost_and_refresh_update_date() throws Exception {
        var id = UUID.randomUUID();
        var contract = new Contract(
                new ContractId(id),
                new ClientId(UUID.randomUUID()),
                LocalDate.now(),
                null,
                BigDecimal.valueOf(100)
        );

        contract.updateCost(BigDecimal.valueOf(200));

        Mockito.when(updateContractCostUseCase.execute(any(), any())).thenReturn(contract);

        var req = new UpdateCostRequest(BigDecimal.valueOf(200));

        // when
        mvc.perform(put("/api/contracts/" + id + "/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.costAmount").value(200));

        ArgumentCaptor<ContractId> idCaptor = ArgumentCaptor.forClass(ContractId.class);
        ArgumentCaptor<BigDecimal> costCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        Mockito.verify(updateContractCostUseCase).execute(idCaptor.capture(), costCaptor.capture());

        assertThat(costCaptor.getValue()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(contract.getLastUpdateDate())
                .isNotNull()
                .isBeforeOrEqualTo(LocalDateTime.now());
    }
}
