package edu.wisc.my.restproxy.web

import edu.wisc.my.restproxy.service.ReportService
import groovy.transform.CompileStatic;

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
@CompileStatic
@Controller
public class ResourceProxyController {

  @Autowired
  private RestProxyService proxyService;
  @Autowired
  private ReportService reportService;
  @Autowired
  private Environment env;

  /**
   * @param env the env to set
   */
  void setEnv(Environment env) {
    this.env = env;
  }

  /**
   * Proxies the request and then calls {@link HttpServletResponse#setStatus(int)} with the
   * {@link HttpStatus} recieved. If the proxy response contains content it's simply returned here
   * as an {@link Object}.
   *
   * @param request
   * @param response
   * @param key
   * @return the body of the proxy response or null.
   */
  @RequestMapping("/proxy/{key}/**")
  public @ResponseBody Object proxyResource(HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable String key) {
    ResponseEntity<Object> responseEntity = proxyService.proxyRequest(key, request);
    if(responseEntity == null || responseEntity.getStatusCode() == null) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      reportService.logReport(key, HttpStatus.NOT_FOUND.toString(), System.currentTimeMillis());
      return null;
    }
    response.setStatus(responseEntity.getStatusCode().value());
    reportService.logReport(key, responseEntity.getStatusCode().toString(), System.currentTimeMillis());
    return responseEntity.hasBody() ? responseEntity.getBody() : null;
  }
}
