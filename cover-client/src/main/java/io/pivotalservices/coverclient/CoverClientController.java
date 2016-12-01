package io.pivotalservices.coverclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by benwilcock on 01/12/2016.
 */
@RestController
public class CoverClientController {

    private static final Logger LOG = LoggerFactory.getLogger(CoverClientController.class);

    @Autowired // Wires in the CoversService component
    private CoverService coverService;

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
    public String myCovers() {
        LOG.info("Asking for all known cover types...");
        String covers = coverService.getCovers();
        LOG.info("Found the following cover types: {}", covers);
        return covers;
    }
}
