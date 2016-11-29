# PCF Spring Cloud Services Demo

This simple project demonstrates the use of Spring Cloud Services in PCF from Spring Boot microservice applications.

It contains two Spring Boot Microservices:

 1. The Insurance Covers App `covers-consumer` which depends on...
 2. The Covers Microservice `covers-service`

Between them, these services use all three components of the Spring Cloud Services pack for Pivotal Cloud Foundry.

 1. `covers-consumer` uses the Eureka (Registry) to discover the `covers-service`.
 2. `covers-consumer` uses Hystrix (Service Breaker) to protect itself from the loss of the `covers-service`.
 3. `covers-service` uses Spring Cloud Config to get the list of "insurance covers" it provides.

## Installing

> I'm going to assume you have setup PCF and have the Spring Cloud Services available in the marketplace. If you don't have PCF or don't have the necessary Spring Cloud Services installed, try [PCF Dev](https://docs.pivotal.io/pcf-dev/) instead (but remember to start PCF Dev with Spring Cloud Services available using `cf dev start -s scs` - you'll need 8GB free ram to do this).

1. Clone this repository into a folder. Make this folder your current folder in the terminal.

2. Login to PCF and check the mayketplace to make sure the Spring Cloud Services are available to you.

````
$ cf marketplace
````

3. If the services are available, you can now go ahead and provision some service instances using the script provided.

````
$ ./setup-services.sh
````

You'll have to wait a few minutes while PCF gets on with provisioning the services you've asked for. Check the status of the services periodically using the foillowing command.

````
$ cf services
````

When the Spring Cloud Services services have been provisioned your output form this command should look similar to that shown below. Services called `breaker`, `registry`, `rabbit` and `config` have been added to your developer space in PCF.

````
name       service                       plan       bound apps                        last operation
breaker    p-circuit-breaker-dashboard   standard   covers-consumer                   create succeeded
registry   p-service-registry            standard   covers-consumer, covers-service,  create succeeded
config     p-config-server               standard   covers-service                    create succeeded
rabbit     p-rabbitmq                    standard   covers-zipkin, covers-service...  create succeeded
````

4. You can now build and push the demo Spring Boot microservices to PCF as follows.

````
$ ./gradlew clean assemble
$ cf push
````

> Both the `gradlew` command and a `manifest.yml` describing the microservices has been provided.

Once the apps have been deployed to PCF, you can begin to check out their features.

## Server Consoles

Once deployed, you'll find that the following PCF services have made UI consoles available. 

 - RabbitMQ (RabbitMQ Console)
 - Service Registry (Eureka Dashboard)
 - Circuit Breaker (Hystrix Dashboard)

You can get to these consoles by following the `Services(x) -> [Your Service Name] > Manage` link in the PCF Apps Manager application.

Zipkin also provides a user interface which you can get to by opening the app in your browser using the link provided in the PCF Apps Manager. 

## Using the Microservices

To test the microservice apps you can use `curl` to send requests to the `covers-consumer` microservice using the following command. 

````
$ while true; do sleep 1; curl covers-consumer.local.pcfdev.io/mycovers; echo -e '\n'$(date); done
````

> If you're familiar with JMeter, you may prefer to try the JMeter project I've included in the root folder to automate the sending of these requests.

When everything is working (i.e. when the `covers-consumer` microservice can see and talk to the `covers-service` microservice) you'll see output similar to that shown below...

````
No Cover, Auto Cover, Home Cover, Duvet Cover
Thu 24 Nov 2016 10:58:15 GMT
````

The types of insurance cover available (no cover, auto cover, home cover, duvet cover) comes from a configuration property sourced from the *Config Service*.

There is a purposely engineered exception that will occasionally cause the circuit breaker to trip. When the fault occurs you'll see...

````
No Cover
Thu 24 Nov 2016 10:58:14 GMT
````

This is the `covers-consumer` using a fallback method to provide a reduced set of covers because it thinks the `covers-service` is unavailable.

## How it works

1. The config can be refreshed at runtime without restarting. If the config changes in Git [here](https://github.com/benwilcock/app-config/blob/master/covers-service.yml), the changes can be applied while the service is still running by calling the `/refresh` endpoint. This endpoint is automatically added by the `spring boot actuator`.

````
curl -X POST covers-service.<your-pcf-domain-name>/refresh
````

This is standard Spring Cloud Config functionality. What's special about this is that if you have 100 running instances of a microservice, one POST to `/refresh` is all you need to change it for all instances and without rebooting. Pretty neat hey? 

2. The `covers-consumer` is using a logical name to discover a reference to the `covers-service` using the Service Registry.

````java
URI uri = URI.create("//COVERS-SERVICE/covers");
````

This is standard Spring Cloud Registry functionality. What's neat about this is that the microservices can automatically register themselves when they start without the need for any complicated configuration other than the `@EnableDiscoveryClient` annotation. The registry server details come from the cloud environment properties that PCF provides as part of the service registry service binding.

3. There is a circuit breaker protecting the service calls between the `covers-consumer` and the `covers-service`. This circuit breaker is triggered to break from time to time (as you can see if you open the code from the `CoversService.java` class).

````java
@HystrixCommand(fallbackMethod = "reliable")
public String getCovers() {...}
````

  The method 'reliable()' contsins a simple fallback that returns `No Covers`
  
````java
public String reliable() {
    return "No Cover";
}
````

4. The services are configured to use Sleuth to add trace information to their log messages.

````bash
INFO [covers-service,2850d9e30f986e35,3e79aa27ebf9f9e3,true] 13 --- [io-8080-exec-10]
````

Sleuth Stream is configured to send these messages to the Zipkin server via the RabbitMQ messaging system. This allows Zipkin to paint a clear overarching picture of all the service calls between all the microservices in the project.

## About the Author

[Ben Wilcock](https://uk.linkedin.com/in/benwilcock) works for Pivotal as a Cloud Solutions Architect. Ben has a passion for microservices, cloud and mobile applications and helps [Pivotal's Cloud Foundry](http://pivotal.io/platform) customers to become more responsive, innovate faster and gain greater returns from their software investments. Ben is also a respected technology [blogger](http://benwilcock.wordpress.com) who's articles have featured in [DZone](https://dzone.com/users/296242/benwilcock.html), [Java Code Geeks](https://www.javacodegeeks.com/author/ben-wilcock/), [InfoQ](https://www.infoq.com/author/Ben-Wilcock) and more.
