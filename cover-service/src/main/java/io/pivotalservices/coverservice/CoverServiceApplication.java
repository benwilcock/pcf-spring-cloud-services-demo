package io.pivotalservices.coverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
public class CoverServiceApplication {

	/**
	 * This bean definition tells Sleuth to establish the 'Always[On]Sampler` as the
	 * defaultSampler. This results in _all_ requests, responses and callouts being logged
	 * to [Zipkin].
	 * @return
	 */
	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(CoverServiceApplication.class, args);
	}
}
