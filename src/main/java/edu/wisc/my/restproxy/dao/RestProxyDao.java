/**
 * 
 */
package edu.wisc.my.restproxy.dao;

import org.springframework.http.ResponseEntity;

import edu.wisc.my.restproxy.ProxyRequestContext;

/**
 * Data access interface for talking with a REST API.
 * 
 * @author Nicholas Blair
 */
public interface RestProxyDao {

  /**
   * @param proxyRequestContext
   * @return the {@link ResponseEntity} of the proxied request.
   */
  public ResponseEntity<Object> proxyRequest(ProxyRequestContext proxyRequestContext);
}
