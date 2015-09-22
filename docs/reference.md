## rest-proxy Reference

### Securing your application

In order to secure your proxy, you need to have a full understanding of the security requirements of the REST API you intend to proxy and how that needs to be reflected in your application.

### Downstream REST API does not require authentication, all data open

This is the trivial case; the downstream REST API doesn't enforce any authentication or authorization, all you need is the `url` option listed below.

### Downstream REST API requires authentication, all users can see all data available

This is similar in impact to the previous example. The downstream REST API requires credentials, so part of them trusting you with those credentials implies you aren't going to have an open proxy. You'll need the `url`, `username`, and `password` options listed below, but your app is going to need to figure out how to authenticate users and require authentication for your proxy.

### Downstream REST API requires authentication, users can see only data scoped to their account

This is a fairly complex, and common, example. The downstream REST API provided you credentials that give you broad access to all the data it provides. How then will you limit users of your app to only the data they are allowed to see?

CORS is really a better approach here, but you're probably still reading about rest-proxy because some limitation prevents CORS from being applicable to your app and the downstream REST API.

You have options, none of them trivial, nor in scope for rest-proxy:

1. *You write access control in your app.*
2. *The downstream REST API supports Impersonation of some sort.* Using service account credentials will get you authenticated, relaying an additional header that the downstream REST API understands and uses to "switch users" is possible using the `proxyHeaders` with placeholders listed below.


### Configuration Options

Each target service you wish to proxy should be identified with a 'key'; this key will manifest as the prefix to the properties used to configure that proxy and will manifest in the URL to reach that proxy. Assume your app is running on localhost:8080 and the web.xml binds the path '/proxy' to the RestProxyController.

All proxied URLs will replicate the HTTP verb used to invoke them (GET, POST, PUT, DELETE, etc) and will remit the request body as is.

#### url

**Required**, provides the base URL of the REST API you wish to proxy.

Example:
```
simple.url=http://jsonplaceholder.typicode.com/
```

Result:

* http://localhost:8080/proxy/simple/posts is a proxy for http://jsonplaceholder.typicode.com/posts

#### username, password

**Optional.** These properties are used if the proxied REST API requires HTTP Basic Auth.

Example:
```
authenticated.url=http://somewhere.wisc.edu/
authenticated.username=someuser
authenticated.password=someuser
```

Result:

* http://localhost:8080/proxy/authenticated/something is a proxy for http://somewhere.wisc.edu/something

#### proxyHeaders

**Optional.** These properties are used to append additional headers to the outgoing request to the proxied server. This property can be a single header or a comma separated list of headers. Header values can be static or replaced with request attributes.

Example:
```
withHeaders.url=http://other.wisc.edu/
withHeaders.proxyHeaders=Simple-Static-Header: foo,Placeholder-Header: ${wiscedupvi}

```

Result:

* http://localhost:8080/proxy/withHeaders/data is a proxy for http://other.wisc.edu/data, the request will include *Simple-Static-Header: foo* and *Placeholder-Header: UW000A000* (assuming the user hitting localhost:8080/proxy/withHeaders/data is authenticated and has a value of UW000A000 for their wiscedupvi).
