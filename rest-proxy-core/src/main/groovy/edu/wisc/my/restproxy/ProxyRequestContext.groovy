package edu.wisc.my.restproxy;

import groovy.transform.EqualsAndHashCode;
import groovy.transform.CompileStatic;

import org.springframework.http.HttpMethod;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Java Bean representing all of the context about a REST Request this library is proxying.
 *
 * @author Nicholas Blair
 */
@CompileStatic
@EqualsAndHashCode
public class ProxyRequestContext {

  private final String resourceKey;
  private HttpMethod httpMethod = HttpMethod.GET;
  private String uri;
  private String username;
  private String password;
  private Map<String, String> attributes = new HashMap<>();
  private Multimap<String, String> headers = ArrayListMultimap.create();
  private RequestBody requestBody;

  /**
   *
   * @param resourceKey required
   */
  public ProxyRequestContext(String resourceKey) {
    this.resourceKey = resourceKey;
  }
  /**
   * @return the resourceKey
   */
  public String getResourceKey() {
    return resourceKey;
  }
  /**
   * Defaults to {@link HttpMethod#GET} if not set explicitly.
   *
   * @return the httpMethod
   */
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }
  /**
   * @param httpMethod the httpMethod to set
   */
  public ProxyRequestContext setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }
  /**
   * @return the the full URI (including scheme, host, port, path)
   */
  public String getUri() {
    return uri;
  }
  /**
   * @param uri the full URI (including scheme, host, port, path)
   */
  public ProxyRequestContext setUri(String uri) {
    this.uri = uri;
    return this;
  }
  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }
  /**
   * @param username the username to set
   */
  public ProxyRequestContext setUsername(String username) {
    this.username = username;
    return this;
  }
  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }
  /**
   * @param password the password to set
   */
  public ProxyRequestContext setPassword(String password) {
    this.password = password;
    return this;
  }
  /**
   * @return the attributes
   */
  public Map<String, String> getAttributes() {
    return attributes;
  }
  /**
   * @param attributes the attributes to set
   */
  public ProxyRequestContext setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
    return this;
  }
  /**
   * @return the headers
   */
  public Multimap<String, String> getHeaders() {
    return headers;
  }
  /**
   * @param headers the headers to set
   */
  public ProxyRequestContext setHeaders(Multimap<String, String> headers) {
    this.headers = headers;
    return this;
  }
  /**
   * @return the requestBody
   */
  public RequestBody getRequestBody() {
    return requestBody;
  }
  /**
   * @param requestBody the requestBody to set
   */
  public ProxyRequestContext setRequestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
    return this;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ProxyRequestContext [resourceKey=${resourceKey}, httpMethod=${httpMethod}, uri=${uri}, username=${username}, password=${password != null ? '<set, suppressed>' : 'empty'}, attributes=${attributes}, headers=${headers}]"
  }

}
