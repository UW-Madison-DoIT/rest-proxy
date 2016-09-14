package edu.wisc.my.restproxy.web

import edu.wisc.my.restproxy.service.ReportService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@Controller
class ReportController {
  @Autowired
  private ReportService reportService;

  @RequestMapping("/report/{key}")
  public @ResponseBody Object singleReport(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable String key) {
    return reportService.getReport(key);
  }

  @RequestMapping("/report")
  public @ResponseBody Object allReports(HttpServletRequest request,
                                            HttpServletResponse response) {
    return reportService.getAllReports();
  }
}
