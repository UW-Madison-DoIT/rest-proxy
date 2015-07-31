/**
 * 
 */
package edu.wisc.my.restproxy.service;

import javax.servlet.http.HttpServletRequest;

import edu.wisc.my.restproxy.ProxyRequestContext;

/**
 * Service interface for proxying a REST API.
 * 
 * @see ProxyRequestContext
 * @author Nicholas Blair
 */
public interface RestProxyService {

  /**
   * 
   * @param resourceKey
   * @param request
   * @return the Object returned from the REST API, serialized to an {@link Object} (may return null)
   */
  public Object proxyRequest(String resourceKey, HttpServletRequest request);
}
