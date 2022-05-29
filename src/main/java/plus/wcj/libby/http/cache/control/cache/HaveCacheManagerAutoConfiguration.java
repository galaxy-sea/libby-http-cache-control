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
package plus.wcj.libby.http.cache.control.cache;

import plus.wcj.libby.http.cache.control.sequence.Sequence;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/21
 */
public class HaveCacheManagerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "httpETagCache")
    @ConditionalOnBean(name = {"cacheManager"})
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    public HttpETagCache httpETagCache(CacheManager cacheManager, Sequence sequence) {
        return new HttpETagCache(cacheManager, sequence);
    }

}
