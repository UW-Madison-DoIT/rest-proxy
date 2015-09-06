/**
 * 
 */
package edu.wisc.my.restproxy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.wisc.my.restproxy.service.RestProxyService;

/**
 * {@link RestController} for proxying other REST resources.
 * 
 * @author Nicholas Blair
 */
@Controller
public class ResourceProxyController {

  @Autowired
  private RestProxyService proxyService;
  @Autowired
  private Environment env;

  /**
   * @param env the env to set
   */
  void setEnv(Environment env) {
    this.env = env;
  }
  /**
   * 
   */
  @RequestMapping("/{key}/**")
  public  @ResponseBody Object proxyResource(HttpServletRequest request, 
      HttpServletResponse response,
      @PathVariable String key) {
    ResponseEntity<Object> responseEntity = proxyService.proxyRequest(key, request);   
    if(responseEntity != null) {
      HttpStatus statusCode = responseEntity.getStatusCode();
      response.setStatus(statusCode.value());
      if(responseEntity.hasBody()) {
        return responseEntity.getBody();
      }
    }else {
      response.setStatus(HttpStatus.NOT_FOUND.value());
    }
    return null;    
  }
}
