#  Vaudoise Insurance API

##  Overview

**Vaudoise API** is a clean, domain-driven REST application built with **Spring Boot 3**, **Java 21**, and **PostgreSQL**.  
It manages **clients (persons & companies)** and their **insurance contracts**, designed with **Hexagonal Architecture (Ports & Adapters)** to ensure clear separation between business logic and infrastructure.

---

##  Architecture & Design

The project follows **Clean / Hexagonal Architecture** principles:

-  **Domain Layer** — Pure business logic (`Client`, `Contract`, `Name`, `CompanyIdentifier`...)
-  **Application Layer** — Implements **use cases** (`CreateClientUseCase`, `GetTotalActiveCostUseCase`...) that orchestrate domain logic through **ports**.
-  **Infrastructure Layer** — Contains adapters:
    - JPA repositories for PostgreSQL
    - REST controllers for the API
    - DTO mappers and error handlers

This structure guarantees that the **domain** remains **independent** from frameworks or databases.  
 *Proof*: the project was initially implemented with **in-memory repositories**, then migrated to **PostgreSQL** without modifying domain or use cases.

---

##  Run Locally
### 1. Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker & Docker Compose**

---

###  2. Start PostgreSQL (via Docker Compose)

Since your `docker-compose.yml` file is already at the project root, simply run:

```bash
docker compose up -d
```

This will start a PostgreSQL 15 container named `vaudoise-db` accessible at port `5432`.

---

###  3. Start the Application

Once the database is running, launch the Spring Boot app:

```bash
mvn clean spring-boot:run
```

 The application starts on  
 [http://localhost:8080](http://localhost:8080)

---

###  API Documentation (Swagger)

Once running, open:

- Swagger UI → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI Docs → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

##  Example API Requests

###  Create a Person
```http
POST /api/clients/person
Content-Type: application/json

{
  "name": "Mohamed Aymen TLILI",
  "email": "aymentli@gmail.com",
  "phone": "+41790001122",
  "birthdate": "1991-02-18"
}
```

###  Create a Company
```http
POST /api/clients/company
Content-Type: application/json

{
  "name": "Vaudoise",
  "email": "contact@vaudoise.ch",
  "phone": "+41215555555",
  "companyIdentifier": "vaa-123"
}
```

###  Create a Contract
```http
POST /api/contracts/create-new
Content-Type: application/json

{
  "clientId": "UUID_OF_CLIENT",
  "startDate": "2025-01-01",
  "endDate": "2026-01-01",
  "costAmount": 2000.00
}
```

###  Get All Contracts for a Client
```http
GET /api/contracts/clients/{clientId}
```

###  Get Total Active Cost
```http
GET /api/contracts/clients/{clientId}/contracts/total-active-cost
```

---

##  Why It Works

 Domain is framework-agnostic (pure Java, no Spring dependency).  
 Repositories are injected via interfaces → persistence easily replaceable.  
 Flyway ensures consistent and versioned database schema.  
 Unit & integration tests validate every layer.  
 Switching from InMemory to PostgreSQL required only new adapters — no change in business logic.

---

##  Run Tests

```bash
mvn clean test
```

Expected output:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Results:
Tests run: 87, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

**Tests include:**
- Domain model validation
- Use cases (Create, Read, Update, Delete)
- REST controllers (MockMvc)
- InMemory & JPA repository implementations

---

##  Tech Stack

| Layer | Technology |
|-------|-------------|
| Framework | Spring Boot 3.5.6 |
| Language | Java 21 |
| Database | PostgreSQL 15 |
| Migration | Flyway |
| API Docs | Springdoc OpenAPI (Swagger) |
| Testing | JUnit 5, Mockito |
| Packaging | Maven |

---

##  Proof of Architecture Independence

Initially implemented with:

- `InMemoryClientRepo`
- `InMemoryContractRepo`

Then switched to:

- `JpaClientRepository`
- `JpaContractRepository`

 No change to domain, controllers, or use cases — true separation of concerns.

---

##  Future Improvements

Potential evolutions include:

-  Add authentication (JWT / OAuth2)
-  CI/CD deployment via GitHub Actions (AWS / GCP)
-  Add Redis caching for contract queries

These evolutions demonstrate the system’s **flexibility**, **testability**, and **maintainability**.

---

##  Author

**Mohamed Aymen Tlili**  
Fullstack Java Developer — Lausanne 🇨🇭
> Designed for maintainability, domain purity, and infrastructure independence.

---

###  Quick Recap

1. Your `docker-compose.yml` is already ready.
2. Start PostgreSQL:
   ```bash
   docker compose up -d
   ```
3. Run the app:
   ```bash
   mvn clean spring-boot:run
   ```
4. Open [http://localhost:8080](http://localhost:8080) and test endpoints.

---
