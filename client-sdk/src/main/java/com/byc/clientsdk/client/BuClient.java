package com.byc.clientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.byc.clientsdk.model.Domain;
import com.byc.clientsdk.model.IP;
import com.byc.clientsdk.model.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.byc.clientsdk.utils.SignUtils.getSign;


/**
 * 调研第三方接口的客户端
 */
public class BuClient {

    private static final String GATEWAY_HOST = "http://localhost:8002";

    private String accessKey;

    private String secretKey;

    public BuClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .charset(StandardCharsets.UTF_8)
                .addHeaders(getHeaders(json) )
                .body(json)
                .execute();
        System.out.println(response.getStatus());
        System.out.println(response.body());
        return response.body();
    }

    private Map<String, String> getHeaders(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        // 一定不能发送给后端
//        map.put("secretKey", secretKey);

        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body", body);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", getSign(body, secretKey));
        return map;
    }

    public String getDomainStatus(Domain domain) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        String json = JSONUtil.toJsonStr(domain);
        HttpResponse response = HttpRequest.get(GATEWAY_HOST + "/api/domain/exist")
                .addHeaders(getHeaders(json) )
                .body(json)
                .execute();
//        System.out.println(result);
        return response.body();
    }

    public String getIPInfo(IP ip) {
        String json = JSONUtil.toJsonStr(ip);
        HttpResponse response = HttpRequest.get(GATEWAY_HOST + "/api/domain/ip")
                .addHeaders(getHeaders(json) )
                .body(json)
                .execute();
        return response.body();
    }

}