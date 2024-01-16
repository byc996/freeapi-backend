package com.byc.buinterface.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * interface info
 * @TableName interface_info
 */
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;


    /**
     *  简介
     */
    private String brief;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口类型 （免费，限次）
     */
    private String type;

    /**
     * 接口种类（趣味娱乐，功能应用）
     */
    private String category;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 请求头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 调用次数
     */
    private Integer invokeNum;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;



}