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


import plus.wcj.libby.http.cache.control.annotation.HttpETagBind;
import plus.wcj.libby.http.cache.control.aop.HttpAnnotationPointcutAdvisor;
import plus.wcj.libby.http.cache.control.aop.HttpETagAdvice;
import plus.wcj.libby.http.cache.control.cache.HttpETagCache;
import plus.wcj.libby.http.cache.control.sequence.Sequence;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/15
 */
@Configuration(proxyBeanMethods = false)
public class LibbyETagAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "sequence")
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    public Sequence sequence() {
        return new Sequence();
    }

    @Bean
    @ConditionalOnMissingBean(name = {"cacheManager", "httpETagCache"})
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    public HttpETagCache httpETagCache(Sequence sequence) {
        return new HttpETagCache(new ConcurrentMapCacheManager(), sequence);
    }

    @Bean
    @ConditionalOnMissingBean(name = "HttpETagPointcutAdvisor")
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor HttpETagPointcutAdvisor(HttpETagCache httpETagCache) {
        HttpETagAdvice interceptor = new HttpETagAdvice(httpETagCache);
        return new HttpAnnotationPointcutAdvisor(interceptor, HttpETagBind.class);
    }
}
