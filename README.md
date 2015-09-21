# rest-proxy
A Simple server side REST proxy service written in Groovy

[![Build Status](https://travis-ci.org/UW-Madison-DoIT/rest-proxy.svg)](https://travis-ci.org/UW-Madison-DoIT/rest-proxy)

### Purpose

This is an important component with a couple of use cases.

 * Picture an app in the portal providing a preview of a fuller external application. Without this component, we would need to write REST controllers in the app that are a façade to the external-to-portal REST web services (that might even be provided out of the WSO2 ESB). With this component, we can drop `rest-proxy-core` in the portal app, configure it with the ESB endpoint address for the application's REST API and credentials, and then NOT have to write any controllers! The AngularJS controllers can talk to the (same domain) URIs presented by the `rest-proxy-core` controllers, which in turn relay to the ESB and the external application.
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
        <servlet-name>rest-proxy-api</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>rest-proxy-api</servlet-name>
        <url-pattern>/api/*</url-pattern>
    </servlet-mapping>
    
```
+ Verify it works

### Run Standalone Microservice

#### Running
* Supply endpoint configuration - you can copy application.properties.example to get started, or add a command-line flag, e.g. `--todos.uri=http://jsonplaceholder.typicode.com/todos`
* Run `gradle bootRun` to start the server
* [Verify in your browser](localhost:8080/todos)

__OR__

* Run `gradle build` to build a standalone jar
* Start the server by running `java -jar rest-proxy-boot/build/rest-proxy-boot-<VERSION>.jar`

### Releasing to Artifact Repository

#### Manually
Only authorized users can perform the release.  Contact one of the core contributors if you think you should have access.

* Run `gradle uploadArchives` and provide Sonatype credentials when prompted (if they are not already supplied in ~/.gradle/gradle.properties).  This will build both projects and generate pomfiles, javadoc, test, and sources artifacts, and then upload them to the Sonatype Nexus repository.

