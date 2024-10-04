package com.wkwk.interceptor;

import com.wkwk.utils.ThreadLocalUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Configuration
public class MyFeignRequestInterceptor  implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("userId", ThreadLocalUtil.getUserId().toString());
    }
}
