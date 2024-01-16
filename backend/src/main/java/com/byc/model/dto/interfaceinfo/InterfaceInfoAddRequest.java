package com.byc.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 接口类型 （免费，限次）
     */
    private String type;

    /**
     * 接口种类（趣味娱乐，功能应用）
     */
    private String category;


    /**
     *  简介
     */
    private String brief;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

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
     * 请求类型
     */
    private String method;



}