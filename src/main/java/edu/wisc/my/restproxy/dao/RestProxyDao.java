/**
 * 
 */
package edu.wisc.my.restproxy.dao;

import edu.wisc.my.restproxy.ProxyRequestContext;

/**
 * Data access interface for talking with a REST API.
 * 
 * @author Nicholas Blair
 */
public interface RestProxyDao {

  /**
   * 
   * @param proxyRequestContext
   * @return the response of the proxied request, serialized to an {@link Object}
   */
  public Object proxyRequest(ProxyRequestContext proxyRequestContext);
}
