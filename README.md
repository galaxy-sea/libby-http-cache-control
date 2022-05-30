<!-- TOC -->

- [1. HTTP cache introduction](#1-http-cache-introduction)
- [2. Getting started](#2-getting-started)
    - [2.1. maven dependency](#21-maven-dependency)
- [3. Spring MVC](#3-spring-mvc)
    - [3.1. cache-control And eTag](#31-cache-control-and-etag)
    - [3.2. spel expressions](#32-spel-expressions)
    - [3.3. properties](#33-properties)
    - [3.4. support Spring Cache Abstraction](#34-support-spring-cache-abstraction)
- [4. Spring Cloud OpenFeign clients](#4-spring-cloud-openfeign-clients)
    - [4.1. OkHttp](#41-okhttp)
    - [4.2. Apache HTTP](#42-apache-http)
    - [4.3. Apache HC5](#43-apache-hc5)

<!-- /TOC -->


[![Maven Central](https://img.shields.io/maven-central/v/plus.wcj/libby-http-cache-control?color=3498db&style=flat-square)](https://repo1.maven.org/maven2/plus/wcj/libby-http-cache-control/)
[![JDK](https://img.shields.io/badge/JDK-1.8-4343?style=flat-square)](https://openjdk.java.net/projects/jdk8/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x.x-2ecc71?style=flat-square)](https://spring.io/projects/spring-boot)
[![Spring Cloud OpenFeign](https://img.shields.io/badge/Spring%20Cloud%20OpenFeign-3.x.x-2ecc71?style=flat-square)](https://spring.io/projects/spring-cloud-openfeign#learn)
[![license](https://img.shields.io/github/license/galaxy-sea/libby-http-cache-control?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)

# 1. HTTP cache introduction

- [HTTP caching](https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching)
- [Cache-Control](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control)
- [Prevent unnecessary network requests with the HTTP Cache](https://web.dev/http-cache/)

# 2. Getting started

## 2.1. maven dependency

[![Maven Central](https://img.shields.io/maven-central/v/plus.wcj/libby-http-cache-control?style=flat-square)](https://repo1.maven.org/maven2/plus/wcj/libby-http-cache-control/)

```xml

<dependency>
    <groupId>plus.wcj</groupId>
    <artifactId>libby-http-cache-control</artifactId>
    <version>${Latest-version}</version>
</dependency>
```

# 3. Spring MVC

## 3.1. cache-control And eTag

```java

@RestController
@RequestMapping
public class CacheController {
    private Map<String, Long> data = new HashMap<>();

    @GetMapping("cache/{id}")
    @HttpCacheControl(key = "#id", value = "max-age=10, public")
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

## 3.2. spel expressions

1. @HttpCacheControl
2. @HttpETagBind

The key() fields of ``@HttpCacheControl`` and ``@HttpETagBind`` support spel expressions.
Currently, only method parameter list is supported, Return value is not supported.

## 3.3. properties

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

## 3.4. support Spring Cache Abstraction

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

# 4. Spring Cloud OpenFeign clients

| client       | support | cache type   | default cache type |
|--------------|---------|--------------|--------------------|
| OkHttp       | Yes     | disk         | disk               |
| Apache HTTP  | yes     | disk, memory | memory             |
| Apache HC5   | yes     | disk, memory | memory             |
| java.net.URL | No      | -            | -                  |

## 4.1. OkHttp

``` xml
  <dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-okhttp</artifactId>
    <version>${version}</version>
  </dependency>
```

```yaml
feign:
  okhttp:
    enabled: true
libby:
  ok-http:
    cache-directory: "./libby"
    cache-max-size: 2105131412
```

## 4.2. Apache HTTP

``` xml
  <dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
    <version>${version}</version>
  </dependency>

  <dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient-cache</artifactId>
    <version>${version}</version>
  </dependency>
```

```yaml
feign:
  httpclient:
    enabled: true
libby:
  httpclient:
    cache-type: MEMORY
    cache-directory: "./libby"
    max-object-size: 210513
    max-cache-entries: 1412

```

## 4.3. Apache HC5

``` xml
  <dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-hc5</artifactId>
    <optional>true</optional>
  </dependency>

  <dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5-cache</artifactId>
    <optional>true</optional>
  </dependency>
```

```yaml
feign:
  httpclient:
    enabled: true
libby:
  httpclient5:
    cache-type: MEMORY
    cache-directory: "./libby"
    max-object-size: 210513
    max-cache-entries: 1412

```