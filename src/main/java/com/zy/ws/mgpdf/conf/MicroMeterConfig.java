package com.zy.ws.mgpdf.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 *
 * @author John Sevillano
 * @version 1.0 ,18/11/2021
 * @since 1.0
 * @see documento "Estandar de programación Java 1.0"
 */
@Configuration
public class MicroMeterConfig {
    @Value("${spring.application.name}")
    String appName;

    @Value("${eureka.instance.instance-id}")
    String instanceId;

    @Bean
    MeterRegistryCustomizer<PrometheusMeterRegistry> configureMetricsRegistry(){
        return registry -> registry.config().commonTags("application", appName,"instance",instanceId);
    }
}
