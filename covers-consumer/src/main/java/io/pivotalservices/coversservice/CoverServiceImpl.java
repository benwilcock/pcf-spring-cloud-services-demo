package io.pivotalservices.coversservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CoverServiceImpl implements CoverService {

    /** Use a logical name to identify the consumed microservice **/
    private static final String COVERS_SERVICE_LOGICAL_NAME = "//COVERS-SERVICE"; // registered in the Eureka [Registry]
    private static final String COVER_TYPES_ENDPOINT_NAME = "/covers";

    private static final Logger LOG = LoggerFactory.getLogger(CoverService.class);

    /** The RestTemplate to use when calling other RESTful services.**/
    private RestTemplate restTemplate;

    /**
     * Wire in the RestTemplate from the context.
     * @param restTemplate
     */
    @Autowired
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
    @HystrixCommand(fallbackMethod = "reliable") // Identify the fallback method if this method fails
    public String getCovers() {

        double random = (Math.random() * 100);

        if(random < 90.0d) { // Fail a percentage of the time.

            /** Use a logical name to identify the target microservice **/
            URI uri = URI.create(COVERS_SERVICE_LOGICAL_NAME + COVER_TYPES_ENDPOINT_NAME);
            LOG.info("Calling the 'covers-service' to get all the latest types of cover....");
            String covers = this.restTemplate.getForObject(uri, String.class);
            LOG.info("The latest types of cover include: {}", covers);
            return covers;
        } else {
            LOG.warn("*** Simulating a FAILURE ***");
            throw new RuntimeException("A Random [Simulated] Error Occurred!");
        }
    }

    /**
     * This is the Fallback method that Hystrix will use when the 'getCovers()' method fails.
     * @return
     */
    public String reliable() {
        LOG.warn("Using the fallback method as there seems to be an issue getting cover types.");
        return "No Cover";
    }

}
