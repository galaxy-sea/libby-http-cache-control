/*
 * Copyright 2021-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plus.wcj.libby.http.cache.control;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/21
 */
@Data
@ConfigurationProperties(prefix = "libby")
public class LibbyControlProperties {

    private String value = "max-age=210513";

    private long maxAge = 0;

    private boolean noCache = false;

    private boolean noStore = false;

    private boolean mustRevalidate = false;

    private boolean noTransform = false;

    private boolean cachePublic = false;

    private boolean cachePrivate = false;

    private boolean proxyRevalidate = false;

    private long staleWhileRevalidate = 0;

    private long staleIfError = 0;

    private long sMaxAge = 0;

    private OkHttp okHttp = new OkHttp();

    private Httpclient httpclient = new Httpclient();

    private Httpclient5 httpclient5 = new Httpclient5();

    @Data
    public static class OkHttp {
        /** Returns the directory where this cache stores its data. */
        private String cacheDirectory = "./libby";
        /** Returns the maximum number of bytes that this cache should use to store its data. */
        private long cacheMaxSize = 2105131412L;
    }

    @Data
    public static class Httpclient {
        private CacheType cacheType = CacheType.MEMORY;
        /** Returns the directory where this cache stores its data. */
        private String cacheDirectory = "./libby";
        /** Specifies the maximum response body bytes size that will be eligible for caching. */
        private long maxObjectSize = 210513;
        /** Sets the maximum number of cache entries the cache will retain. */
        private int maxCacheEntries = 1412;
    }

    @Data
    public static class Httpclient5 {
        private CacheType cacheType = CacheType.MEMORY;
        /** Returns the directory where this cache stores its data. */
        private String cacheDirectory = "./libby";
        /** Specifies the maximum response body bytes size that will be eligible for caching. */
        private long maxObjectSize = 210513;
        /** Sets the maximum number of cache entries the cache will retain. */
        private int maxCacheEntries = 1412;
    }

    public enum CacheType {
        /** jvm memory */
        MEMORY,
        /** os File */
        FILE;
    }

}
