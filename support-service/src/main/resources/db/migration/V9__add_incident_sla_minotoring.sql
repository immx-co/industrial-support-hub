ALTER TABLE incidents
    ADD COLUMN sla_breached BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE incidents
    ADD COLUMN sla_breached_at TIMESTAMP WITH TIME ZONE;

CREATE INDEX idx_incidents_sla_monitoring
    ON incidents (sla_deadline)
    WHERE sla_breached = FALSE
      AND status IN ('NEW', 'ASSIGNED', 'IN_PROGRESS');