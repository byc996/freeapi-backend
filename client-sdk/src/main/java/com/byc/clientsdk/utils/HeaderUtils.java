package com.byc.clientsdk.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.byc.clientsdk.model.HeaderEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.byc.clientsdk.utils.SignUtils.getSign;

public class HeaderUtils {

    public static Map<String, String> getHeaders(String accessKey, String secretKey, String url, String requestHeader) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(requestHeader)) {
            List<HeaderEntry> headerEntries = JSONUtil.toList(requestHeader, HeaderEntry.class);
            for (HeaderEntry headerEntry: headerEntries) {
                map.put(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        map.put("accessKey", accessKey);
        // 一定不能发送给后端
//        map.put("secretKey", secretKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("url", url);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", getSign(url, secretKey));
        return map;
    }
}
