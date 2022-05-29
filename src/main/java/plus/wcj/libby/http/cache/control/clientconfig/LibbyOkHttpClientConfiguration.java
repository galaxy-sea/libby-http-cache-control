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
package plus.wcj.libby.http.cache.control.clientconfig;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import plus.wcj.libby.http.cache.control.LibbyControlProperties;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/28
 */
@EnableConfigurationProperties(LibbyControlProperties.class)
@ConditionalOnProperty(name = "spring.cloud.httpclientfactories.ok.enabled", matchIfMissing = true)
@ConditionalOnClass(OkHttpClient.class)
@AutoConfigureBefore(HttpClientConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class LibbyOkHttpClientConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient.Builder okHttpClientBuilder(Cache okhttp3Cache) {
        return new OkHttpClient.Builder().cache(okhttp3Cache);
    }

    @Bean
    @ConditionalOnMissingBean
    public Cache okhttp3Cache(LibbyControlProperties libbyControlProperties) {
        return new Cache(new File(libbyControlProperties.getHttpClientCachePath()), libbyControlProperties.getHttpClientCacheMaxSize());
    }


}
