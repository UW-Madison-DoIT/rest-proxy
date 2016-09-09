package edu.wisc.my.restproxy.service

import edu.wisc.my.restproxy.model.ProxyAuthMethod
import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.HandlerMapping;

import com.google.common.net.HttpHeaders;

import edu.wisc.my.restproxy.KeyUtils;
import edu.wisc.my.restproxy.model.ProxyRequestContext;
import edu.wisc.my.restproxy.model.RequestBody;
import edu.wisc.my.restproxy.dao.RestProxyDao;

/**
 * Concrete implementation of {@link RestProxyService}.
 *
 * @author Nicholas Blair
 */
@CompileStatic
@Service
public class RestProxyServiceImpl implements RestProxyService {

  @Autowired
  private Environment env;
  @Autowired
  private RestProxyDao proxyDao;
  private static final Logger logger = LoggerFactory.getLogger(RestProxyServiceImpl.class);

  /**
   * Visible for testing.
   *
   * @param env the env to set
   */
  void setEnv(Environment env) {
    this.env = env;
  }
  /**
   * {@inheritDoc}
   *
   * Inspects the {@link Environment} for necessary properties about the target API:
   * <ul>
   * <li>Resource root URI</li>
   * <li>Credentials</li>
   * </ul>
   *
   * Delegates to {@link RestProxyDao#proxyRequest(ProxyRequestContext)}
   *
   * @see KeyUtils#getProxyHeaders(Environment, String, HttpServletRequest)
   */
  @Override
  public ResponseEntity<Object> proxyRequest(final String resourceKey, final HttpServletRequest request) {
    final String resourceRoot = env.getProperty(resourceKey + ".uri");
    if(StringUtils.isBlank(resourceRoot)) {
      logger.info("unknown resourceKey {}", resourceKey);
      return null;
    }
    StringBuilder uri = new StringBuilder(resourceRoot);

    String resourcePath = (String) request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE );
    if(StringUtils.isNotBlank(resourcePath)) {
      if(resourcePath.startsWith("/"+resourceKey)) {
        resourcePath = resourcePath.replaceFirst("/"+resourceKey, "");
      }
      if(!StringUtils.endsWith(uri, "/") && StringUtils.isNotBlank(resourcePath) && !resourcePath.startsWith("/")) {
        uri.append("/");
      }
      uri.append(resourcePath);
    }

    if(StringUtils.isNotBlank(request.getQueryString())) {
      uri.append("?");
      uri.append(URLDecoder.decode(request.getQueryString()));
    }

    String authType = env.getProperty("${resourceKey}.auth");
    ProxyAuthMethod am = authType ? ProxyAuthMethod.valueOf(authType) :  ProxyAuthMethod.NONE;

    String username = env.getProperty("${resourceKey}.username");
    String password = env.getProperty("${resourceKey}.password");

    ProxyRequestContext context = new ProxyRequestContext(resourceKey)
      .setAttributes(KeyUtils.getHeaders(env, request, resourceKey))
      .setHttpMethod(HttpMethod.valueOf(request.getMethod()))
      .setPassword(password)
      .setHeaders(KeyUtils.getProxyHeaders(env, resourceKey, request))
      .setUri(uri.toString())
      .setUsername(username)
      .setAuthMethod(am);

      if(am == ProxyAuthMethod.JWT) {
        context.getJwtDetails().setId(Long.toString(System.currentTimeMillis()));
        context.getJwtDetails().setGrantType(env.getProperty("${resourceKey}.jwt.granttype"));
        context.getJwtDetails().setIssuer(env.getProperty("${resourceKey}.jwt.issuer"));
        context.getJwtDetails().setSubject(env.getProperty("${resourceKey}.jwt.subject"));
        context.getJwtDetails().setSecret(env.getProperty("${resourceKey}.jwt.secret"));
        context.getJwtDetails().setTtl(env.getProperty("${resourceKey}.jwt.ttl"));
        context.getJwtDetails().setTokenURL(env.getProperty("${resourceKey}.jwt.tokenurl"));
      }

    RequestBody requestBody;
    try {
      InputStream inputStream = request.getInputStream();
      final String contentType = request.getContentType();
      int contentLength = request.getContentLength();
      if(inputStream != null && contentLength > 0) {
        requestBody = new RequestBody()
          .setBody(FileCopyUtils.copyToByteArray(inputStream));

        if(StringUtils.isNotBlank(contentType)) {
          requestBody.setContentType(contentType);
        }

        context.setRequestBody(requestBody)
          .getHeaders().put(HttpHeaders.CONTENT_TYPE, contentType);
      }
    } catch (IOException e) {
      logger.debug("caught IOException attempting to check request inputStream, no requestBody provided", e);
    }

    logger.debug("proxying request {}", context);
    return proxyDao.proxyRequest(context);
  }
}
