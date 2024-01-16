package com.byc.gateway;

import cn.hutool.json.JSONUtil;
import com.byc.clientsdk.utils.SignUtils;
import com.byc.common.model.BaseResponse;
import com.byc.common.model.ErrorCode;
import com.byc.common.exception.BusinessException;
import com.byc.common.model.ResultUtils;
import com.byc.common.model.entity.InterfaceInfo;
import com.byc.common.model.entity.User;
import com.byc.common.service.InnerInterfaceInfoService;
import com.byc.common.service.InnerUserInterfaceInfoService;
import com.byc.common.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

//    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "localhost");

    private static final String INTERFACE_HOST = "http://localhost:8001";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径: " + path);
        log.info("请求方法: " + method);
        log.info("请求参数: " + request.getQueryParams());
        String remoteHost = request.getRemoteAddress().getHostName();
        log.info("请求来源地址: " + remoteHost);
        ServerHttpResponse response = exchange.getResponse();

        // 2. 用户鉴权（判断ak，sk是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String url = headers.getFirst("url");
        // 数据库中查ak是否已分配给用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        // 3. 黑白名单
        List<String> whiteList = JSONUtil.toList(invokeUser.getWhiteList(), String.class);
        if (!whiteList.isEmpty() &&!whiteList.contains(remoteHost)) {
            BaseResponse baseResponse = ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "您的IP不在白名单内");
            return handleCustomError(response, baseResponse);
        }

        long userId = invokeUser.getId();
//        if (!"harvey".equals(accessKey)){
//            return handleNoAuth(response);
//        }
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过五分钟
        long currentTime = System.currentTimeMillis() / 1000;
        long FIVE_MINUTES = 60 * 5;
        if ((currentTime - Long.parseLong(timestamp)) > FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 数据库中根据accessKey 查询 secretKey

        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSign(url, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 4. 请求的模拟接口是否存在？
        // 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfoByUrlAndMethod(url, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
//        if (interfaceInfo == null) {
//            return handleNoAuth(response);
//        }
        long interfaceInfoId = interfaceInfo.getId();
        String type = interfaceInfo.getType();

        // todo 是否还有调用次数
        int restNum = innerUserInterfaceInfoService.getRestNum(interfaceInfoId, userId, type);
        if (restNum <= 0) {
            BaseResponse baseResponse = ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "你本月的调用次数已使用完.");

//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "你的调用次数已用完");
            return handleCustomError(response, baseResponse);
        }

        // 5. 请求转发，调用模拟接口


        // 6. 响应日志
        // 通过response装饰器可以获取调用完后返回的response body， 并生成日志
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response) {
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
                        try {
                            innerUserInterfaceInfoService.increment(interfaceInfoId, userId, type);
                        } catch (Exception e) {
                            log.error("increment error", e);
                        }
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

    public Mono<Void> handleCustomError(ServerHttpResponse response, BaseResponse baseResponse) {
        String responseBody = JSONUtil.toJsonStr(baseResponse);
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBytes)));
    }
}