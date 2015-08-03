/**
 * 
 */
package edu.wisc.my.restproxy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
@RestController
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
  public @ResponseBody Object proxyResource(HttpServletRequest request, 
      HttpServletResponse response,
      @PathVariable String key) {
    Object result = proxyService.proxyRequest(key, request);     
    if(result == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return null;
    } else {
      return result;
    }
  }
}
