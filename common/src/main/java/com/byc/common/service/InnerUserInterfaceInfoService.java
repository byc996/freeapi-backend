package com.byc.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byc.common.model.entity.UserInterfaceInfo;

/**
* @author buyic
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-03-14 13:19:14
*/
public interface InnerUserInterfaceInfoService {

    /**
     *  调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean increment(long interfaceInfoId, long userId);
}
