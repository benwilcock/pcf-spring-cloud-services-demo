package io.pivotalservices.coversservice.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Adds the [Registry] feature by including the @EnableDiscoveryClient annotation.
 * Also configures the [Zipkin] AlwaysSampler as the default sampler so that everything
 * is sent to [Zipkin] automatically.
 *
 * Created by benwilcock on 30/11/2016.
 */
@Configuration
@EnableDiscoveryClient
public class Config {

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
}
