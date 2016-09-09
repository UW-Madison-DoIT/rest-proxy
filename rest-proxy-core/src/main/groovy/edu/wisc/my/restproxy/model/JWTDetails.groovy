package edu.wisc.my.restproxy.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

@Builder(builderStrategy = SimpleStrategy)
@Canonical
@CompileStatic
public class JWTDetails {
  String id, subject, issuer, secret, ttl, tokenURL, grantType;


  @Override
  public String toString() {
    return "JWTDetails{" +
            "id='" + id + '\'' +
            ", subject='" + subject + '\'' +
            ", issuer='" + issuer + '\'' +
            ", secret='" + secret + '\'' +
            ", ttl='" + ttl + '\'' +
            ", tokenURL='" + tokenURL + '\'' +
            ", grantType='" + grantType + '\'' +
            '}';
  }

  boolean isValid() {
    return id && subject && issuer && secret && ttl && tokenURL && grantType;
  }
}
