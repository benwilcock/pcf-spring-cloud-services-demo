package io.pivotalservices.coversservice.config;

import org.springframework.context.annotation.Configuration;

/**
 * This configuration is for use when testing and removes the [Registry]
 * @EnableDiscoveryClient annotation so that boot time is quicker and
 * there are fewer exceptions from the discovery client in the test log.
 * This configuration also removes the [Sleuth] AlwaysSampler for similar
 * reasons.
 *
 * Created by benwilcock on 30/11/2016.
 */
@Configuration
public class Config {

}
