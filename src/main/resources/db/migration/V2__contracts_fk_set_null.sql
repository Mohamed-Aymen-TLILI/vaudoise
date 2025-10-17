ALTER TABLE contracts ALTER COLUMN client_id DROP NOT NULL;

ALTER TABLE contracts DROP CONSTRAINT IF EXISTS contracts_client_id_fkey;

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_client
        FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_contracts_client_id ON contracts (client_id);
