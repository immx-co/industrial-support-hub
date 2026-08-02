CREATE TABLE notifications
(
    id               UUID         NOT NULL,
    event_id         UUID         NOT NULL,
    event_type       VARCHAR(50)  NOT NULL,
    organization_id  UUID         NOT NULL,
    incident_id      UUID         NOT NULL,

    recipient_type   VARCHAR(20)  NOT NULL,
    recipient_value  VARCHAR(100) NOT NULL,

    channel           VARCHAR(20)  NOT NULL,
    status            VARCHAR(20)  NOT NULL,

    subject           VARCHAR(200) NOT NULL,
    message           TEXT         NOT NULL,

    retry_count       INTEGER      NOT NULL DEFAULT 0,
    last_error        TEXT,

    created_at        TIMESTAMPTZ  NOT NULL,
    sent_at           TIMESTAMPTZ,

    CONSTRAINT pk_notifications
        PRIMARY KEY (id),

    CONSTRAINT uq_notifications_event_recipient_channel
        UNIQUE (event_id, recipient_type, recipient_value, channel),

    CONSTRAINT chk_notifications_retry_count
        CHECK (retry_count >= 0)
);

CREATE INDEX ix_notifications_status_created_at
    ON notifications (status, created_at);

CREATE INDEX ix_notifications_organization_id
    ON notifications (organization_id);

CREATE INDEX ix_notifications_incident_id
    ON notifications (incident_id);