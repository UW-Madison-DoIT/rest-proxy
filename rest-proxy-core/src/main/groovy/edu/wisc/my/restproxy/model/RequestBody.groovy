package edu.wisc.my.restproxy.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy;

@Builder(builderStrategy = SimpleStrategy)
@Canonical
@CompileStatic
public class RequestBody {

  byte[] body;
  String contentType;

}
