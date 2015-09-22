# rest-proxy

[![Join the chat at https://gitter.im/UW-Madison-DoIT/rest-proxy](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/UW-Madison-DoIT/rest-proxy?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
A Simple server side REST proxy service written in Groovy

[![Build Status](https://travis-ci.org/UW-Madison-DoIT/rest-proxy.svg)](https://travis-ci.org/UW-Madison-DoIT/rest-proxy)

### Purpose

This library provides one option for integrating one web application with the REST API provided by another web application.
Typically, a web application that wanted to issue XmlHttpRequests to another web application's REST API would use [CORS](https://en.wikipedia.org/wiki/Cross-origin_resource_sharing). The assumption here is that both web applications use compatible single-sign-on technologies so an authenticated user in the first is generally an authenticated user in the second.

*But what if the web application hosting the REST API doesn't support CORS?* That's where rest-proxy can help.

Without rest-proxy, imagine how you would have to integrate these two applications:

1. The application hosting the REST API will likely have separate authentication mechanism, say a "service account" using HTTP Basic/Digest.
2. In order for your web application to make REST calls, your application is going to have to have its own REST Controllers which are simply a Proxy for the other application. You'll need a proxy method/controller for every method on the target REST API you want to call.
3. Those controllers will be pretty boiler plate, binding the same URL/query pattern that the target endpoint supports, then delegating to some rest client configured with your "service account."

Items number 2 and 3 above are provided with rest-proxy.

### Requirements

1. Java 7+
2. Spring Framework


### Getting Started

1. Add the following dependency to your project:
```
<dependency>
  <groupId>edu.wisc.my.restproxy</groupId>
  <artifactId>rest-proxy-core</artifactId>
  <version>2.0.0</artifactId>
</dependency>```
2. A Spring `@Configuration` class is provided, add `@Import(edu.wisc.my.restproxy.config.RestProxyConfiguration)` on one of your existing `@Configuration` classes. If you are using Spring's XML configuration instead, add the following to your `Spring application context`:
```xml
    <mvc:annotation-driven/>
    <context:component-scan base-package="edu.wisc.my.restproxy">
       <context:exclude-filter type="regex" expression="edu.wisc.my.restproxy.config.*"/>
    </context:component-scan>
```
3. Configure a url-pattern for the Spring DispatcherServlet. If you are writing a [My UW App](https://github.com/UW-Madison-DoIT/my-app-seed/), you may not have the DispatcherServlet already configured. Add the following to web.xml:
```xml
    <servlet>
        <servlet-name>proxy</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>rest-proxy-api</servlet-name>
        <url-pattern>/proxy/*</url-pattern>
    </servlet-mapping>
```
4. Now we have to configure the proxy. rest-proxy gets its configuration about the target REST APIs you want to proxy from the Spring `@Environment`. If you have a `@PropertySource` already defined, you can add the necessary properties there, or you can choose to add a separate `@PropertySource` (it doesn't matter, they all end up in `@Environment` no matter how many `@PropertySource`s you have). The most basic configuration available is as follows:
```
target.url=http://somewhere.wisc.edu/something
```
This configuration will result in the url 'http://localhost:8080/proxy/target/foo' (assuming http://localhost:8080 is your web application's address) being a proxy for 'http://somewhere.wisc.edu/something/foo'. If 'http://somewhere.wisc.edu/something' requires service account credentials, the configuration expands to:
```
target.url=http://somewhere.wisc.edu/something
target.username=someuser
target.password=somepassword
```
5. Secure your configuration! **This is a complicated topic** and there is no simple explanation. Some ideas and samples are covered [in the reference manual](docs/reference.md).


### Quickstart an example 

The rest-proxy-boot module in this project is a Spring Boot project that includes rest-proxy-core. It is intended for demonstration purposes.

#### Running rest-proxy-boot
* Supply endpoint configuration - you can copy application.properties.example to get started, or add a command-line flag, e.g. `--todos.uri=http://jsonplaceholder.typicode.com/todos`
* Run `gradle bootRun` to start the server
* [Verify in your browser](localhost:8080/todos)

__OR__

* Run `gradle build` to build a standalone jar
* Start the server by running `java -jar rest-proxy-boot/build/rest-proxy-boot-<VERSION>.jar`

### More documentation

See [docs](docs) for more documentation about this project.
