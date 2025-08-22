package com.zy.ws.mgpdf;

import com.zy.lib.message.component.Message;
import com.zy.lib.message.resource.Constant;
import com.zy.ws.mgpdf.conf.FileStorageProperties;
import com.zy.ws.mgpdf.feignclient.IFeignClient;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
@ComponentScan(basePackageClasses = {Application.class, Constant.class, Message.class})
@EnableFeignClients(basePackageClasses = IFeignClient.class)
@EnableEurekaClient
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(60);
		executor.setMaxPoolSize(60);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("PdfServer-");
		executor.initialize();
		return executor;
	}
}
