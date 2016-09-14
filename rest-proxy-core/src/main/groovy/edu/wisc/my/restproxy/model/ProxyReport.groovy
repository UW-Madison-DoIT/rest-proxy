package edu.wisc.my.restproxy.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * ProxyReport : Utilized to report back when was the last success/failure of each key
 */

@Builder(builderStrategy = SimpleStrategy)
@Canonical
@CompileStatic
public class ProxyReport {
  public ProxyReport(String key){
    this.key = key;
  }
  String key;
  Map<String, Long> lastResultMap = new HashMap<>();

  @Override
  public String toString(){
    return "[key=${key}, lastResultMap=${lastResultMap}]";
  }
}
