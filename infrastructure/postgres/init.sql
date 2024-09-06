CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE credits (
    credit_id UUID PRIMARY KEY,
    status VARCHAR(255),
    installments_count INTEGER,
    amount DECIMAL(19, 2),
    user_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_user_id ON credits(user_id);

CREATE TABLE installments (
    id UUID PRIMARY KEY,
    amount DECIMAL(19, 2),
    status VARCHAR(255),
    due_date DATE,
    credit_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credit_id FOREIGN KEY (credit_id) REFERENCES credits(credit_id) ON DELETE CASCADE
);
CREATE INDEX idx_credit_id ON installments(credit_id);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    amount DECIMAL(19, 2),
    installment_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_installment_id FOREIGN KEY (installment_id) REFERENCES installments(id) ON DELETE CASCADE
);
CREATE INDEX idx_installment_id ON payments(installment_id);

INSERT INTO users (id, first_name, last_name, created_at, updated_at)
VALUES ('b9e661d3-4ca3-4be1-9887-0bfb128a7b15', 'Al', 'Pacino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);