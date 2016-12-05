# PCF Spring Cloud Services Demo

This simple project demonstrates the use of Spring Cloud Services ('SCS') in PCF using Spring Boot microservice applications.

It contains two Spring Boot microservices:

 1. The `cover-client` a RESTful microservice which depends on...
 2. The `cover-service` another RESTful microservice.

Between them, these services use all three components of the Spring Cloud Services tile for Pivotal Cloud Foundry. Highlights include...

 - The `cover-client` uses the Eureka (Registry) to discover the `cover-service`.
 - The `cover-client` uses Hystrix (Service Breaker) to protect itself from the loss of the `cover-service`.
 - The `cover-service` uses Spring Cloud Config to get the list of "insurance covers" it provides.

## Installing the PCF SCS Demo

> I assume that you have already setup PCF and the CF Cli and have the Spring Cloud Services available to you in the marketplace. If you don't have PCF or don't have the necessary Spring Cloud Services installed, try using [PCF Dev](https://docs.pivotal.io/pcf-dev/) but remember to start PCF Dev with Spring Cloud Services available using `cf dev start -s scs` (you'll need at least 8GB of free RAM to do this and you may have to tweak the manifest).

1. Clone this repository into a folder. Make this folder your current folder in your terminal or command line.

2. Login to PCF and check the marketplace to make sure that the Spring Cloud Services are available to you as follows...

    ````bash
    $ cf marketplace
    
    p-circuit-breaker-dashboard   standard   Circuit Breaker Dashboard for Spring Cloud Applications
    p-config-server               standard   Config Server for Spring Cloud Applications
    p-service-registry            standard   Service Registry for Spring Cloud Applications
    ````

3. If the required SCS services are available in the marketplace, you can now go ahead and provision these services using the setup-services.sh script provided (windows users may need to add a non bash equivalent).

    ````bash
    $ ./setup-services.sh
    ````

    > If you decide not to use the script and provision them by hand, be aware that the Config server needs a JSON file containing the location of the Git repository that holds the application configuration for the microservices.

    You'll have to wait a few minutes while PCF provisions the services you've asked for. Check the status of the services periodically using the following command...
    
    ````bash
    $ cf services
    ````
    
    When the SCS services have been provisioned, your output form this command should look similar to that shown below. Note that services called `breaker`, `registry`, `rabbit` and `config` have been added to your list of services in PCF.
    
    ````bash
    name       service                       plan       bound apps                        last operation
    breaker    p-circuit-breaker-dashboard   standard   covers-consumer                   create succeeded
    registry   p-service-registry            standard   covers-consumer, covers-service,  create succeeded
    config     p-config-server               standard   covers-service                    create succeeded
    rabbit     p-rabbitmq                    standard   covers-zipkin, covers-service...  create succeeded
    ````

4. You can now build and push the demo Spring Boot microservices to PCF as follows.

    ````bash
    $ ./gradlew clean build
    $ cf push
    ````
    
    > Both a `gradlew` command for building the microservices and a `manifest.yml` describing the microservices has been provided for you.

Once the SCS services have been provisioned and the apps you build have been bound and deployed to PCF, you're ready to examine the power of their combined features with this microservice architecture. 

## Using the Microservices

To test the microservice apps you can use a simple `curl` to send requests to the `covers-consumer` microservice. The following command does this repeatedly every second... 

````bash
$ while true; do sleep 1; curl covers-consumer.local.pcfdev.io/mycovers; echo -e '\n'$(date); done
````

> If you're more familiar with JMeter, you may prefer to use the JMeter project I've included in the root folder to automate the sending of these requests (but remember to reconfigure the JMeter project to use your PCF endpoint URI's).

When everything is working (i.e. when the `covers-consumer` microservice can see and talk to the `covers-service` microservice) you'll see output similar to that shown below...

````bash
No Cover, Auto Cover, Home Cover, Duvet Cover
Thu 24 Nov 2016 10:58:15 GMT
````

The types of insurance cover available (No Cover, Auto Cover, Home Cover, Duvet Cover) comes from a configuration property sourced from the SCS *Config Service*.

There is a random exception that will occasionally cause the circuit breaker to trip. When the fault occurs you'll see...

````
No Cover
Thu 24 Nov 2016 10:58:14 GMT
````

This is the `covers-consumer` using a fallback method to provide a reduced set of covers because it thinks the `covers-service` is unavailable.

## Demo Highlights

1. The config of the microservices is fully externalised (12factor style) and can be refreshed at any time without having to restart the application. If the config changes in Git [here](https://github.com/benwilcock/app-config/blob/master/covers-service.yml), the changes can be applied while the service is still running simply by calling the `/refresh` endpoint. This endpoint is automatically added by Spring Boot.

    ````bash
    $ curl -X POST covers-service.<your-pcf-domain-name>/refresh
    ````

    This is standard Spring Cloud Config functionality. What's special about this is that if you have 100 running service instances, a single POST to `/refresh` on just one instance is all you need to change the configuration for all running instances without restarting. Pretty neat hey? 

2. The location of other services is discovered via a registry of logical names. As an example of this, the `covers-consumer` is using a logical name to discover a reference to the `covers-service` using the registry. There are no hard-coded endpoints.

    ````java
    URI uri = URI.create("//COVERS-SERVICE/covers");
    ````

    This is standard Spring Cloud Registry functionality. What's also cool about this is that the microservices automatically register themselves with the registry when they boot. There is no complicated configuration other than the `@EnableDiscoveryClient` annotation in the application class. The registry server's contact details come from the `environment properties` that PCF automatically provides at startup as part of the service binding.

3. Service-to-service calls are resilient and have useful fallbacks that can help prevent cascading exceptions. As an example, there is a circuit breaker protecting the service calls between the `covers-consumer` and the `covers-service`. This circuit breaker is artificially triggered from time to time in order to show the breaker in operation (as you can see if you open the code from the `CoversService.java` class).

    ````java
    @HystrixCommand(fallbackMethod = "getCoversFallbackMethod")
    public String getCovers() {...}
    ````

    The method 'getCoversFallbackMethod()' contsins a simple fallback that returns `No Covers`
  
    ````java
    public String getCoversFallbackMethod() {
        return "No Cover";
    }
    ````

4. Both of the microservices are configured to use Sleuth. When on the classpath, Sleuth automatically adds trace information to the log messages of Spring Boot applications...

    ````bash
    INFO [covers-service,2850d9e30f986e35,3e79aa27ebf9f9e3,true] 13 --- [io-8080-exec-10]
    ````

    Sleuth is configured to send these messages to the Zipkin server via a RabbitMQ based message stream. This allows the Zipkin server to paint a clear and overarching picture of all the service calls between all the microservices in the project. It can even diagram these interactions for you to aid your understanding your microservice communications. 
    

## Extra Credit - The Server Consoles

When you provisioned the services, SCS made the following UI consoles available to you... 

 - RabbitMQ (RabbitMQ Console)
 - Service Registry (Eureka Dashboard)
 - Circuit Breaker (Hystrix Dashboard)

You can get to these consoles by following the `Services(x) -> [Your Service Name] > Manage` link in the PCF Apps Manager application.

Zipkin is not part of the SCS tile, but it also provides a UI for searching for traces which you can get to by opening the app in your browser using the link provided in the PCF Apps Manager.

## About the Author

[Ben Wilcock](https://uk.linkedin.com/in/benwilcock) works for Pivotal as a Cloud Solutions Architect. Ben has a passion for microservices, cloud and mobile applications and helps [Pivotal's Cloud Foundry](http://pivotal.io/platform) customers to become more responsive, innovate faster and gain greater returns from their software investments. Ben is also a respected technology [blogger](http://benwilcock.wordpress.com) who's articles have featured in [DZone](https://dzone.com/users/296242/benwilcock.html), [Java Code Geeks](https://www.javacodegeeks.com/author/ben-wilcock/), [InfoQ](https://www.infoq.com/author/Ben-Wilcock) and more.
