package edu.wisc.my.restproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.collect.Multimap;

/**
 * Tests for {@link KeyUtils}.
 *
 * @author Nicholas Blair
 */
public class KeyUtilsTest {

  /**
   * Verify stable behavior for {@link KeyUtils#getProxyHeaders(org.springframework.core.env.Environment, String, HttpServletRequest)}
   * when no proxyHeaders configuration found in the {@link Environment}
   */
  @Test
  public void getProxyHeaders_empty() {
    assertTrue(KeyUtils.getProxyHeaders(new MockEnvironment(), "empty", new MockHttpServletRequest()).isEmpty());
  }
  /**
   * Control experiment for {@link KeyUtils#getProxyHeaders(org.springframework.core.env.Environment, String, HttpServletRequest)}
   * for simple proxyHeaders configuration: 1 static value.
   */
  @Test
  public void getProxyHeaders_control() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("control.proxyHeaders", "foo: bar");
    Multimap<String, String> proxyHeaders = KeyUtils.getProxyHeaders(env, "control", new MockHttpServletRequest());
    assertEquals(1, proxyHeaders.get("foo").size());
    assertEquals("bar", proxyHeaders.get("foo").iterator().next());
  }
  /**
   * Experiment for {@link KeyUtils#getProxyHeaders(org.springframework.core.env.Environment, String, HttpServletRequest)}
   * for proxyHeaders configuration with a placeholder.
   */
  @Test
  public void getProxyHeaders_placeholder() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("placeholder.proxyHeaders", "foo: {bar}");
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("bar", "banana");
    Multimap<String, String> proxyHeaders = KeyUtils.getProxyHeaders(env, "placeholder", request);
    assertEquals(1, proxyHeaders.get("foo").size());
    assertEquals("banana", proxyHeaders.get("foo").iterator().next());
  }
  /**
   * Experiment for {@link KeyUtils#getProxyHeaders(org.springframework.core.env.Environment, String, HttpServletRequest)}
   * for proxyHeaders configuration with multiple HTTP headers: 1 placeholder, 1 static, and 1 unresolved.
   */
  @Test
  public void getProxyHeaders_multiple() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("multiple.proxyHeaders", "foo: {bar}, one: two, unresolved: {notthere}");
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setAttribute("bar", "apple");
    Multimap<String, String> proxyHeaders = KeyUtils.getProxyHeaders(env, "multiple", request);
    assertEquals("apple", proxyHeaders.get("foo").iterator().next());
    assertEquals("two", proxyHeaders.get("one").iterator().next());

    assertTrue(proxyHeaders.get("unresolved").isEmpty());
  }
}
