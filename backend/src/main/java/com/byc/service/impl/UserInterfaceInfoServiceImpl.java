package com.byc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.byc.common.ErrorCode;
import com.byc.common.model.entity.UserInterfaceInfo;
import com.byc.exception.BusinessException;
import com.byc.mapper.UserInterfaceInfoMapper;
import com.byc.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author buyic
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-03-14 13:19:14
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int num = userInterfaceInfo.getRestNum();


        // 创建时，所有参数必须非空
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if (userInterfaceInfo.getRestNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }

}




