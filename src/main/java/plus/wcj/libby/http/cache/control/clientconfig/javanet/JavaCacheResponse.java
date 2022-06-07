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

package plus.wcj.libby.http.cache.control.clientconfig.javanet;

import java.io.IOException;
import java.io.InputStream;
import java.net.CacheResponse;
import java.util.List;
import java.util.Map;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/6/1
 */
public class JavaCacheResponse extends CacheResponse {
    @Override
    public Map<String, List<String>> getHeaders() throws IOException {
        return null;
    }

    @Override
    public InputStream getBody() throws IOException {
        return null;
    }
}