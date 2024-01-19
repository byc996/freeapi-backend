package com.byc.buinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.byc.buinterface.model.DomainVO;
import com.byc.buinterface.model.dto.IPLocation;
import com.byc.common.model.BaseResponse;
import com.byc.common.model.ErrorCode;
import com.byc.common.model.ResultUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RestController
@RequestMapping("/functionality")
public class FunctionalityController {

    private static final Map<String, String> codeMap = new HashMap<String, String>(){
        {
            put("210", "该域名还没被注册，可用");
            put("211", "该域名已被注册");
            put("212", "该域名无效");
            put("213", "超时");
        }
    };

    //https://ip-api.com/docs/api:json#test
    @GetMapping("/ip")
    public BaseResponse<IPLocation> getIPInfo(@RequestParam String ip) {
        ip = ip.trim();
        if (!isValidIP(ip)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "ip格式有误");
        }
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("lang", "zh-CN");
        String resp = HttpUtil.get( "http://ip-api.com/json/" + ip, paramMap);
        IPLocation ipLocation = JSONUtil.toBean(resp, IPLocation.class);
        return ResultUtils.success(ipLocation);
    }

    // https://developer.aliyun.com/article/657579
    @GetMapping("/domain")
    public BaseResponse<DomainVO> getDomainStatus(@RequestParam String domain) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("area_domain", domain);
        String resp = HttpUtil.get("http://panda.www.net.cn/cgi-bin/check.cgi", paramMap);
        DomainVO domainVO = new DomainVO();
        domainVO.setDomain(domain);
        try {
            Document doc = DocumentHelper.parseText(resp);
            Element roots = doc.getRootElement();
            Iterator elements=roots.elementIterator();
            while (elements.hasNext()){
                Element child= (Element) elements.next();
                if (child.getName().equals("returncode") && !child.getText().equals("200")) {
                    domainVO.setStatus("Failed to query. The API is abnormal.");
                    break;
                }
                if (child.getName().equals("original")) {
                    String code = child.getText().split(":")[0].trim();
                    domainVO.setStatus(codeMap.get(code));
                }
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success(domainVO);
    }


    public static boolean isValidIP(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return inetAddress.getHostAddress().equals(ipAddress);
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
