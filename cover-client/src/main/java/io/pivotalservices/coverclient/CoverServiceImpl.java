package io.pivotalservices.coverclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by benwilcock on 22/11/2016.
 * This service class handles the communication with the `covers-service` application.
 * This callout features a [Circuit Breaker] and requires the [Registry] to contain
 * an entry for the dependent `covers-service`.
 */
@Service
@RefreshScope
public class CoverServiceImpl implements CoverService {

    private static final Logger LOG = LoggerFactory.getLogger(CoverService.class);
    private static final String SERVICE_PREFIX = "//";
    private static final String ENDPOINT_PREFIX = "/";

    private RestTemplate restTemplate;

    @Value("${cover.client.failsafe.cover-types:NotConfigured}")
    private String coverTypes;

    @Value("${cover.client.random-faults:false}")
    private boolean randomFaults;

    @Value("${cover.client.coverServiceLogicalName:COVER-SERVICE}")
    private String coverServiceLogicalName; // The name registered in the Eureka [Registry]

    @Value("${cover.client.coverTypesEndpoint:covers}")
    private String coverTypesEndpoint;

    public CoverServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * This method is used to discover the latest types of Cover available.
     * It does this by calling out to the `covers-service` microservice.
     * The target microservice is only given a logical name (//COVERS-SERVICE).
     * This name is resolved by the RestTemplate using the [Registry] of services in Eureka.
     * @return
     */
    @HystrixCommand(fallbackMethod = "getCoversFallbackMethod") // Identify the fallback method if this method fails
    public String getCovers() {

        if(randomFaults){
            LOG.debug("Random faults are configured and could happen...");
            double random = (Math.random() * 100); // Fail a percentage of the time.
            if(random > 90.0d) {
                LOG.debug("A random fault was triggered (random > 90.0d = {}) [{}]", random > 90.0d, random);
                LOG.warn("*** Simulating a FAILURE ***");
                throw new RuntimeException("A Random [Simulated] Error Occurred!");
            }
        }

        try {
            /** Use a logical name to identify the target microservice **/
            URI uri = URI.create(SERVICE_PREFIX + coverServiceLogicalName + ENDPOINT_PREFIX + coverTypesEndpoint);
            LOG.debug("Calling the 'covers-service' ({}) to get all the latest types of cover....", uri.toString());
            String covers = this.restTemplate.getForObject(uri, String.class);
            LOG.debug("The latest types of cover include: {}", covers);
            return covers;
        } catch (Throwable e){
            LOG.warn("There was an unexpected problem: {} '{}'", e.getClass().toString(), e.getMessage());
            throw e;
        }
    }

    /**
     * This is the Fallback method that Hystrix will use when the 'getCovers()' method fails.
     * @return
     */
    public String getCoversFallbackMethod() {
        LOG.warn("Using the fallback method as there was an issue getting the cover types.");
        return coverTypes;
    }

}
