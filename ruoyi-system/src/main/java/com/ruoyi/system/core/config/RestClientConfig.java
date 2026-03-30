package com.ruoyi.system.core.config;

import com.ruoyi.common.utils.http.HttpRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: xiaodemos
 * @date: 2025-08-06 11:21
 * @description: 第三方请求配置类
 */

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpRestClient httpRestClient(RestTemplate restTemplate) {
        HttpRestClient client = new HttpRestClient(restTemplate);
        // 添加全局默认请求头
        // client.addDefaultHeader("X-App-Id", "my-app-id");
        return client;
    }
}
