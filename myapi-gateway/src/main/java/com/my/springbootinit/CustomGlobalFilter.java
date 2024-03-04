package com.my.springbootinit;


import cn.hutool.json.JSONUtil;
import com.my.myapiclientsdk.signUtils.SignUtils;
import com.my.myapicommon.common.BaseResponse;
import com.my.myapicommon.common.ErrorCode;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.myapicommon.model.User;
import com.my.myapicommon.model.UserInterfaceInfo;
import com.my.myapicommon.service.InnerInterfaceInfoService;
import com.my.myapicommon.service.InnerNonceService;
import com.my.myapicommon.service.InnerUserInterfaceInfoService;
import com.my.myapicommon.service.InnerUserService;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 网关全局过滤器
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    InnerNonceService innerNonceService;

    private static final List<String> WHITE_LIST = Collections.singletonList("127.0.0.1");

    private static final String HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /**
         * 1.请求日志
         */
        ServerHttpRequest request = exchange.getRequest();
        String protocol = "http://";
        String host = request.getRemoteAddress().getHostString();
        String port = String.valueOf(request.getRemoteAddress().getPort());
        host = host + ":" + "8123";
        String path = request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径: " + protocol + host + path);
        log.info("请求方法: " + method);
        log.info("请求参数: " + request.getQueryParams());
        String hostString = request.getLocalAddress().getHostString();
        log.info("请求来源地址: " + hostString);
        log.info("请求目标地址: " + host);
        ServerHttpResponse response = exchange.getResponse();
        /**
         * 2.访问控制
         */
        if (!WHITE_LIST.contains(hostString)) {
            //禁止访问
            return handleNoAuth(response, ErrorCode.BLACK_LIST_ERROR);
        }

        /**
         * 3.用户鉴权(判断accessKey、secretKey是否合法)
         */
        HttpHeaders headers = request.getHeaders();
        String interfaceInfoId1 = headers.getFirst("interfaceInfoId");
        String accessKey = headers.getFirst("accessKey");
        String body = null;
        try {
            body = URLDecoder.decode(headers.getFirst("body"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String nonce = headers.getFirst("nonce");
        String timeStamp = headers.getFirst("timeStamp");
        String headerSign = headers.getFirst("sign");

        // 这里的accessKey肯定是得从数据库中获取的
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error,user do not exist");
            return handleNoAuth(response, ErrorCode.ACCESS_KEY_ERROR);
        }


        /**
         **这里可以检查当前时间与请求携带的时间戳之间的间隔时间
         * todo 以不超过五分钟为例
         */
        if ((System.currentTimeMillis() / 1000 - Long.parseLong(timeStamp)) > 60 * 5) {
            //禁止访问
            return handleNoAuth(response, ErrorCode.INVOKE_TIMEOUT);
        }

        /**
         * 检查随机数，防止出现请求重发(每次用户请求携带的随机数都不一样，服务端要做校验，确保是有效的随机数)
         * 这里考虑首先从redis里面查询该随机数是否存在，若不存在，则将随机数放入到redis中，5分钟过期,如果存在，则认为是重放攻击，拒绝调用
         */
        if (!innerNonceService.nonceJudge(nonce)) {
            //禁止访问
            return handleNoAuth(response, ErrorCode.RANDOM_NUMBER_ERROR);
        }

        //这里的secretKey肯定是得从数据库中获取的
        String secretKey = invokeUser.getSecretKey();
        String sign = SignUtils.getSign(body, secretKey);
        if (!sign.equals(headerSign) || sign == null) {
            //禁止访问
            return handleNoAuth(response, ErrorCode.SECRET_KEY_ERROR);
        }
//        /**
//         * 4.判断接口是否存在
//         * 从数据库中查询对应的接口信息
//         */
//        InterfaceInfo interfaceInfo = null;
//        try {
//            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(protocol, host, path, method);
//        } catch (Exception e) {
//            log.error("getInterfaceInfo error,interfaceInfo do not exist");
//            return handleNoAuth(response, "接口不存在或已被下线");
//        }

        Long userId = invokeUser.getId();
        long interfaceInfoId = Long.parseLong(interfaceInfoId1);

        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(interfaceInfoId);
        if (interfaceInfo.getLeftNum() <= 0) {
            return handleNoAuth(response, ErrorCode.INTERFACE_LEFTNUM_RUNOUT);
        }


        /**
         * 判断该用户对于该接口是否还剩余调用次数/如果该记录不存在，调用的方法也会先创建一条新纪录
         */
        UserInterfaceInfo userInterfaceInfo = innerUserInterfaceInfoService.getUserInterfaceInfo(userId, interfaceInfoId);
        if (userInterfaceInfo.getLeftNum() <= 0) {
            return handleNoAuth(response, ErrorCode.USER_INTERFACE_LEFTNUM_RUNOUT);
        }


        /**
         * 5.请求转发，调用接口
         * 利用 response 装饰者，增强原有 response 的处理能力，在接口返回响应后再去对接口调用次数进行修改等
         * 6.响应日志
         */


        return ResponseLog(exchange, chain, userId, interfaceInfoId);


//        /**
//         * 7.接口调用成功，次数更改 todo 接口调用次数 +1
//         */
//        if (responseStatusCode == HttpStatus.OK) {
//            log.info("invoke interface success");
//
//        }
//        /**
//         * 8.接口调用失败，返回一个规范的错误码
//         */
//        else {
//            log.info("invoke interface error");
//            return handleInvokeError(response);
//
//        }


    }

    /**
     * 鉴权失败处理
     *
     * @param response
     * @return
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response, ErrorCode errorCode) {


        response.setStatusCode(HttpStatus.FORBIDDEN);
//        BaseResponse<String> noAuth = new BaseResponse<>(40300, "NO_AUTH", message);
//        String jsonStr = JSONUtil.toJsonStr(noAuth);
        DataBuffer dataBuffer = response.bufferFactory().wrap(errorCode.getMessage().getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * 接口调用失败处理
     *
     * @param response
     * @return
     */
    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    /**
     * 预期是等模拟接口调用完成，才记录响应日志、统计调用次数但现实是chain.filter 方法立刻返回了，
     * 直到 filter 过滤器 return 后才调用了模拟接口原因是:
     * chain.filter 是个异步操作，理解为前端的 promise，
     * 也就是如果按照原来的设想情况下会先去对更改接口调用次数，之后才会去调用接口，这显然是不合理的
     * 解决方案: 利用 response 装饰者，增强原有 response 的处理能力
     * 参考博客: https://blog.csdn.net/qq_19636353/article/details/126759522 (以这个为主)
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> ResponseLog(ServerWebExchange exchange, GatewayFilterChain chain, long userId, long interfaceInfoId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        AtomicReference<DataBuffer> dataBuffer1 = null;
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //往返回值里面写数据
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                /**
                                 *    接口调用成功，次数更改接口调用次数 +1
                                 */
                                try {
                                    innerUserInterfaceInfoService.invokeCount(userId, interfaceInfoId);
                                } catch (Exception e) {
                                    log.error("invokeCount error");
                                }

                                //拼接字符串
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info("invoke interface success");
                                log.info("响应数据: " + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }

                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.info("invoke interface error");
            log.error("gateway log exception.\n" + e);
            handleNoAuth(exchange.getResponse(), ErrorCode.SYSTEM_ERROR);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }


}