package edu.wisc.my.restproxy.dao

import groovy.transform.CompileStatic;
import org.springframework.http.ResponseEntity;

import edu.wisc.my.restproxy.model.ProxyRequestContext;

/**
 * Data access interface for talking with a REST API.
 *
 * @author Nicholas Blair
 */
@CompileStatic
public interface RestProxyDao {

  /**
   * @param proxyRequestContext
   * @return the {@link ResponseEntity} of the proxied request.
   */
  public ResponseEntity<Object> proxyRequest(ProxyRequestContext proxyRequestContext);
}
