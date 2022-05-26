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

package plus.wcj.libby.http.cache.control.aop;


import plus.wcj.libby.http.cache.control.LibbyControlProperties;
import plus.wcj.libby.http.cache.control.annotation.HttpCacheControl;
import plus.wcj.libby.http.cache.control.cache.HttpETagCache;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/15
 */
public class HttpCacheControlAdvice implements AfterReturningAdvice {

    private final HttpETagCache httpETagCache;

    private final String defaultCacheControlValue;

    public HttpCacheControlAdvice(HttpETagCache httpETagCache, LibbyControlProperties libbyControlProperties) {
        this.httpETagCache = httpETagCache;
        this.defaultCacheControlValue = toCacheControlValue(libbyControlProperties);
    }


    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        HttpCacheControl httpCacheControl = method.getAnnotation(HttpCacheControl.class);
        String spelExpression = httpCacheControl.key();
        String cacheControl = toCacheControlValue(httpCacheControl);
        String key = SpelUtil.parser(method, args, spelExpression);
        String eTag = httpETagCache.getOrDefault(key);
        response.setHeader(HttpHeaders.ETAG, eTag);
        response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
    }


    private String toCacheControlValue(HttpCacheControl cacheControl) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (cacheControl.maxAge() != 0) {
            stringJoiner.add("max-age=" + cacheControl.maxAge());
        }
        if (cacheControl.noCache()) {
            stringJoiner.add("no-cache");
        }
        if (cacheControl.noStore()) {
            stringJoiner.add("no-store");
        }
        if (cacheControl.mustRevalidate()) {
            stringJoiner.add("must-revalidate");
        }
        if (cacheControl.noTransform()) {
            stringJoiner.add("no-transform");
        }
        if (cacheControl.cachePublic()) {
            stringJoiner.add("public");
        }
        if (cacheControl.cachePrivate()) {
            stringJoiner.add("private");
        }
        if (cacheControl.proxyRevalidate()) {
            stringJoiner.add("proxy-revalidate");
        }
        if (cacheControl.sMaxAge() != 0) {
            stringJoiner.add("s-maxage=" + cacheControl.sMaxAge());
        }
        if (cacheControl.staleIfError() != 0) {
            stringJoiner.add("stale-if-error=" + cacheControl.staleIfError());
        }
        if (cacheControl.staleWhileRevalidate() != 0) {
            stringJoiner.add("stale-while-revalidate=" + cacheControl.staleWhileRevalidate());
        }
        String cacheControlValue = stringJoiner.toString();

        return StringUtils.hasLength(cacheControlValue) ? cacheControlValue : StringUtils.hasLength(cacheControl.value()) ? cacheControl.value() : defaultCacheControlValue;
    }


    private String toCacheControlValue(LibbyControlProperties cacheControl) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (cacheControl.getMaxAge() != 0) {
            stringJoiner.add("max-age=" + cacheControl.getMaxAge());
        }
        if (cacheControl.isNoCache()) {
            stringJoiner.add("no-cache");
        }
        if (cacheControl.isNoStore()) {
            stringJoiner.add("no-store");
        }
        if (cacheControl.isMustRevalidate()) {
            stringJoiner.add("must-revalidate");
        }
        if (cacheControl.isNoTransform()) {
            stringJoiner.add("no-transform");
        }
        if (cacheControl.isCachePublic()) {
            stringJoiner.add("public");
        }
        if (cacheControl.isCachePrivate()) {
            stringJoiner.add("private");
        }
        if (cacheControl.isProxyRevalidate()) {
            stringJoiner.add("proxy-revalidate");
        }
        if (cacheControl.getSMaxAge() != 0) {
            stringJoiner.add("s-maxage=" + cacheControl.getSMaxAge());
        }
        if (cacheControl.getStaleIfError() != 0) {
            stringJoiner.add("stale-if-error=" + cacheControl.getStaleIfError());
        }
        if (cacheControl.getStaleWhileRevalidate() != 0) {
            stringJoiner.add("stale-while-revalidate=" + cacheControl.getStaleWhileRevalidate());
        }
        String cacheControlValue = stringJoiner.toString();

        return StringUtils.hasLength(cacheControlValue) ? cacheControlValue : cacheControl.getValue();
    }
}
