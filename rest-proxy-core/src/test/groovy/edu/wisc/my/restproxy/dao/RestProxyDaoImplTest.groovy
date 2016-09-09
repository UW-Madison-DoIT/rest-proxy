package edu.wisc.my.restproxy.dao

import edu.wisc.my.restproxy.JWTUtils
import edu.wisc.my.restproxy.model.JWTDetails
import edu.wisc.my.restproxy.model.ProxyAuthMethod
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus

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
import edu.wisc.my.restproxy.model.ProxyRequestContext

import static org.mockito.Mockito.when;

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
  protected ProxyRequestContext getContext(boolean generateJWT) {
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

    if(generateJWT) {
      context.getJwtDetails()
      .setGrantType("someGrantType")
      .setTokenURL("https://example.com/tokengetter")
      .setTtl("1000")
      .setSecret("SHHHHH")
      .setId("10048")
      .setIssuer("Tim")
      .setSubject("Tim");

      context.setAuthMethod(ProxyAuthMethod.JWT);
    }

    return context;
  }

  /**
   * Control test for {@link RestProxyDaoImpl#proxyRequest(ProxyRequestContext)}
   */
  @Test
  public void proxyRequest_control() {
    ProxyRequestContext context = getContext(false);
    context.setAuthMethod(ProxyAuthMethod.BASIC);
    HttpHeaders headers = new HttpHeaders();
    StringBuffer credsBuffer = new StringBuffer(context.getUsername());
    credsBuffer.append(":");
    credsBuffer.append(context.getPassword());
    String creds = credsBuffer.toString();
    byte[] base64CredsBytes = Base64.encodeBase64(creds.getBytes());
    String base64Creds = new String(base64CredsBytes);
    headers.add("Authorization", "Basic " + base64Creds);

    HttpEntity<String> expectedRequest = new HttpEntity<String>(headers);
    when(
        restTemplate.exchange(
            Matchers.eq(context.getUri()),
            Matchers.eq(context.getHttpMethod()),
            Matchers.eq(expectedRequest),
            (Class)Matchers.anyObject(),
            Matchers.eq(context.getAttributes())
        )
    ).thenReturn(expectedResponse);

    Object proxyResponse = proxyDao.proxyRequest(context);
    Mockito.verify(restTemplate).exchange(
        Matchers.eq(context.getUri()),
        Matchers.eq(context.getHttpMethod()),
        Matchers.eq(expectedRequest),
        (Class)Matchers.anyObject(),
        Matchers.eq(context.getAttributes())
    );
    assertEquals(expectedResponse, proxyResponse);
  }

  @Test
  public void proxyRequest_jwtFlowTest() {
    ProxyRequestContext context = getContext(true);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer 1f0af717251950dbd4d73154fdf0a474a5c5119adad999683f5b450c460726aa");
    HttpEntity<String> expectedRequest = new HttpEntity<String>(headers);
    def jsonSlurper = new JsonSlurper();

    when(
            restTemplate.exchange(
                    Matchers.eq(context.getUri()),
                    Matchers.eq(context.getHttpMethod()),
                    Matchers.eq(expectedRequest),
                    (Class)Matchers.anyObject(),
                    Matchers.eq(context.getAttributes())
            )
    ).thenReturn(expectedResponse);

    def responseBody = jsonSlurper.parseText('''
                  {
                    "expires_in": 2591999111,
                    "access_token": "1f0af717251950dbd4d73154fdf0a474a5c5119adad999683f5b450c460726aa"
                  }
                ''');

    ResponseEntity<Object> tokenResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);
    HttpEntity<String> expectedTokenRequest = new HttpEntity<String>();
    when(
            restTemplate.postForEntity(
                    Matchers.eq(context.getJwtDetails().getTokenURL()),
                    Matchers.eq(expectedTokenRequest),
                    (Class)Matchers.anyObject(),
                    Matchers.any())
    ).thenReturn(tokenResponse)

    Object proxyResponse = proxyDao.proxyRequest(context);
    Mockito.verify(restTemplate).exchange(
            Matchers.eq(context.getUri()),
            Matchers.eq(context.getHttpMethod()),
            Matchers.eq(expectedRequest),
            (Class)Matchers.anyObject(),
            Matchers.eq(context.getAttributes())
    );
    assertEquals(expectedResponse, proxyResponse);
  }
}
