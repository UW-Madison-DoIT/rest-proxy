package edu.wisc.my.restproxy.dao;

import java.util.Map;

import org.springframework.http.HttpMethod;

public interface GenericRestLookupDao {

  /**
   * Gets information using basic auth
   * @param uri the URL to get the data
   * @param username The username of the authorized user
   * @param password the password of the authorized user
   * @param method the method (POST/GET/etc...)
   * @param attributes a key/val map of attributes to utilize in substitution in the URI {var} variables
   * @return return an object (basically whatever we get back)
   */
  public Object getStuffWithAuth(String uri, String username, String password, HttpMethod method,Map<String, String> attributes);
  
  /**
   * Gets information not using authorization
   * @param uri the URL to get the data
   * @param attributes a key/val map of attributes to utilize in substitution in the URI {var} variables
   * @return return an object (basically whatever we get back)
   */
  public Object getStuff(String uri, Map<String, String> attributes);
}
