package io.pivotalservices.coversservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RefreshScope
@RestController
@SpringBootApplication
@ComponentScan("io.pivotalservices.coversservice")
public class CoversServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CoversServiceApplication.class);

	@Value("${cover.service.cover-types:NotConfigured}")
	private String covers;

	@Value("${cover.service.random-delay:true}")
	private boolean addRandomDelay;

	@GetMapping(value = "/covers")
	public String getCovers() throws InterruptedException {
		LOG.info("Getting all known cover types...");

		if(addRandomDelay) {
			long random = (new Double(Math.random() * 1000)).longValue();
			TimeUnit.MILLISECONDS.sleep(random); // Adding a random sleep interval to make trace more interesting.
		}

        LOG.info("Returning cover types: {}", covers);
        return covers;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoversServiceApplication.class, args);
	}
}
