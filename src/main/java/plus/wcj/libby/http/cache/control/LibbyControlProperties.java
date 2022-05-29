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

    private String httpClientCachePath = "./libby";

    private long httpClientCacheMaxSize = 2105131412L;
}
