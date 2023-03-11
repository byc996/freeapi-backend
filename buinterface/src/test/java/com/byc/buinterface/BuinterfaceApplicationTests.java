package com.byc.buinterface;

import com.byc.clientsdk.client.BuClient;
import com.byc.clientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BuinterfaceApplicationTests {

	@Resource
	private BuClient buClient;
	@Test
	void contextLoads() {
		buClient.getNameByGet("hello");
		buClient.getNameByPost("hello1");
		User user = new User();
		user.setUsername("harvey");
		buClient.getUsernameByPost(user);
	}

}
