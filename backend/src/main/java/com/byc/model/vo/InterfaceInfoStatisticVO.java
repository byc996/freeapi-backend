package com.byc.model.vo;

import com.byc.common.model.entity.InterfaceInfo;
import com.byc.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息统计封装类
 *
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoStatisticVO extends InterfaceInfo {

    /**
     * 调用次数
     */
    private int total;

    private static final long serialVersionUID = 1L;
}