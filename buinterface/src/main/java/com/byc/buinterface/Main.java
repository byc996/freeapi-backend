package com.byc.buinterface;

import com.byc.buinterface.client.BuClient;
import com.byc.buinterface.model.User;

public class Main {
    public static void main(String[] args) {
        BuClient buClient = new BuClient("harvey", "12345678");
        buClient.getNameByGet("hello");
        buClient.getNameByPost("hello1");
        User user = new User();
        user.setUsername("harvey");
        buClient.getUsernameByPost(user);
    }
}
