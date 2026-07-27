package com.immx.industrialsupport.supportservice.services.sla;

import com.immx.industrialsupport.contracts.incident.IncidentStatus;
import com.immx.industrialsupport.integrationcontracts.common.AggregateType;
import com.immx.industrialsupport.integrationcontracts.common.EventType;
import com.immx.industrialsupport.integrationcontracts.incident.IncidentSlaBreachedEvent;
import com.immx.industrialsupport.supportservice.entities.Incident;
import com.immx.industrialsupport.supportservice.repositories.IncidentRepository;
import com.immx.industrialsupport.supportservice.services.outbox.IOutboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/**
 * Сервис по отслеживанию истекших обращений.
 */
@Service
public class SlaMonitoringService implements ISlaMonitoringService {

    private static final Set<IncidentStatus> MONITORED_STATUSES = Set.of(
            IncidentStatus.NEW,
            IncidentStatus.ASSIGNED,
            IncidentStatus.IN_PROGRESS);

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IOutboxService outboxService;

    @Autowired
    private Clock clock;

    @Value("${app.sla.monitor.batch-size:100}")
    private int batchSize;

    @Override
    @Transactional
    public int processOverdueIncidents() {
        OffsetDateTime now = OffsetDateTime.now(clock);

        List<Incident> overdueIncidents = incidentRepository.findOverdueIncidents(
                now,
                MONITORED_STATUSES,
                PageRequest.of(
                        0,
                        batchSize));

        for(Incident incident : overdueIncidents) {
            incident.markSlaAsBreached(now);

            IncidentSlaBreachedEvent payload = new IncidentSlaBreachedEvent(
                    incident.getId(),
                    incident.getOrganization()
                            .getId(),
                    incident.getDepartment()
                            .getId(),
                    incident.getPriority(),
                    incident.getStatus(),
                    incident.getSlaDeadline(),
                    now);

            outboxService.save(
                    AggregateType.INCIDENT,
                    incident.getId(),
                    EventType.INCIDENT_SLA_BREACHED,
                    payload);
        }

        return overdueIncidents.size();
    }
}
