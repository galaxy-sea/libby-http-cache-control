<!-- TOC -->

- [HTTP cache introduction](#http-cache-introduction)
- [Getting started](#getting-started)
  - [maven dependency](#maven-dependency)
  - [cache-control And eTag](#cache-control-and-etag)
  - [spel expressions](#spel-expressions)
  - [properties](#properties)
- [support Spring Cache Abstraction](#support-spring-cache-abstraction)

<!-- /TOC -->


# HTTP cache introduction


- [HTTP caching](https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching)
- [Cache-Control](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control)
- [Prevent unnecessary network requests with the HTTP Cache](https://web.dev/http-cache/)

# Getting started

## maven dependency

```xml
<dependency>
    <groupId>plus.wcj</groupId>
    <artifactId>libby-http-cache-control</artifactId>
    <version>${Latest-version}</version>
</dependency>
```

## cache-control And eTag

```java
@RestController
@RequestMapping
public class CacheController {
    private Map<String, Long> data = new HashMap<>();

    @GetMapping("cache/{id}")
    @HttpCacheControl(key = "#id",value = "max-age=10, public")
    public Long get(@PathVariable String id) {
        return data.computeIfAbsent(id, s -> System.currentTimeMillis());
    }

    @PostMapping("cache/{id}")
    @HttpETagBind(key = "#id")
    public void post(@PathVariable String id) {
        data.put(id, System.currentTimeMillis());
    }

    @GetMapping("html")
    public ResponseEntity<String> html() {
        return ResponseEntity
                .ok()
                .body("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><script src=\"https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js\"></script><script>$(document).ready(function(){$(\"button:nth-child(1)\").click(function(){$.get(\"./cache/1\",function(data,status){$(\"ol\").append(\"<li>cache1: data: \"+data+\"</li>\")})});$(\"button:nth-child(2)\").click(function(){$.get(\"./cache/2\",function(data,status){$(\"ol\").append(\"<li>cache2: data: \"+data+\"</li>\")})});$(\"button:nth-child(3)\").click(function(){$.post(\"./cache/1\",function(data,status){$(\"ol\").append(\"<li>cache1: modify cache1</li>\")})});$(\"button:nth-child(4)\").click(function(){$.post(\"./cache/2\",function(data,status){$(\"ol\").append(\"<li>cache2: modify cache2</li>\")})})});</script></head><body><button>get cache1</button><button>get cache2</button><button>modify cache1</button><button>modify cache2</button><ol><li>start test</li></ol></body></html>");
    }
}
```
## spel expressions

1. @HttpCacheControl
2. @HttpETagBind

The key() fields of ``@HttpCacheControl`` and ``@HttpETagBind`` support spel expressions.
Currently, only method parameter list is supported, Return value is not supported.

## properties

```yaml
libby:
  value: "max-age=210513"
  max-age: 0
  no-cache: false
  no-store: false
  must-revalidate: false
  no-transform: false
  cache-public: false
  cache-private: false
  proxy-revalidate: false
  stale-while-revalidate: 0
  stale-if-error: 0
  s-max-age: 0
```

weight sort

properties value field < properties Other fields < @HttpCacheControl value field < @HttpCacheControl Other fields

# support Spring Cache Abstraction

see [Cache Abstraction](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)

```java
@SpringBootApplication
@EnableCaching
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
```
