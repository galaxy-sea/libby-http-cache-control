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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import plus.wcj.libby.http.cache.control.LibbyControlProperties;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.clientconfig.HttpClient5FeignConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/30
 */
@EnableConfigurationProperties(LibbyControlProperties.class)
@AutoConfigureBefore(HttpClient5FeignConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CloseableHttpClient.class)
@ConditionalOnClass(name = "org.apache.hc.client5.http.impl.cache.CachingHttpClients")
public class LibbyHttpClient5FeignConfiguration {

    private static final Log LOG = LogFactory.getLog(HttpClient5FeignConfiguration.class);

    private CloseableHttpClient httpClient5;

    @Bean
    @ConditionalOnMissingBean(HttpClientConnectionManager.class)
    public HttpClientConnectionManager hc5ConnectionManager(FeignHttpClientProperties httpClientProperties) {
        return PoolingHttpClientConnectionManagerBuilder.create()
                                                        .setSSLSocketFactory(httpsSSLConnectionSocketFactory(httpClientProperties.isDisableSslValidation()))
                                                        .setMaxConnTotal(httpClientProperties.getMaxConnections())
                                                        .setMaxConnPerRoute(httpClientProperties.getMaxConnectionsPerRoute())
                                                        .setConnPoolPolicy(PoolReusePolicy.valueOf(httpClientProperties.getHc5().getPoolReusePolicy().name()))
                                                        .setPoolConcurrencyPolicy(
                                                                PoolConcurrencyPolicy.valueOf(httpClientProperties.getHc5().getPoolConcurrencyPolicy().name()))
                                                        .setConnectionTimeToLive(
                                                                TimeValue.of(httpClientProperties.getTimeToLive(), httpClientProperties.getTimeToLiveUnit()))
                                                        .setDefaultSocketConfig(
                                                                SocketConfig.custom().setSoTimeout(Timeout.of(httpClientProperties.getHc5().getSocketTimeout(),
                                                                                                              httpClientProperties.getHc5().getSocketTimeoutUnit()
                                                                )).build())
                                                        .build();
    }


    @Bean
    @ConditionalOnMissingBean
    public CacheConfig cacheConfig(LibbyControlProperties libbyControlProperties) {
        LibbyControlProperties.Httpclient5 httpclient5 = libbyControlProperties.getHttpclient5();
        return CacheConfig.custom()
                          .setMaxCacheEntries(httpclient5.getMaxCacheEntries())
                          .setMaxObjectSize(httpclient5.getMaxObjectSize())
                          .build();
    }


    @Bean
    public CloseableHttpClient httpClient5(HttpClientConnectionManager connectionManager,
                                           FeignHttpClientProperties httpClientProperties,
                                           LibbyControlProperties libbyControlProperties,
                                           CacheConfig cacheConfig) {

        File file = null;
        LibbyControlProperties.Httpclient5 httpclient5 = libbyControlProperties.getHttpclient5();
        if (LibbyControlProperties.CacheType.FILE.equals(httpclient5.getCacheType())) {
            file = new File(httpclient5.getCacheDirectory());
            file.mkdirs();
        }

        httpClient5 = CachingHttpClients.custom()
                                        .setCacheDir(file)
                                        .setCacheConfig(cacheConfig)
                                        .disableCookieManagement()
                                        .useSystemProperties()
                                        .setConnectionManager(connectionManager)
                                        .evictExpiredConnections()
                                        .setDefaultRequestConfig(RequestConfig.custom()
                                                                              .setConnectTimeout(Timeout.of(httpClientProperties.getConnectionTimeout(), TimeUnit.MILLISECONDS))
                                                                              .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
                                                                              .build())
                                        .build();
        return httpClient5;
    }

    @PreDestroy
    public void destroy() {
        if (httpClient5 != null) {
            httpClient5.close(CloseMode.GRACEFUL);
        }
    }

    private LayeredConnectionSocketFactory httpsSSLConnectionSocketFactory(boolean isDisableSslValidation) {
        final SSLConnectionSocketFactoryBuilder sslConnectionSocketFactoryBuilder = SSLConnectionSocketFactoryBuilder
                .create().setTlsVersions(TLS.V_1_3, TLS.V_1_2);

        if (isDisableSslValidation) {
            try {
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, new SecureRandom());
                sslConnectionSocketFactoryBuilder.setSslContext(sslContext);
            } catch (NoSuchAlgorithmException e) {
                LOG.warn("Error creating SSLContext", e);
            } catch (KeyManagementException e) {
                LOG.warn("Error creating SSLContext", e);
            }
        } else {
            sslConnectionSocketFactoryBuilder.setSslContext(SSLContexts.createSystemDefault());
        }

        return sslConnectionSocketFactoryBuilder.build();
    }

}
