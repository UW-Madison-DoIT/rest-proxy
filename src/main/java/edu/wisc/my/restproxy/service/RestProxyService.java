/**
 * 
 */
package edu.wisc.my.restproxy.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

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
   * @return the {@link ResponseEntity} returned from the REST API (may return null)
   */
  public ResponseEntity<Object> proxyRequest(String resourceKey, HttpServletRequest request);
}
