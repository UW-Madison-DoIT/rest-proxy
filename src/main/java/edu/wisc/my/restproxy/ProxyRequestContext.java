/**
 * 
 */
package edu.wisc.my.restproxy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Java Bean representing all of the context about a REST Request this library is proxying.
 * 
 * @author Nicholas Blair
 */
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
    return "ProxyRequestContext [resourceKey=" + resourceKey + ", httpMethod=" + httpMethod
        + ", uri=" + uri + ", username=" + username + ", password=" + ( password != null ? "<set, suppressed>" : "empty")
        + ", attributes=" + attributes + ", headers=" + headers + "]";
  }
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
    result = prime * result + ((headers == null) ? 0 : headers.hashCode());
    result = prime * result + ((httpMethod == null) ? 0 : httpMethod.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((resourceKey == null) ? 0 : resourceKey.hashCode());
    result = prime * result + ((uri == null) ? 0 : uri.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ProxyRequestContext other = (ProxyRequestContext) obj;
    if (attributes == null) {
      if (other.attributes != null)
        return false;
    } else if (!attributes.equals(other.attributes))
      return false;
    if (headers == null) {
      if (other.headers != null)
        return false;
    } else if (!headers.equals(other.headers))
      return false;
    if (httpMethod != other.httpMethod)
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (resourceKey == null) {
      if (other.resourceKey != null)
        return false;
    } else if (!resourceKey.equals(other.resourceKey))
      return false;
    if (uri == null) {
      if (other.uri != null)
        return false;
    } else if (!uri.equals(other.uri))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }
  
}
