package io.pivotalservices.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by benwilcock on 22/11/2016.
 */
@Service
public class CoverService {

    Logger LOG = LoggerFactory.getLogger(CoverService.class);

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "reliable")
    public String getCovers() {

        double random = (Math.random() * 100);

        if(random < 90.0d) {
            URI uri = URI.create("//COVERS-SERVICE/covers");
            return this.restTemplate.getForObject(uri, String.class);
        } else {
            LOG.info("*** Simulating a FAILURE ***");
            throw new RuntimeException("A Random Error Occurred!");
        }
    }

    public String reliable() {
        return "No Cover";
    }

}
