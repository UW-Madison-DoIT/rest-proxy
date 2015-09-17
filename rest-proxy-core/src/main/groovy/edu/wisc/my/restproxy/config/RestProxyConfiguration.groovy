package edu.wisc.my.restproxy.config

import groovy.transform.CompileStatic;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link Configuration} activating necessary REST proxy services.
 *
 * To use this class, simply {@link Import} it with the rest of your configuration.
 * It's strongly suggested that your configuration provide a {@link org.springframework.web.client.RestTemplate} bean, but not required.
 *
 * @author Nicholas Blair
 */
@CompileStatic
@ComponentScan(value = ["edu.wisc.my.restproxy.dao", "edu.wisc.my.restproxy.service", "edu.wisc.my.restproxy.web"])
@Configuration
public class RestProxyConfiguration {

}
