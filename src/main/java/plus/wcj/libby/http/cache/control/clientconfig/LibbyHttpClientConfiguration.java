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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
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
import java.io.IOException;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/29
 */
@EnableConfigurationProperties(LibbyControlProperties.class)
@AutoConfigureBefore(HttpClientConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.httpclientfactories.apache.enabled", matchIfMissing = true)
@ConditionalOnClass(value = HttpClient.class, name = "org.apache.http.impl.client.cache.CachingHttpClientBuilder")
public class LibbyHttpClientConfiguration {



    @Bean
    @ConditionalOnMissingBean
    public HttpClientBuilder apacheHttpClientBuilder(CacheConfig cacheConfig, LibbyControlProperties libbyControlProperties) throws IOException {

        CachingHttpClientBuilder builder = CachingHttpClientBuilder.create()
                                                                   .setCacheConfig(cacheConfig);

        LibbyControlProperties.Httpclient httpclient = libbyControlProperties.getHttpclient();
        if (LibbyControlProperties.CacheType.FILE.equals(httpclient.getCacheType())) {
            File file = new File(httpclient.getCacheDirectory());
            file.mkdirs();
            builder.setCacheDir(file);
        }
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheConfig cacheConfig(LibbyControlProperties libbyControlProperties) {
        LibbyControlProperties.Httpclient httpclient = libbyControlProperties.getHttpclient();
        return CacheConfig.custom()
                          .setMaxCacheEntries(httpclient.getMaxCacheEntries())
                          .setMaxObjectSize(httpclient.getMaxObjectSize())
                          .build();
    }

}
