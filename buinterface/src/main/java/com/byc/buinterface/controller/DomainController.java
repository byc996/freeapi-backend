package com.byc.buinterface.controller;

import cn.hutool.http.HttpUtil;
import com.byc.buinterface.model.DomainVO;
import com.byc.clientsdk.model.Domain;
import com.byc.clientsdk.model.IP;
import com.byc.clientsdk.model.User;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/domain")
public class DomainController {

    private static final Map<String, String> codeMap = new HashMap<String, String>(){
        {
            put("210", "Domain name is available");
            put("211", "Domain name is not available");
            put("212", "Domain name is invalid");
            put("213", "Time out");
        }
    };

    @GetMapping("/ip")
    public String getIPInfo(@RequestBody IP ip) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("lang", ip.getLang());
        String resp = HttpUtil.get( "http://ip-api.com/json/" + ip.getIp(), paramMap);
        return resp;
    }

    @GetMapping("/exist")
    public DomainVO getDomainStatus(@RequestBody Domain domain) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("area_domain", domain.getDomain());
        String resp = HttpUtil.get("http://panda.www.net.cn/cgi-bin/check.cgi", paramMap);
        DomainVO domainVO = new DomainVO();
        domainVO.setDomain(domain.getDomain());
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
        return domainVO;
    }


}
