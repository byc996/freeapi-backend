package com.byc.model.vo;

import java.util.Date;

public class UserInterfaceInfoVO {


    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 接口类型
     */
    private String interfaceType;

    /**
     * 接口状态
     */
    private String interfaceStatus;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer restNum;

    /**
     * 创建时间
     */
    private Date createTime;
}
