package com.byc.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum InterfaceCategoryEnum {

    NEWS("新闻资讯", "新闻资讯"),
    ENTERTAINMENT("趣味娱乐", "趣味娱乐"),
    FUNCTIONALITY("功能应用", "功能应用"),
    DATA("数据智能", "数据智能"),
    QA("知识问答", "知识问答"),
    LIFE("生活服务", "生活服务");

    private final String text;

    private final String value;

    InterfaceCategoryEnum(String text, String value) {
        this.text = text;
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

    public String getText() {
        return text;
    }
}
