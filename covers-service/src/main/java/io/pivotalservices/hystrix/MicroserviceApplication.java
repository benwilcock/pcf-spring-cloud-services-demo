package io.pivotalservices.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RefreshScope
@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceApplication {

    Logger LOG = LoggerFactory.getLogger(MicroserviceApplication.class);

	@Value("${cover.service.cover-types:MisConfigured-Cover-Types}")
	private String covers;


	@GetMapping(value = "/covers")
	public String getCovers() throws InterruptedException {
		LOG.info("Getting all known cover types...");
        long random = (new Double(Math.random() * 1000)).longValue();
        TimeUnit.MILLISECONDS.sleep(random); // Adding a random sleep interval to make trace more interesting.
        LOG.info("Returning cover types: {}", covers);
        return covers;
	}

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceApplication.class, args);
	}
}
