CREATE TABLE users
(
    id            UUID                     NOT NULL DEFAULT gen_random_uuid(),
    department_id UUID                     NOT NULL,
    external_id   VARCHAR(100),
    username      VARCHAR(100)             NOT NULL,
    email         VARCHAR(255)             NOT NULL,
    password_hash VARCHAR(255)             NOT NULL,
    first_name    VARCHAR(100)             NOT NULL,
    last_name     VARCHAR(100)             NOT NULL,
    enabled       BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_users
        PRIMARY KEY (id),

    CONSTRAINT fk_users_department
        FOREIGN KEY (department_id)
            REFERENCES departments (id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_users_external_id
        UNIQUE (external_id)
);

CREATE UNIQUE INDEX uq_users_username_lower
    ON users (LOWER(username));

CREATE UNIQUE INDEX uq_users_email_lower
    ON users (LOWER(email));

CREATE INDEX idx_users_department_id
    ON users (department_id);