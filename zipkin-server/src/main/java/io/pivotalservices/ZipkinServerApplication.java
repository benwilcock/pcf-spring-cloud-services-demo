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

	public static void main(String[] args) {
		SpringApplication.run(ZipkinServerApplication.class, args);
	}
}
