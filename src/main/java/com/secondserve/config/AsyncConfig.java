package com.secondserve.config;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(destroyMethod = "shutdown")
    public Executor asyncExecutor() {
        final var executor = new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(100)
                .maxPoolSize(500)
                .threadNamePrefix("CustomTP-")
                .build();
        executor.initialize();

        return executor;
    }
}
