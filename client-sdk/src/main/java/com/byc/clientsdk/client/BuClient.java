package com.byc.clientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.byc.clientsdk.model.*;
import com.byc.clientsdk.utils.HeaderUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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
        String url = GATEWAY_HOST + "/api/name/user";
        HttpResponse response = HttpRequest.post(url)
//                    .charset(StandardCharsets.UTF_8)
                .addHeaders(HeaderUtils.getHeaders(accessKey, secretKey, url, ""))
                .body(json)
                .execute();
        return response.body();
    }



    public String getDomainStatus(Domain domain) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        String json = JSONUtil.toJsonStr(domain);
        String url = GATEWAY_HOST + "/api/domain/exist";
        HttpResponse response = HttpRequest.get(url)
                .addHeaders(HeaderUtils.getHeaders(accessKey, secretKey, url, ""))
                .body(json)
                .execute();
//        System.out.println(result);
        return response.body();
    }

    public String getIPInfo(IP ip) {
        String json = JSONUtil.toJsonStr(ip);
        String url = GATEWAY_HOST + "/api/domain/ip";
        HttpResponse response = HttpRequest.get(url)
                .addHeaders(HeaderUtils.getHeaders(accessKey, secretKey, url, ""))
                .body(json)
                .execute();
        return response.body();
    }

//    private Map<String, String> getHeaders(String url, String requestHeader) {
//        Map<String, String> map = new HashMap<>();
//        if (StringUtils.isNotBlank(requestHeader)) {
//            List<HeaderEntry> headerEntries = JSONUtil.toList(requestHeader, HeaderEntry.class);
//            for (HeaderEntry headerEntry: headerEntries) {
//                map.put(headerEntry.getKey(), headerEntry.getValue());
//            }
//        }
//        map.put("accessKey", this.accessKey);
//        // 一定不能发送给后端
////        map.put("secretKey", secretKey);
//        map.put("nonce", RandomUtil.randomNumbers(4));
//        map.put("url", url);
//        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        map.put("sign", getSign(url, this.secretKey));
//        return map;
//    }

}