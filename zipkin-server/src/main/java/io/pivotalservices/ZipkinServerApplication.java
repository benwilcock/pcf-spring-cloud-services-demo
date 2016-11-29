package io.pivotalservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;


/**
 * As a Spring Boot application, much of the configuration is dependent on the
 * classpath dependencies such as Sleuth, Stream, Rabbit etc. See the `build.gradle`
 * for details.
 **/
@EnableDiscoveryClient // Enables Auto registration with the [Registry]
@EnableZipkinStreamServer // Enables [Zipkin] Server and allows Stream collection
@SpringBootApplication
public class ZipkinServerApplication {

    /**
     * As you can see, there is little that needs to be done here thanks to Spring Boot & Cloud.
     * Just build and deploy this [Zipkin] service app to Pivotal Cloud Foundry using the manifest
     * provided in the root folder. Just make sure that the [Rabbit] and the [Registry] PCF services
     * are bound to the app in order to properly fulfil it's dependencies. When Zipkin starts
     * it will attach to Rabbit and wait for the Sleuth messages to start to come through from
     * the 'covers-consumer' and 'covers-service' microservices.
     */
	public static void main(String[] args) {
		SpringApplication.run(ZipkinServerApplication.class, args);
	}
}
