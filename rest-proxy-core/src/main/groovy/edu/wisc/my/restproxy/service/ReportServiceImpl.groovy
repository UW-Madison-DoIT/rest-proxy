package edu.wisc.my.restproxy.service

import edu.wisc.my.restproxy.model.ProxyReport
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * Report Service Implementation
 */
@Service
class ReportServiceImpl implements ReportService {

  private static Map<String, ProxyReport> reports = new HashMap<>(10);


  @Override
  ProxyReport getReport(String key) {
    return reports != null ? reports.get(key) : null;
  }

  @Override
  Map<String, ProxyReport> getAllReports() {
    return reports;
  }

  @Override
  void logReport(String key, String status, long epochTime) {
    if(reports == null) {
      return;
    }

    ProxyReport rpt = reports.get(key);
    rpt = rpt ?: new ProxyReport(key);
    rpt.getLastResultMap().put(status, epochTime);
    reports.put(key,rpt);
  }
}
