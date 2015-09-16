# json-proxy-service
A Simple server side JSON proxy service written in Groovy

[![Build Status](https://travis-ci.org/UW-Madison-DoIT/json-proxy-service.svg)](https://travis-ci.org/UW-Madison-DoIT/json-proxy-service)

### Purpose

This is an important component with a couple of use cases.

 * Picture an app in the portal providing a preview of a fuller external application. Without this component, we would need to write REST controllers in the app that are a façade to the external-to-portal REST web services (that might even be provided out of the WSO2 ESB). With this component, we can drop `json-proxy-core` in the portal app, configure it with the ESB endpoint address for the application's REST API and credentials, and then NOT have to write any controllers! The AngularJS controllers can talk to the (same domain) URIs presented by the `json-proxy-core` controllers, which in turn relay to the ESB and the external application.
 * Picture this module deployed as a façade for ESB endpoints. `my.wisc.edu/esb/someservice/api/foo` . You could theoretically write 100% javascript apps against the proxy proxying ESB-provided JSON.

### Add Proxy Servlet to Existing Service
+ A Spring `@Configuration` class is provided, simply `@Import(edu.wisc.my.restproxy.config.RestProxyConfiguration)` on one of your existing `@Configuration` classes. If you are using Spring's XML configuration instead, add the following to your `Spring application context`:
```xml
    <mvc:annotation-driven/>
    <context:component-scan base-package="edu.wisc.my.restproxy"/>
```
+ Setup a servlet:
```xml
    <servlet>
        <servlet-name>json-proxy-api</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>json-proxy-api</servlet-name>
        <url-pattern>/api/*</url-pattern>
    </servlet-mapping>
    
```
+ Verify it works

### Run Standalone Microservice

#### Prerequisites
 * [Grails 3.0.x](https://grails.org/)

#### Running
* Run `gradle bootRun` to start the server
* [Verify it works](localhost:8080/todos.json)

__OR__

* Run `gradle build` to build a standalone jar
* Start the server by running `java -jar json-proxy-service/build/json-proxy-service-<VERSION>.jar`

