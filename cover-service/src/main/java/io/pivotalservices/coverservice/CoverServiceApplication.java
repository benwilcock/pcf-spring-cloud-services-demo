package io.pivotalservices.coverservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;


@SpringBootApplication // Standard Spring Boot application identifier
@EnableDiscoveryClient // Enables registry registration with the [Registry]
public class CoverServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CoverServiceApplication.class);
	/**
	 * This @Bean definition tells Sleuth to establish the 'Always[On]Sampler` as the
	 * defaultSampler. This results in _all_ requests, responses and callouts being logged
	 * to [Zipkin] (even the [Zipkin] timing messages themselves).
	 * @return
	 */
	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

    /**
     * If you set an ENV variable for the container called TEST_ENV_VARIABLE, this property would
     * be set and would be available for use. You can set container variables in two ways. The
     * first is to use `cf set-env` at the command line. The second is to configure the variable
     * alongside the application that needs it in the `manifest.yml` file, for example...
     *
     * env:
     *   TEST_ENV_VARIABLE: my-value-here
     */
	@Value("${test.env.variable:NotConfigured}")
    private String testEnvVariable;


	/**
	 * Used to start the application when packaged as a Java JAR. Uses String Boot's default web configuration
	 * which includes the embedded Tomcat webserver.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(CoverServiceApplication.class, args);
	}

    /**
     * Called once Spring is configured but before starting the application proper.
     */
	@PostConstruct
    public void dump(){
        LOG.info("The TEST_ENV_VARIABLE is set to '{}'", testEnvVariable);
    }
}
