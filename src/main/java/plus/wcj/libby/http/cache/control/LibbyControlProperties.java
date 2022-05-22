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
}
