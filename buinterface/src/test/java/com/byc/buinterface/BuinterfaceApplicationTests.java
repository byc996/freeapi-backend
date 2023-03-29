package com.byc.buinterface;

import cn.hutool.http.HttpUtil;
import com.byc.clientsdk.client.BuClient;
import com.byc.clientsdk.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
class BuinterfaceApplicationTests {

	@Resource
	private BuClient buClient;
	@Test
	void contextLoads() {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("area_domain", "bc2996.com");
		String resp = HttpUtil.get("http://panda.www.net.cn/cgi-bin/check.cgi", paramMap);
		System.out.println(resp);
		Assertions.assertTrue(!"".equals(resp));
	}

}
