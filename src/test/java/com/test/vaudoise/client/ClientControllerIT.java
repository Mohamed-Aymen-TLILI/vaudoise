package com.test.vaudoise.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.vaudoise.application.usecase.CreateClientUseCase;
import com.test.vaudoise.application.usecase.ReadClientUseCase;
import com.test.vaudoise.core.exception.ValidationException;
import com.test.vaudoise.domain.model.*;
import com.test.vaudoise.infrastructure.persistance.memory.InMemoryClientRepo;
import com.test.vaudoise.infrastructure.web.controller.ClientController;
import com.test.vaudoise.infrastructure.web.dto.CreateCompanyRequest;
import com.test.vaudoise.infrastructure.web.dto.CreatePersonRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerIT {

    private final InMemoryClientRepo repo = new InMemoryClientRepo();
    private final CreateClientUseCase usecase = new CreateClientUseCase(repo);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateClientUseCase createClientUseCase;

    @MockitoBean
    private ReadClientUseCase readClientUseCase;

    @Test
    void should_create_person_and_return_201() throws Exception {
        var person = new Person(
                new ClientId(UUID.randomUUID()),
                new Name("Mohamed Aymen TLILI"),
                new Email("matlili@example.com"),
                new Phone("+41791234567"),
                LocalDate.of(1990, 1, 1)
        );

        Mockito.when(createClientUseCase.execute(Mockito.any(Person.class))).thenReturn(person);

        var request = new CreatePersonRequest("Mohamed Aymen TLILI", "matlili@example.com", "+41791234567", LocalDate.of(1990, 1, 1));

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.name").value("Mohamed Aymen TLILI"))
                .andExpect((ResultMatcher) jsonPath("$.type").value("PERSON"))
                .andExpect((ResultMatcher) jsonPath("$.email").value("matlili@example.com"));
    }

    @Test
    void should_create_company_and_return_201() throws Exception {
        var company = new Company(
                new ClientId(UUID.randomUUID()),
                new Name("Vaudoise Assurances"),
                new Email("contact@vaudoise.ch"),
                new Phone("+41215555555"),
                new CompanyIdentifier("vaa-123")
        );

        Mockito.when(createClientUseCase.execute(Mockito.any(Company.class))).thenReturn(company);

        var request = new CreateCompanyRequest(
                "Vaudoise Assurances",
                "contact@vaudoise.ch",
                "+41215555555",
                "vaa-123"
        );

        mvc.perform(post("/api/clients/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("COMPANY"))
                .andExpect(jsonPath("$.name").value("Vaudoise Assurances"))
                .andExpect(jsonPath("$.email").value("contact@vaudoise.ch"))
                .andExpect(jsonPath("$.companyIdentifier").value("vaa-123"));
    }


    @Test
    void should_return_400_when_company_identifier_exists() throws Exception {
        var request = new CreateCompanyRequest(
                "Vaudoise Assurances",
                "contact@vaudoise.ch",
                "+41215555555",
                "vaa-123"
        );

        Mockito.when(createClientUseCase.execute(Mockito.any(Company.class)))
                .thenThrow(new ValidationException("companyIdentifier already exists"));

        mvc.perform(post("/api/clients/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("companyIdentifier already exists"));
    }


    @Test
    void should_return_400_when_email_invalid_for_person() throws Exception {
        var request = new CreatePersonRequest(
                "Mohamed Aymen TLILI",
                "invalid-email",
                "+41791234567",
                LocalDate.of(1990, 1, 1)
        );

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Invalid email address"));
    }

    @Test
    void should_return_400_when_phone_invalid_for_person() throws Exception {
        var request = new CreatePersonRequest(
                "Mohamed Aymen TLILI",
                "matlili@example.com",
                "INVALID_PHONE",
                LocalDate.of(1990, 1, 1)
        );

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("invalid phone"));
    }

    @Test
    void should_return_400_when_name_blank_for_person() throws Exception {
        var request = new CreatePersonRequest(
                "  ",
                "matlili@example.com",
                "+41791234567",
                LocalDate.of(1990, 1, 1)
        );

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    void should_return_400_when_name_not_valid_for_person() throws Exception {
        var request = new CreatePersonRequest(
                "a",
                "matlili@example.com",
                "+41791234567",
                LocalDate.of(1990, 1, 1)
        );

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("name length must be between 2 and 200 characters"));
    }

    @Test
    void should_return_400_when_company_identifier_missing() throws Exception {
        var request = new CreateCompanyRequest(
                "Vaudoise Assurances",
                "contact@vaudoise.ch",
                "+41215555555",
                null
        );

        mvc.perform(post("/api/clients/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid companyIdentifier"));
    }

    @Test
    void should_return_detailed_validation_errors() throws Exception {
        var request = new CreatePersonRequest(
                "",
                "",
                "",
                null
        );

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.details[?(@.field == 'birthdate')].message").value("Birthdate is required"));
    }

    @Test
    void should_throw_when_company_identifier_already_exists() {
        var existing = new Company(
                new ClientId(UUID.randomUUID()),
                new Name("Vaudoise"),
                new Email("contact@vaudoise.ch"),
                new Phone("+41215555555"),
                new CompanyIdentifier("vaa-123")
        );
        repo.save(existing);

        var duplicate = new Company(
                new ClientId(UUID.randomUUID()),
                new Name("Vaudoise 2"),
                new Email("contact2@vaudoise.ch"),
                new Phone("+41216666666"),
                new CompanyIdentifier("vaa-123")
        );

        assertThatThrownBy(() -> usecase.execute(duplicate))
                .isInstanceOf(ValidationException.class)
                .hasMessage("companyIdentifier already exists");
    }


    @Test
    void should_return_400_when_birthdate_not_iso_format() throws Exception {
        var invalidJson = """
        {
          "name": "Mohamed Aymen TLILI",
          "email": "matili@example.com",
          "phone": "+41791234567",
          "birthdate": "01.01.1990"
        }
        """;

        mvc.perform(post("/api/clients/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid date format, expected ISO 8601 (yyyy-MM-dd)"));
    }


    @Test
    void should_return_person_when_exists() throws Exception {
        var id = UUID.randomUUID();
        var person = new Person(
                new ClientId(id),
                new Name("Mohamed Aymen TLILI"),
                new Email("matili@example.com"),
                new Phone("+41791234567"),
                LocalDate.of(1990, 1, 1)
        );
        Mockito.when(readClientUseCase.execute(new ClientId(id))).thenReturn(person);

        mvc.perform(get("/api/clients/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Mohamed Aymen TLILI"))
                .andExpect(jsonPath("$.email").value("matili@example.com"))
                .andExpect(jsonPath("$.phone").value("+41791234567"))
                .andExpect(jsonPath("$.birthdate").value("1990-01-01"))
                .andExpect(jsonPath("$.type").value("PERSON"));

    }

    @Test
    void should_return_400_when_client_not_found() throws Exception {
        var id = UUID.randomUUID();

        Mockito.when(readClientUseCase.execute(Mockito.any(ClientId.class)))
                .thenThrow(new ValidationException("Client not found"));

        mvc.perform(get("/api/clients/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Client not found"));
    }


}