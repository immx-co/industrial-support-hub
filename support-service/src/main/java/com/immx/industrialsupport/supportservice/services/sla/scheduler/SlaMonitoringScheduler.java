package com.immx.industrialsupport.supportservice.services.sla.scheduler;

import com.immx.industrialsupport.supportservice.services.sla.ISlaMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Планировщик фонового контроля <code>SLA</code> обращений.
 */
@Component
@ConditionalOnProperty(
        name = "app.sla.monitor.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class SlaMonitoringScheduler {

    @Autowired
    private ISlaMonitoringService slaMonitoringService;

    /**
     * Запускает поиск и обработку просроченных обращений.
     */
    @Scheduled(fixedDelayString = "${app.sla.monitor.delay:30000}")
    public void monitorSla() {
        slaMonitoringService.processOverdueIncidents();
    }
}
