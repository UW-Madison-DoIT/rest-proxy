# json-proxy-service
A Simple server side JSON proxy service written in Java

### Setup
+ Configure your `Spring application context` to collect annotation beans from edu.wisc.my.util
```xml
    <mvc:annotation-driven/>
    <context:component-scan base-package="edu.wisc.my.restproxy"/>
```
+ Setup a servlet 
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
