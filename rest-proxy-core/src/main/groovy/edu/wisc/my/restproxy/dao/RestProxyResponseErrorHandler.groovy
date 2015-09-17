/**
 *
 */
package edu.wisc.my.restproxy.dao;

import java.io.IOException;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * {@link ResponseErrorHandler} implementation that does nothing and considers every
 * {@link ClientHttpRequest} a success.
 *
 * This class does nothing because RestProxy is responsible solely for relaying requests and
 * responses. We don't care what's in the response, we just forward it on. It's up to the client to deal
 * with responses, whether they're successes or errors.
 *
 * @author Collin Cudd
 */
public class RestProxyResponseErrorHandler implements ResponseErrorHandler {

  /*
   * (non-Javadoc)
   *
   * @see
   * org.springframework.web.client.ResponseErrorHandler#handleError(org.springframework.http.client
   * .ClientHttpResponse)
   */
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    //no-op
  }

  /* (non-Javadoc)
   * @see org.springframework.web.client.ResponseErrorHandler#hasError(org.springframework.http.client.ClientHttpResponse)
   */
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return false;
  }
}
