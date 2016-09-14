package edu.wisc.my.restproxy.service

import edu.wisc.my.restproxy.model.ProxyReport

/**
 *  Report Service provides a service to log and retrieve information about a key
 */
interface ReportService {
  /**
   * get a single report about a specific key
   * @param key the key in question
   * @return the proxy report (if one exists, can return null)
   */
  public ProxyReport getReport(String key);
  /**
   * Get the master report for all the keys
   * @return a <key, report> hash map
   */
  public Map<String, ProxyReport> getAllReports();
  /**
   * Manipulate the map
   * @param key the key to log under
   * @param status the return status
   * @param epochTime the time this request happened
   */
  public void logReport(String key, String status, long epochTime);
}
