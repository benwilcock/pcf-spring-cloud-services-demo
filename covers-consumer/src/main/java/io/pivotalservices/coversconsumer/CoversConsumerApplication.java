package io.pivotalservices.coversconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * This method offers an endpoint called '/mycovers' that will accept an empty GET
 * request. It then uses the `covers-service` to get the latest types of cover available
 * before returining this list to the user.
 *
 * If the covers service is unavailable, a [Circuit Breaker] kicks in which returns a single
 * choice of `No Cover`.
 */
@RestController // Spring Stereotype
@SpringBootApplication  // Identified this application as a Spring Boot application
@EnableCircuitBreaker // Turns on the Hystrix [Circuit Breaker] features for this application
@EnableDiscoveryClient // Allows this microservice to register itself with the [Registry]
public class CoversConsumerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CoversConsumerApplication.class);

    @Autowired // Wires in the CoversService component
    private CoverService coverService;

    @Bean
    @LoadBalanced // Tell the RestTemplate to use a load balancer like Ribbon
    public RestTemplate rest(RestTemplateBuilder builder) {
        return builder.build();
    }

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

    /**
     * This method offers an endpoint called '/mycovers' that will accept an empty GET
     * request. It then uses the `covers-service` to get the latest types of cover available
     * before returining this list to the user.
     *
     * If the covers service is unavailable, a [Circuit Breaker] kicks in which returns a single
     * choice of `No Cover`.
     *
     * @return String Types of cover available.
     */
    @GetMapping("/mycovers")
    public String toRead() {
        LOG.info("Asking for all known cover types...");
        String covers = coverService.getCovers();
        LOG.info("Found the following cover types: {}", covers);
        return covers;
    }

    /**
     * Used to boot this microservice application using an embedded web server.
     * @param args
     */
	public static void main(String[] args) {
		SpringApplication.run(CoversConsumerApplication.class, args);
	}
}
