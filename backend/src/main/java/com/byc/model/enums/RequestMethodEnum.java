package com.byc.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RequestMethodEnum {

    GET("GET"),
    POST("POST");

    private final String value;


    RequestMethodEnum(String value) {
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public String getValue() {
        return value;
    }

}
