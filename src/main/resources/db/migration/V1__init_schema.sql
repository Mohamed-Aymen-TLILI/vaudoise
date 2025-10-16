CREATE TABLE clients (
        id UUID PRIMARY KEY,
        type VARCHAR(20) NOT NULL,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        phone VARCHAR(20) NOT NULL,
        birth_date DATE,
        company_identifier VARCHAR(50)
);

CREATE TABLE contracts (
        id UUID PRIMARY KEY,
        client_id UUID NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
        start_date DATE NOT NULL,
        end_date DATE,
        cost_amount NUMERIC(12,2) NOT NULL,
        last_update_date TIMESTAMP
);
