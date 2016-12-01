package io.pivotalservices.coverservice;

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
public class CoversServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CoversServiceApplication.class);

	@Value("${cover.service.cover-types:NotConfigured}")
	private String covers;

	@Value("${cover.service.random-delay:true}")
	private boolean addRandomDelay;

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

	@GetMapping(value = "/covers")
	public String getCovers() throws InterruptedException {
		LOG.debug("Getting all known cover types...");

		if(addRandomDelay) {
			long random = (new Double(Math.random() * 1000)).longValue();
			TimeUnit.MILLISECONDS.sleep(random); // Adding a random sleep interval to make trace more interesting.
		}

        LOG.debug("Returning the cover types: {}", covers);
        return covers;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoversServiceApplication.class, args);
	}
}
