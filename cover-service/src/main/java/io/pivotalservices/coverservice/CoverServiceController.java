package io.pivotalservices.coverservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Created by benwilcock on 01/12/2016.
 */
@RefreshScope
@RestController
public class CoverServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(CoverServiceApplication.class);

    @Value("${cover.service.cover-types:NotConfigured}")
    private String covers;

    @Value("${cover.service.random-delay:false}")
    private boolean addRandomDelay;

    @GetMapping(value = "/covers")
    public String getCovers(){
        LOG.debug("Getting all known cover types...");

        if(addRandomDelay) {
            long random = (new Double(Math.random() * 1000)).longValue();
            LOG.debug("The property 'cover.service.random-delay' is set to {} so pausing for {} milliseconds.", addRandomDelay, random);
            try {
                TimeUnit.MILLISECONDS.sleep(random); // Adding a random sleep interval to make trace more interesting.
            } catch (InterruptedException ie){
                //The sleep was interrupted.
                LOG.trace("The sleep for {} milliseconds was interrupted: {} ", random, ie.getMessage());
            }
        }

        LOG.debug("Returning the cover types: {}", covers);
        return covers;
    }
}
