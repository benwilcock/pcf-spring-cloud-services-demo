package io.pivotalservices.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceApplication {

    Logger LOG = LoggerFactory.getLogger(MicroserviceApplication.class);

	@Value("${cover.service.cover-types:MisConfigured-Cover-Types}")
	private String covers;


	@GetMapping(value = "/covers")
	public String getCovers(){
        LOG.info("Returning covers: {}", covers);
        return covers;
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceApplication.class, args);
	}
}
