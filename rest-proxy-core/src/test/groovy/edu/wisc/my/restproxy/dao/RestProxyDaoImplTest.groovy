/**
 *
 */
package edu.wisc.my.restproxy.dao

import org.junit.Ignore;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import edu.wisc.my.restproxy.KeyUtils;
import edu.wisc.my.restproxy.ProxyRequestContext;

/**
 * Tests for {@link RestProxyDaoImpl}.
 *
 * @author Collin Cudd
 */
@RunWith(MockitoJUnitRunner.class)
public class RestProxyDaoImplTest {

  private MockEnvironment env = new MockEnvironment();
  @Mock private RestTemplate restTemplate;
  @InjectMocks private RestProxyDaoImpl proxyDao = new RestProxyDaoImpl();

  @Mock
  ResponseEntity<Object> expectedResponse;

  /**
   * @return {@link ProxyRequestContext}.
   */
  protected ProxyRequestContext getContext() {
    String resourceKey = "proxytest";
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("GET");

    ProxyRequestContext context = new ProxyRequestContext(resourceKey)
    .setAttributes(KeyUtils.getHeaders(env, request, resourceKey))
    .setHttpMethod(HttpMethod.valueOf(request.getMethod()))
    .setPassword("foopass")
    .setHeaders(KeyUtils.getProxyHeaders(env, resourceKey, request))
    .setUri("localhost:8080/foo")
    .setUsername("foouser");

    return context;
  }

  /**
   * Control test for {@link RestProxyDaoImpl#proxyRequest(ProxyRequestContext)}
   */
  @Ignore
  @Test
  public void proxyRequest_control() {
    ProxyRequestContext context = getContext();
    HttpHeaders headers = new HttpHeaders();
    StringBuffer credsBuffer = new StringBuffer(context.getUsername());
    credsBuffer.append(":");
    credsBuffer.append(context.getPassword());
    String creds = credsBuffer.toString();
    byte[] base64CredsBytes = Base64.encodeBase64(creds.getBytes());
    String base64Creds = new String(base64CredsBytes);
    headers.add("Authorization", "Basic " + base64Creds);

    HttpEntity<String> expectedRequest = new HttpEntity<String>(headers);
    Mockito.when(
        restTemplate.exchange(
            Matchers.eq(context.getUri()),
            Matchers.eq(context.getHttpMethod()),
            Matchers.eq(expectedRequest),
            Matchers.eq(Object.class),
            Matchers.eq(context.getAttributes())
        )
    ).thenReturn(expectedResponse);

    Object proxyResponse = proxyDao.proxyRequest(context);
    Mockito.verify(restTemplate).exchange(
        Matchers.eq(context.getUri()),
        Matchers.eq(context.getHttpMethod()),
        Matchers.eq(expectedRequest),
        Matchers.eq(Object.class),
        Matchers.eq(context.getAttributes())
    );
    assertEquals(expectedResponse, proxyResponse);
    }
}
