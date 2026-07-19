CREATE TABLE organizations
(
    id          UUID                     NOT NULL DEFAULT gen_random_uuid(),
    external_id VARCHAR(100),
    name        VARCHAR(255)             NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_organizations PRIMARY KEY (id),
    CONSTRAINT uq_organizations_external_id UNIQUE (external_id)
);

CREATE TABLE departments
(
    id              UUID                     NOT NULL DEFAULT gen_random_uuid(),
    organization_id UUID                     NOT NULL,
    external_id     VARCHAR(100),
    name            VARCHAR(255)             NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_departments PRIMARY KEY (id),

    CONSTRAINT fk_departments_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id),

    CONSTRAINT uq_departments_external_id
        UNIQUE (organization_id, external_id)
);

CREATE INDEX idx_departments_organization_id
    ON departments (organization_id);