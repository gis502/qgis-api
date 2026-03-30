package com.ruoyi.common.utils.http;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author: xiaodemos
 * @date: 2025-08-06 11:19
 * @description: 第三方请求
 */

public class HttpRestClient {

    private final RestTemplate restTemplate;

    // 默认的请求头设置
    private static final HttpHeaders defaultHeaders;

    static {
        defaultHeaders = new HttpHeaders();
        defaultHeaders.setContentType(MediaType.APPLICATION_JSON);
        defaultHeaders.add("Accept", "application/json");
    }

    /**
     * 构造方法，注入RestTemplate
     * @param restTemplate RestTemplate实例
     */
    public HttpRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 不带Token的GET请求
     * @param url 请求URL
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @return 响应结果
     */
    public <T> T get(String url,  ParameterizedTypeReference<T> responseType) {
        return get(url, null, responseType);
    }

    /**
     * 带Token的GET请求
     * @param url 请求URL
     * @param token 认证Token
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @return 响应结果
     */
    public <T> T get(String url, String token,  ParameterizedTypeReference<T> responseType) {
        HttpEntity<?> entity = createHttpEntity(token, null);
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        );
        return response.getBody();
    }

    /**
     * 带路径参数和Token的GET请求
     * @param url 请求URL，包含路径参数占位符，如"/user/{id}"
     * @param token 认证Token
     * @param uriVariables 路径参数键值对
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @return 响应结果
     */
    public <T> T get(String url, String token, Map<String, ?> uriVariables,  ParameterizedTypeReference<T> responseType) {
        HttpEntity<?> entity = createHttpEntity(token, null);
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType,
                uriVariables
        );
        return response.getBody();
    }

    /**
     * 不带Token的POST请求
     * @param url 请求URL
     * @param requestBody 请求体
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @param <R> 请求体泛型
     * @return 响应结果
     */
    public <T, R> T post(String url, R requestBody, ParameterizedTypeReference<T> responseType) {
        return post(url, null, requestBody, responseType);
    }

    /**
     * 带Token的POST请求
     * @param url 请求URL
     * @param token 认证Token
     * @param requestBody 请求体
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @param <R> 请求体泛型
     * @return 响应结果
     */
    public <T, R> T post(String url, String token, R requestBody,  ParameterizedTypeReference<T> responseType) {
        HttpEntity<R> entity = createHttpEntity(token, requestBody);
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                responseType
        );
        return response.getBody();
    }

    /**
     * 带Token的PUT请求
     * @param url 请求URL
     * @param token 认证Token
     * @param requestBody 请求体
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @param <R> 请求体泛型
     * @return 响应结果
     */
    public <T, R> T put(String url, String token, R requestBody,  ParameterizedTypeReference<T> responseType) {
        HttpEntity<R> entity = createHttpEntity(token, requestBody);
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                responseType
        );
        return response.getBody();
    }

    /**
     * 带Token的DELETE请求
     * @param url 请求URL
     * @param token 认证Token
     * @param responseType 响应数据类型
     * @param <T> 响应数据泛型
     * @return 响应结果
     */
    public <T> T delete(String url, String token,  ParameterizedTypeReference<T> responseType) {
        HttpEntity<?> entity = createHttpEntity(token, null);
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                responseType
        );
        return response.getBody();
    }

    /**
     * 创建HTTP请求实体，包含 headers 和 body
     * @param token 认证Token，可为null
     * @param body 请求体，可为null
     * @param <R> 请求体泛型
     * @return HttpEntity实例
     */
    private <R> HttpEntity<R> createHttpEntity(String token, R body) {
        HttpHeaders headers = new HttpHeaders();
        // 复制默认请求头
        headers.putAll(defaultHeaders);

        // 如果有token，添加到请求头
        if (token != null && !token.trim().isEmpty()) {
            headers.add("Authorization", "Bearer " + token);
        }

        return new HttpEntity<>(body, headers);
    }

    /**
     * 添加默认请求头
     * @param name 头名称
     * @param value 头值
     */
    public void addDefaultHeader(String name, String value) {
        defaultHeaders.add(name, value);
    }

    /**
     * 移除默认请求头
     * @param name 头名称
     */
    public void removeDefaultHeader(String name) {
        defaultHeaders.remove(name);
    }
}
