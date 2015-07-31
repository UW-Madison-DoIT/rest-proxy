/**
 * 
 */
package edu.wisc.my.restproxy.dao;

import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.wisc.my.restproxy.ProxyRequestContext;

/**
 * {@link RestProxyDao} implementation backed by a {@link RestTemplate}.
 * 
 * A default instance is provided, but consumers are strongly recommended to configure
 * their own instance and inject.
 * 
 * @author Nicholas Blair
 */
@Service
public class RestProxyDaoImpl implements RestProxyDao {

  @Autowired(required=false)
  private RestTemplate restTemplate = new RestTemplate();
  
  private static final Logger logger = LoggerFactory.getLogger(RestProxyDaoImpl.class);
  /* (non-Javadoc)
   * @see edu.wisc.my.restproxy.dao.RestProxyDao#proxyRequest(edu.wisc.my.restproxy.ProxyRequestContext)
   */
  @Override
  public Object proxyRequest(ProxyRequestContext context) {
    HttpHeaders headers = new HttpHeaders();
    if(StringUtils.isNotBlank(context.getUsername()) && null != context.getPassword()) {
      String creds = context.getUsername() + ":" + context.getPassword();
      byte[] base64CredsBytes = Base64.encodeBase64(creds.getBytes());
      String base64Creds = new String(base64CredsBytes);
      headers.add("Authorization", "Basic " + base64Creds);
    }
    
    for(Entry<String, String> entry: context.getHeaders().entries()) {
      headers.add(entry.getKey(), entry.getValue());
    }
    
    HttpEntity<String> request = new HttpEntity<String>(headers);
    ResponseEntity<Object> response = restTemplate.exchange(context.getUri(), 
        context.getHttpMethod(), request, Object.class, context.getAttributes());
    logger.trace("completed request for {}, response= {}", context, response);
    Object responseBody = response.getBody();
    return responseBody;
  }

}
