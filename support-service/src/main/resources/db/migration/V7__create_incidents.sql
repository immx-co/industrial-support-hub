CREATE TABLE incidents
(
    id                  UUID PRIMARY KEY,
    organization_id     UUID                     NOT NULL,
    department_id       UUID                     NOT NULL,
    reporter_id         UUID                     NOT NULL,
    assigned_engineer_id UUID,

    title               VARCHAR(200)             NOT NULL,
    description         TEXT                     NOT NULL,
    priority            VARCHAR(30)              NOT NULL,
    status              VARCHAR(30)              NOT NULL,

    sla_deadline         TIMESTAMP WITH TIME ZONE,
    resolved_at          TIMESTAMP WITH TIME ZONE,
    closed_at            TIMESTAMP WITH TIME ZONE,

    created_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITH TIME ZONE NOT NULL,

    version              BIGINT                   NOT NULL DEFAULT 0,

    CONSTRAINT fk_incidents_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id),

    CONSTRAINT fk_incidents_department
        FOREIGN KEY (department_id)
            REFERENCES departments (id),

    CONSTRAINT fk_incidents_reporter
        FOREIGN KEY (reporter_id)
            REFERENCES users (id),

    CONSTRAINT fk_incidents_engineer
        FOREIGN KEY (assigned_engineer_id)
            REFERENCES users (id)
);