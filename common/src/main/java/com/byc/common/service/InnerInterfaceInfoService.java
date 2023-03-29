package com.byc.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byc.common.model.entity.InterfaceInfo;


/**
* @author buyic
* @description 针对表【interface_info(interface info)】的数据库操作Service
* @createDate 2023-03-09 12:56:36
*/
public interface InnerInterfaceInfoService{
    /**
     * 从数据库中查模拟接口是否存在（请求路径、请求方法，布尔）
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
