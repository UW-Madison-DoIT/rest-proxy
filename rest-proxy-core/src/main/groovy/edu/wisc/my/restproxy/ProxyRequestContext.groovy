package edu.wisc.my.restproxy

import groovy.transform.Canonical;
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy;
import org.springframework.http.HttpMethod;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Java Bean representing all of the context about a REST Request this library is proxying.
 *
 * @author Nicholas Blair
 */
@Builder(builderStrategy = SimpleStrategy)
@Canonical
@CompileStatic
public class ProxyRequestContext {

  final String resourceKey;
  HttpMethod httpMethod = HttpMethod.GET;
  String uri;
  String username;
  String password;
  Map<String, String> attributes = new HashMap<>();
  Multimap<String, String> headers = ArrayListMultimap.create();
  RequestBody requestBody;

  /**
   *
   * @param resourceKey required
   */
  public ProxyRequestContext(String resourceKey) {
    this.resourceKey = resourceKey;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ProxyRequestContext [resourceKey=${resourceKey}, httpMethod=${httpMethod}, uri=${uri}, username=${username}, password=${password != null ? '<set, suppressed>' : 'empty'}, attributes=${attributes}, headers=${headers}]"
  }

}
