package com.byc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byc.common.model.entity.UserInterfaceInfo;

/**
* @author buyic
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-03-14 13:19:14
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add 是否为创建校验
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId);

}
