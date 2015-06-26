@Grab("edu.wisc.my.restproxy:json-proxy-service:1.0")

@ComponentScan("edu.wisc.my.restproxy")
@RestController
@RequestMapping("/")
class ProxyService extends edu.wisc.my.restproxy.api.GenericRestLookupController {}

