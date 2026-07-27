package com.immx.industrialsupport.supportservice.services.sla.scheduler;

import com.immx.industrialsupport.supportservice.services.sla.ISlaMonitoringService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SlaMonitoringScheduler {

    @Autowired
    private ISlaMonitoringService slaMonitoringService;

    /**
     * Запускает поиск и обработку просроченных обращений.
     */
    @Scheduled(fixedDelayString = "${app.sla.monitor.delay:30000}")
    public void monitorSla() {
        long startedAt = System.nanoTime();

        log.info("Поиск просроченных обращений запущен.");

        int processedCount = slaMonitoringService.processOverdueIncidents();

        long durationMs = (System.nanoTime() - startedAt) / 1_000_000;

        log.info(
                "Поиск просроченных обращений завершён. Обработано: {}, время выполнения: {} мс",
                processedCount,
                durationMs);
    }
}
