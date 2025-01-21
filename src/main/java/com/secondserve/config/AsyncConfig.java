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
                .corePoolSize(16)
                .maxPoolSize(32)
                .queueCapacity(100)
                .threadNamePrefix("AsyncExecutor-")
                .build();
        executor.initialize();

        return executor;
    }
}
