/**
 * 
 */
package edu.wisc.my.restproxy.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import edu.wisc.my.restproxy.service.RestProxyService;

/**
 * Tests for {@link ResourceProxyController}.
 * 
 * @author Nicholas Blair
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceProxyControllerTest {

  private MockEnvironment env = new MockEnvironment();
  @Mock private RestProxyService service;
  @InjectMocks private ResourceProxyController controller = new ResourceProxyController();
  
  @Before
  public void setup() {
      controller.setEnv(env);
  }
  /**
   * Experiment for {@link ResourceProxyController#proxyResource(HttpServletRequest, HttpServletResponse, String)}
   * confirming behavior when "key" path variable not found in environment.
   */
  @Test
  public void proxyResource_keyNotFound() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      
      assertNull(controller.proxyResource(request, response, "bogus"));
      assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }
  /**
   * Control experiment for {@link ResourceProxyController#proxyResource(HttpServletRequest, HttpServletResponse, String)}
   * confirm behavior when "key" argument contains a known key and no additional path elements underneath.
   */
  @Test
  public void proxyResource_control() {
      Object result = new Object();
      final String resourceKey = "something";
      
      HttpServletRequest request = new MockHttpServletRequest();
      when(service.proxyRequest(resourceKey, request)).thenReturn(result);
      
      env.setProperty("something.uri", "http://localhost/something");
      
      MockHttpServletResponse response = new MockHttpServletResponse();
      
      assertEquals(result, controller.proxyResource(request, response, "something"));
  }
}
