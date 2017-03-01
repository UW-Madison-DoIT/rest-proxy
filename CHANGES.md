This changelog documents recent `rest-proxy` releases.

Note that these releases 

+ are *not* modeled as GitHub releases, and 
+ are *not* modeled as tags in Git
+ *are* [in Maven Central][edu.wisc.my.restproxy group in Maven Central]

[![SVG badge indicating latest version in Maven Central](https://img.shields.io/maven-central/v/edu.wisc.my.restproxy/rest-proxy-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22rest-proxy-core%22)

`rest-proxy` practices Semantic Versioning, at least as of recent releases.

# v3 series

Upgrading: 

The v3 series differs from the v2 series in prefixing proxy access with `/proxy/` (to create namespace for other functionality at paths other than `/proxy/`). This is a breaking change in that all usages of your `rest-proxy` instances will need to add a `/proxy/` to the URLs they are requesting across this upgrade.

## 3.2.0

Adds the ability to proxy any content type, rather than just application/json and a few other types.

## 3.1.0

Adds a health check for each key.

`/report/{key}`

yields

```JSON
{
   "uwmadisonreddit":{
     "key": "uwmadisonreddit",
     "lastResultMap":{
       "429": 1473713497451
     }
   }
}
```

( #53 )

## 3.0.2

Bugfix relating to the introduction of `/proxy/` to URL paths in v3.0.0.

( #52 , resolving #51 )

## 3.0.1

Namespaced the path to keyed proxies with a preceding `/proxy/` .

That is, `/{key}/*` became `/proxy/{key}/*` .

This freed namespace for health check endpoints.

#v2 series

This series ended with v2.2.0.

## 2.2.0

Added JWT to methods available for `rest-proxy` to authenticate to proxied resources.

## 2.1.4

Tweaked dependency declarations to stop needlessly depending upon 

+ UW-Madison local Maven repositories
+ snapshots

Tweaked Travis-CI build to run faster by caching dependencies.

## 2.1.3 

+ Upgraded dependencies
+ Cleaned away legacy Spring Boot configuration

# Earlier releases

[Releases on GitHub](https://github.com/UW-Madison-DoIT/rest-proxy/releases) represent these.


[edu.wisc.my.restproxy group in Maven Central]: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22edu.wisc.my.restproxy%22