CREATE TABLE outbox_events
(
    id             uuid PRIMARY KEY,
    aggregate_type varchar(50)  NOT NULL,
    aggregate_id   uuid         NOT NULL,
    event_type     varchar(100) NOT NULL,
    payload        jsonb        NOT NULL,
    status         varchar(20)  NOT NULL DEFAULT 'NEW',
    retry_count    integer      NOT NULL DEFAULT 0,
    created_at     timestamptz  NOT NULL DEFAULT now(),
    published_at   timestamptz,
    last_error     text
);

CREATE INDEX idx_outbox_events_status_created_at
    ON outbox_events (status, created_at);