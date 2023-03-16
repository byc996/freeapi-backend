package com.byc.gateway;

import com.byc.clientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "localhost");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径: " + request.getPath().value());
        log.info("请求方法: " + request.getMethod());
        log.info("请求参数: " + request.getQueryParams());
        String remoteHost = request.getRemoteAddress().getHostName();
        log.info("请求来源地址: " + remoteHost);
        ServerHttpResponse response = exchange.getResponse();
        // 2. 黑白名单
//        if (!IP_WHITE_LIST.contains(remoteHost)) {
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
        // 3. 用户鉴权（判断ak，sk是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // todo 数据库中查
        if (!accessKey.equals("harvey")) {
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // todo 时间和当前时间不能超过五分钟
        long currentTime = System.currentTimeMillis() / 1000;
        long FIVE_MINUTES = 60 * 5;
        if ((currentTime - Long.parseLong(timestamp)) > FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // todo 数据库中根据accessKey 查询 secretKey
        String serverSign = SignUtils.getSign(body, "12345678");
        if (!sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 4. 请求的模拟接口是否存在？
        // todo 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        // 5. 请求转发，调用模拟接口


        // 6. 响应日志
        // 通过response装饰器可以获取调用完后返回的response body， 并生成日志
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response){
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        DataBufferUtils.release(dataBuffer); // 释放掉内存
                        // 构建日志
                        String data = new String(content, StandardCharsets.UTF_8);
                        log.info("响应结果：" + data);
                        return bufferFactory.wrap(content);
                    }));
                } else {
                    log.error("响应异常：body is not Flux");
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(responseDecorator).build()).then(
                Mono.fromRunnable(() -> {

                    if (response.getStatusCode() == HttpStatus.OK) {
                        // 7. todo 调用成功，接口调用次数+1 invokeCount

                    } else {
                        // 8. 调用失败，返回一个规范的错误
                        log.error("响应码异常：" + response.getStatusCode());
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}