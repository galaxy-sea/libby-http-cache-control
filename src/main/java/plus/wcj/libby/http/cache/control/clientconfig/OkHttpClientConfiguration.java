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

import java.io.File;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/28
 */
@EnableConfigurationProperties(LibbyControlProperties.class)
@ConditionalOnProperty(name = "spring.cloud.httpclientfactories.ok.enabled", matchIfMissing = true)
@ConditionalOnClass(OkHttpClient.class)
@AutoConfigureBefore(HttpClientConfiguration.class)
public class OkHttpClientConfiguration {


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
