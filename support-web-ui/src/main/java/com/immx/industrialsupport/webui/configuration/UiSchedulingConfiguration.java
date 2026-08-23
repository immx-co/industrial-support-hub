package com.immx.industrialsupport.webui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class UiSchedulingConfiguration {

    @Bean
    public TaskScheduler uiTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("ui-session-expiration");
        scheduler.setRemoveOnCancelPolicy(true);

        return scheduler;
    }
}
