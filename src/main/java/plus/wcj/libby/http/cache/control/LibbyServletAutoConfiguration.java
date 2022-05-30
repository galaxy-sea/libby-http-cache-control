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


import plus.wcj.libby.http.cache.control.annotation.HttpCacheControl;
import plus.wcj.libby.http.cache.control.aop.HttpAnnotationPointcutAdvisor;
import plus.wcj.libby.http.cache.control.aop.HttpCacheControlAdvice;
import plus.wcj.libby.http.cache.control.cache.HttpETagCache;
import plus.wcj.libby.http.cache.control.filter.HttpIfNoneMatchFilter;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/29
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LibbyControlProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class LibbyServletAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "httpCacheControlPointcutAdvisor")
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor httpCacheControlPointcutAdvisor(HttpETagCache httpETagCache, LibbyControlProperties libbyControlProperties) {
        HttpCacheControlAdvice annotationAdvice = new HttpCacheControlAdvice(httpETagCache, libbyControlProperties);
        return new HttpAnnotationPointcutAdvisor(annotationAdvice, HttpCacheControl.class);
    }

    @Bean
    @ConditionalOnMissingBean(name = "httpIfNoneMatchFilter")
    public HttpIfNoneMatchFilter httpIfNoneMatchFilter(HttpETagCache httpETagCache) {
        return new HttpIfNoneMatchFilter(httpETagCache);
    }

}
