package io.pivotalservices.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@EnableCircuitBreaker
@EnableDiscoveryClient
@RestController
@SpringBootApplication
public class MicroserviceConsumerApplication {

    Logger LOG = LoggerFactory.getLogger(MicroserviceConsumerApplication.class);

    @Autowired
    private CoverService coverService;

    @Bean
    @LoadBalanced
    public RestTemplate rest(RestTemplateBuilder builder) {
        return builder.build();
    }

    @GetMapping("/mycovers")
    public String toRead() {
        String covers = coverService.getCovers();
        LOG.info("Found the following covers: {}", covers);
        return covers;
    }

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConsumerApplication.class, args);
	}
}
