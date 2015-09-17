package edu.wisc.my.restproxy

import groovy.transform.CompileStatic;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.PropertyPlaceholderHelper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@CompileStatic
public final class KeyUtils {

  private static final String END_PLACEHOLDER = "}";
  private static final String START_PLACEHOLDER = "{";

  private KeyUtils() {
  }

  private static final Logger logger = LoggerFactory.getLogger(KeyUtils.class);

  public static Map<String, String> getHeaders(Environment env, HttpServletRequest request, String key) {
    HashMap <String, String> map = new HashMap<String, String>();
    String attributes = env.getProperty(key + ".attributes");
    if(StringUtils.isNotBlank(attributes)) {
      for(String attribute : attributes.split(",")) {
        map.put(attribute, request.getHeader(attribute));
      }
    }
    return map;
  }

  /**
   * Utility method for extracting the Proxy Headers for a request.
   *
   * The configuration option '{key}.proxyHeaders' is used to specify a multi-valued list of HTTP headers to add to the
   * outbound request to '{key}.uri'.
   *
   * Example:
   * <pre>
   someservice.proxyHeaders=On-Behalf-Of: {wiscedupvi},Some-Other-Header: staticvalue
   </pre>
   *
   * Implementers can specify either static values ('Some-Other-Header: staticvalue') or use placeholders to relay
   * {@link HttpServletRequest#getAttribute(String)} values ('On-Behalf-Of: {wiscedupvi}')
   *
   * @param env
   * @param resourceKey
   * @param request
   * @return a potentially empty, but never null, {@link Multimap} of HTTP headers to add to the outbound HTTP request.
   */
  public static Multimap<String, String> getProxyHeaders(Environment env, String resourceKey, final HttpServletRequest request) {
    Multimap<String, String> headers = ArrayListMultimap.create();
    String proxyHeadersValue = env.getProperty(resourceKey + ".proxyHeaders");
    if(proxyHeadersValue != null) {
      String [] proxyHeaders = StringUtils.split(proxyHeadersValue, ",");
      for(String proxyHeader: proxyHeaders) {
        String [] tokens = StringUtils.trim(proxyHeader).split(":");
        if(tokens.length == 2) {
          PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(START_PLACEHOLDER, END_PLACEHOLDER);
          String value = helper.replacePlaceholders(tokens[1], new PropertyPlaceholderHelper.PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
              Object attribute = request.getAttribute(placeholderName);
              if(attribute != null && attribute instanceof String) {
                return (String) attribute;
              }
              logger.warn("configuration error: could not resolve placeholder for attribute {} as it's not a String, it's a {}", placeholderName, attribute != null ? attribute.getClass() : null);
              return null;
            }
          });

          value = StringUtils.trim(value);
          if(value != null && !value.startsWith(START_PLACEHOLDER) && !value.endsWith(END_PLACEHOLDER)) {
            headers.put(tokens[0], value);
          }
        } else {
          logger.warn("configuration error: can't split {} on ':', ignoring", proxyHeader);
        }
      }
    }
    return headers;
  }
}
