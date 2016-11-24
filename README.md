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

When the Spring Cloud Services services have been provisioned your output form this command should look similar to that shown below. Services called `breaker`, `registry` and `config` have been added to your developer space in PCF.

````
name       service                       plan       bound apps                        last operation
breaker    p-circuit-breaker-dashboard   standard   covers-consumer                   create succeeded
registry   p-service-registry            standard   covers-consumer, covers-service   create succeeded
config     p-config-server               standard   covers-service                    create succeeded
````

4. You can now build and push the demo Spring Boot microservices to PCF as follows.

````
$ ./gradlew assemble
$ cf push
````

> Both the `gradlew` command and a `manifest.yml` describing the microservices has been provided.

Once the apps have been deployed, you're ready to test them.

## Using the Spring Cloud Services

To see the dashboards, login to PCF AppsManager and find the service that you want to inspect. Click the `Manage` link at the top of the service's details page. For the Circuit Breaker service you'll see a Hystrix dashboard. For the Service Registy you'll see a Eureka dashboard.

To test the apps you can use curl to send some requests to the `covers-consumer` microservice using the following command. You may prefer to try the JMeter project I've included to automate the sending of requests.

````
$ while true; do sleep 1; curl covers-consumer.local.pcfdev.io/mycovers; echo -e '\n'$(date); done
````

When everything is working (the `covers-consumer` can see and talk to the `covers-service`) you'll see...

````
no cover, auto cover, home cover, duvet cover
Thu 24 Nov 2016 10:58:15 GMT
````

The `covers-consumer` finds the `covers-service` using the *Service Registry*. At compile time it has no clue where the `covers-service` will be located, only it's logical name. You can see this in the code.

The output above showing the types of insurance cover available (no cover, auto cover, home cover, duvet cover) is a configuration property that comes from the *Config Service*. The config service is getting this from a repository on Github.com [here](https://github.com/benwilcock/app-config/blob/master/covers-service.yml).

In order to demonstrate the *Circuit Breaker*, there is a purposely engineered exception that will occaisionally cause the circuit breaker to trip. When the fault occurs you'll see...

````
No Cover
Thu 24 Nov 2016 10:58:14 GMT
````

This is the `covers-consumer` using a fallback because it thinks the `covers-service` is unavailable.



## About the Author

[Ben Wilcock](https://uk.linkedin.com/in/benwilcock) works for Pivotal as a Cloud Solutions Architect. Ben has a passion for microservices, cloud and mobile applications and helps [Pivotal's Cloud Foundry](http://pivotal.io/platform) customers to become more responsive, innovate faster and gain greater returns from their software investments. Ben is also a respected technology [blogger](http://benwilcock.wordpress.com) who's articles have featured in [DZone](https://dzone.com/users/296242/benwilcock.html), [Java Code Geeks](https://www.javacodegeeks.com/author/ben-wilcock/), [InfoQ](https://www.infoq.com/author/Ben-Wilcock) and more.
