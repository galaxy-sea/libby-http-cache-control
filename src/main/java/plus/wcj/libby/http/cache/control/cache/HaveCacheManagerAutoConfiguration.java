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
