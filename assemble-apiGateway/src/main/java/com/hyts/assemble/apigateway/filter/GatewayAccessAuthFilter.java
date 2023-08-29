/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.apigateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.hyts.assemble.apigateway.security.AssembleSecurityProperties;
import com.hyts.assemble.common.toolkit.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.gateway.filter
 * @author:LiBo/Alex
 * @create-date:2021-12-12 1:26 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Component
public class GatewayAccessAuthFilter implements GlobalFilter, Ordered {


    @Autowired
    RestTemplate restTemplate;


    @Autowired
    AssembleSecurityProperties assembleSecurityProperties;


    @Autowired(required = false)
    WebClient webClient;


    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    private final static String DEFAULT_HTTP_CODE_KEY = "code";

    private final static String DEFAULT_HTTP_MESSAGE_KEY = "message";

    private final static String DEFAULT_AUTHR_HEADER_KEY = "Authorization";

    /**
     * 过滤器操作
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 计算计时器
        StopWatch stopWatch = new StopWatch("网关处理总体流程");
        try{
            ServerHttpRequest request = exchange.getRequest();
            log.info("【网关】代理请求的操作处理——> url:{} -> method:{} -> body:{}", request.getPath(), request.getMethod().name());
            start(stopWatch,"预校验是否符合满足忽略预校验功能的路径机制");
            if (preValidateRequestPathHaveIngnoreAuth(request)) {
                stop(stopWatch);
                try {
                    start(stopWatch,"执行请求Auth Security请求");
                    JSONObject result = executeValidateSecurityRequestHaveAuth(request,Boolean.FALSE);
                    log.info("执行请求Auth Security请求：{}",result);
                    stop(stopWatch);
                    log.info("【网关】代理预先执行校验执行结果：{}", result);
                    if (isFailure(result)) {
                        log.info("【网关】代理预先执行校验执行回写到response客户端，通知对应结果!");
                        start(stopWatch,"代理预先执行校验执行回写到response客户端");
                        Mono<Void> writeResult = writePreValidateResultToResponse(exchange,result);
                        stop(stopWatch);
                        return writeResult;
                    }
                } catch (Exception e) {
                    log.error("【网关】处理失败，出现异常!", e);
                    stop(stopWatch);
                }
            }
            start(stopWatch,"路由相关的后端微服务以及返回的总体时间");
            return executeRouteServerWriteReponse(exchange,chain,stopWatch);
        }finally {
            stop(stopWatch);
            log.info("计算所有的先关数据信息耗时:{}", stopWatch.prettyPrint());
        }
    }

    /**
     * 启动任务统计
     * @param stopWatch
     * @param taskName
     */
    private void start(StopWatch stopWatch,String taskName){
        if(stopWatch.isRunning()){
            stopWatch.stop();
            //调用请求之前统计开始,调用请求之后统计时间
            stopWatch.start(taskName);
        }else {
            stopWatch.start(taskName);
        }
    }

    /**
     * 停止任务统计
     * @param stopWatch
     */
    private void stop(StopWatch stopWatch){
        if(stopWatch.isRunning()){
            stopWatch.stop();
        }
    }
    /**
     * 预校验是否符合满足忽略预校验功能的路径机制
     *
     * @param request
     * @return
     */
    private Boolean preValidateRequestPathHaveIngnoreAuth(ServerHttpRequest request) {
        log.info("预校验判断是否需要进行预校验执行-路径:{}",request.getPath().toString());
        Boolean result = Arrays.stream(assembleSecurityProperties.getIgnorePath()).noneMatch(param -> antPathMatcher.match(param, request.getPath().toString()));
        log.info("完成预校验执行结果-路径:{} - 是否需要进行预校验Security权限:{}",request.getPath().toString(),result);
        return result;
    }

    /**
     * 执行请求Auth Security请求
     *
     * @param request
     * @return
     */
    private JSONObject executeValidateSecurityRequestHaveAuth(ServerHttpRequest request, boolean async) {
        if (async) {
            Mono<JSONObject> resultWrapper = webClient.post().uri(assembleSecurityProperties.getAuthUrl() + request.getPath()).bodyValue(new JSONObject()).accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.APPLICATION_JSON.toString()).acceptCharset(StandardCharsets.UTF_8).retrieve().bodyToMono(JSONObject.class).take(Duration.of(5, ChronoUnit.SECONDS)).publishOn(Schedulers.elastic());
            try {
                CompletableFuture<JSONObject> completableFuture = CompletableFuture.supplyAsync(() -> resultWrapper.block());
                return completableFuture.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("executeValidateSecurityRequestHaveAuth is failure!", e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(DEFAULT_HTTP_CODE_KEY,HttpStatus.INTERNAL_SERVER_ERROR.value());
                return jsonObject;
            }
        } else {
            HttpHeaders headers = new HttpHeaders();
            List<String> headerList = request.getHeaders().get(DEFAULT_AUTHR_HEADER_KEY);
            if(CollectionUtil.isNotEmpty(headerList)){
                headers.add(DEFAULT_AUTHR_HEADER_KEY,request.getHeaders().get(DEFAULT_AUTHR_HEADER_KEY).get(0)+"");
            }else{
                headers.add(DEFAULT_AUTHR_HEADER_KEY,"");
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put(DEFAULT_HTTP_CODE_KEY,HttpStatus.UNAUTHORIZED.value());
//                jsonObject.put(DEFAULT_HTTP_MESSAGE_KEY,"token非法");
//                return jsonObject;
            }
            HttpEntity entity = new HttpEntity(new JSONObject(),headers);
            return restTemplate.postForObject(assembleSecurityProperties.getAuthUrl() + request.getPath(),entity , JSONObject.class);
        }
    }

    /**
     * 如果失败！建立response通信，回复响应页面的对应的预校验结果
     * @param exchange
     * @param result
     * @return
     */
    private Mono<Void> writePreValidateResultToResponse(ServerWebExchange exchange,JSONObject result){
        HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
        //返回数据格式
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            return exchange.getResponse().writeWith(Flux.just(result).map(bx -> {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(DEFAULT_HTTP_CODE_KEY,bx.getString(DEFAULT_HTTP_CODE_KEY));
                    jsonObject.put(DEFAULT_HTTP_MESSAGE_KEY,bx.getString(DEFAULT_HTTP_MESSAGE_KEY));
                    return exchange.getResponse().bufferFactory().wrap(jsonObject.
                            toJSONString().getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    log.error("execute the auth filter process is failure!", e);
                    return null;
                }
            }));
        } catch (Exception e) {
            log.error("执行访问代理失败！", e);
            return Mono.empty();
        }
    }

    /**
     * 执行路由服务机制结果返回
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> executeRouteServerWriteReponse(ServerWebExchange exchange, GatewayFilterChain chain,StopWatch stopWatch){
        return chain.filter(exchange).then().then(Mono.fromRunnable(() -> {
            stop(stopWatch);
        }));
    }

    /**
     * 是否成功
     * @param result
     * @return
     */
    private boolean isSuccess(JSONObject result){
        int code = result.getInteger(DEFAULT_HTTP_CODE_KEY);
        return code == HttpStatus.OK.value() || code == HttpStatus.ACCEPTED.value() || code == HttpStatus.CREATED.value();
    }

    /**
     * 是否失败
     * @param result
     * @return
     */
    private boolean isFailure(JSONObject result){
        return !isSuccess(result);
    }

    /**
     * 获取信息数据结构
     *
     * @param serverHttpRequest
     * @return
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());

        });
        return bodyRef.get();
    }


    /**
     * 排序控制
     *
     * @return
     */
    @Override
    public int getOrder() {
            return 10;
    }
}
