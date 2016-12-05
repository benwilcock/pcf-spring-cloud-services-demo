package io.pivotalservices.coverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;


@SpringBootApplication // Standard Spring Boot application identifier
@EnableDiscoveryClient // Enables registry registration with the [Registry]
public class CoverServiceApplication {

	/**
	 * This @Bean definition tells Sleuth to establish the 'Always[On]Sampler` as the
	 * defaultSampler. This results in _all_ requests, responses and callouts being logged
	 * to [Zipkin] (even the [Zipkin] timing messages themselves).
	 * @return
	 */
	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	/**
	 * Used to start the application when packaged as a Java JAR. Uses String Boot's default web configuration
	 * which includes the embedded Tomcat webserver.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(CoverServiceApplication.class, args);
	}
}
