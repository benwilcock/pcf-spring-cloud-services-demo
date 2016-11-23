# PCF Spring Cloud Services Demo

This simple project demonstrates the use of Spring Cloud Services in PCF from Spring Boot microservice applications.

It contains two Spring Boot Microservices:

 1. The Insurance Covers App `covers-consumer` which depends on...
 2. The Covers Microservice `covers-service`

Between them, these services use all three components of the Spring Cloud Services pack for Pivotal Cloud Foundry.

 1. `covers-consumer` uses the Eureka (Registry) to discover the `covers-service`.
 2. `covers-consumer` uses Hystrix (Service Breaker) to protect itself from the loss of the `covers-service`.
 3. `covers-service` uses Spring Cloud Config to get the list of "insurance covers" it provides.

## About the Author

[Ben Wilcock](https://uk.linkedin.com/in/benwilcock) works for Pivotal as a Cloud Solutions Architect. Ben has a passion for microservices, cloud and mobile applications and helps [Pivotal's Cloud Foundry](http://pivotal.io/platform) customers to become more responsive, innovate faster and gain greater returns from their software investments. Ben is also a respected technology [blogger](http://benwilcock.wordpress.com) who's articles have featured in [DZone](https://dzone.com/users/296242/benwilcock.html), [Java Code Geeks](https://www.javacodegeeks.com/author/ben-wilcock/), [InfoQ](https://www.infoq.com/author/Ben-Wilcock) and more.
