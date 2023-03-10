package com.byc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byc.model.entity.InterfaceInfo;
import com.byc.model.entity.Post;

/**
* @author buyic
* @description 针对表【interface_info(interface info)】的数据库操作Service
* @createDate 2023-03-09 12:56:36
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add 是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
