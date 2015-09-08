/**
 * 
 */
package edu.wisc.my.restproxy.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import edu.wisc.my.restproxy.ProxyRequestContext;
import edu.wisc.my.restproxy.ValidationResult;
import edu.wisc.my.restproxy.dao.RestProxyDao;

/**
 * Tests for {@link RestProxyServiceImpl}.
 * 
 * @author Nicholas Blair
 */
@RunWith(MockitoJUnitRunner.class)
public class RestProxyServiceImplTest {

  private MockEnvironment env = new MockEnvironment();
  @Mock private RestProxyDao proxyDao;
  @InjectMocks private RestProxyServiceImpl proxy = new RestProxyServiceImpl();
 
  @Before
  public void setup() {
      proxy.setEnv(env);
  }
  /**
   * Control experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}, confirms
   * expected behavior for successful, simple request.
   */
  @Test
  public void proxyRequest_control() {
    final ResponseEntity<Object> result = new ResponseEntity<Object>(new Object(), HttpStatus.OK);
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("GET");
    request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/control/foo");
    env.setProperty("control.uri", "http://destination");

    //note the resourceKey ('control' in this context) is stripped from the uri
    ProxyRequestContext expected = new ProxyRequestContext("control").setUri("http://destination/foo");
    
    when(proxyDao.proxyRequest(expected)).thenReturn(result);
    assertEquals(result, proxy.proxyRequest("control", request));
  }
  
  /**
   * Test simulates a proxy request which fails with a http 400 error.  
   * An error like this could be encountered if you were to post invalid data to a form.
   * Test verifies the HttpStatus AND the body (which likely contins validation error message) are passed back to the client.
   */
  @Test
  public void proxyRequest_failsWithBadRequest() {
    ValidationResult validationFailure = new ValidationResult(false, "you didn't check the box");
    final ResponseEntity<Object> expectedResult = new ResponseEntity<Object>(validationFailure, HttpStatus.BAD_REQUEST);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("POST");
    request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/control/foo");
    request.setContent("{ \"hello\": \"world\" }".getBytes());
    request.addHeader("Content-Type", "application/json");
    env.setProperty("control.uri", "http://destination");
    when(proxyDao.proxyRequest(any(ProxyRequestContext.class))).thenReturn(expectedResult);
    ResponseEntity<Object> result = proxy.proxyRequest("control", request);
    assertEquals(expectedResult, result);
    assertEquals(validationFailure, result.getBody());
  }
  
  /**
   * Experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}, confirms
   * expected behavior when the configuration contains credentials.
   */
  @Test
  public void proxyRequest_withCredentials() {
    final ResponseEntity<Object> result = new ResponseEntity<Object>(new Object(), HttpStatus.OK);
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("GET");
    env.setProperty("withCredentials.uri", "http://localhost/foo");
    env.setProperty("withCredentials.username", "user");
    env.setProperty("withCredentials.password", "pass");
    ProxyRequestContext expected = new ProxyRequestContext("withCredentials").setUri("http://localhost/foo")
        .setUsername("user").setPassword("pass");
    
    when(proxyDao.proxyRequest(expected)).thenReturn(result);
    assertEquals(result, proxy.proxyRequest("withCredentials", request));
  }
  
  /**
   * Experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}, confirms
   * expected behavior when the configuration contains a request for additional headers with static values.
   */
  @Test
  public void proxyRequest_withAdditionalHeader() {
    final ResponseEntity<Object> result = new ResponseEntity<Object>(new Object(), HttpStatus.OK);
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("wiscedupvi", "UW111A111");
    request.setMethod("GET");
    env.setProperty("withAdditionalHeaders.uri", "http://localhost/foo");
    env.setProperty("withAdditionalHeaders.proxyHeaders", "Some-Header: staticvalue");
    ProxyRequestContext expected = new ProxyRequestContext("withAdditionalHeaders").setUri("http://localhost/foo");
    expected.getHeaders().put("Some-Header", "staticvalue");
    
    when(proxyDao.proxyRequest(expected)).thenReturn(result);
    assertEquals(result, proxy.proxyRequest("withAdditionalHeaders", request));
  }
  /**
   * Experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}, confirms
   * expected behavior when the configuration contains a request for additional headers, the values
   * containing placeholders.
   */
  @Test
  public void proxyRequest_withAdditionalHeaders_andPlaceholders() {
    final ResponseEntity<Object> result = new ResponseEntity<Object>(new Object(), HttpStatus.OK);
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("wiscedupvi", "UW111A111");
    request.setMethod("GET");
    env.setProperty("withAdditionalHeaders2.uri", "http://localhost/foo");
    env.setProperty("withAdditionalHeaders2.proxyHeaders", "On-Behalf-Of: {wiscedupvi}");
    ProxyRequestContext expected = new ProxyRequestContext("withAdditionalHeaders2").setUri("http://localhost/foo");
    expected.getHeaders().put("On-Behalf-Of", "UW111A111");
    
    when(proxyDao.proxyRequest(expected)).thenReturn(result);
    assertEquals(result, proxy.proxyRequest("withAdditionalHeaders2", request));
  }
  
  /**
   * Experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}, confirms
   * expected behavior when the request path contains additional fragments.
   */
  @Test
  public void proxyRequest_withAdditionalPath() {
    final ResponseEntity<Object> result = new ResponseEntity<Object>(new Object(), HttpStatus.OK);
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("GET");
    request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "api/v2/employee/123");
    env.setProperty("withAdditionalPath.uri", "http://localhost/foo");
    ProxyRequestContext expected = new ProxyRequestContext("withAdditionalPath").setUri("http://localhost/foo/api/v2/employee/123");
    
    when(proxyDao.proxyRequest(expected)).thenReturn(result);
    assertEquals(result, proxy.proxyRequest("withAdditionalPath", request));
  }
  /**
   * Experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}
   * where the request has a 'application/json' body.
   */
  @Test
  public void proxyRequest_withRequestBody() { 
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("PUT");
    request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/withRequestBody/foo");
    request.setContent("{ \"hello\": \"world\" }".getBytes());
    request.addHeader("Content-Type", "application/json");
    env.setProperty("withRequestBody.uri", "http://destination");

    proxy.proxyRequest("withRequestBody", request);
    verify(proxyDao).proxyRequest(any(ProxyRequestContext.class));
  }
  /**
   * Experiment for {@link RestProxyServiceImpl#proxyRequest(String, HttpServletRequest)}
   * where the request has a body but not the 'application/json' content-type.
   */
  @Test
  public void proxyRequest_withRequestBody_notjson() { 
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("PUT");
    request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/notjson/foo");
    request.setContent("<html></html>".getBytes());
    request.addHeader("Content-Type", "text/html");
    env.setProperty("notjson.uri", "http://destination");

    proxy.proxyRequest("notjson", request);
    verify(proxyDao).proxyRequest(any(ProxyRequestContext.class));
  }
}
