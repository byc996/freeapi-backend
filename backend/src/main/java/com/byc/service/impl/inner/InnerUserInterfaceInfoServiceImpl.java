package com.byc.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.byc.common.ErrorCode;
import com.byc.common.model.entity.UserInterfaceInfo;
import com.byc.common.service.InnerUserInterfaceInfoService;
import com.byc.exception.BusinessException;
import com.byc.mapper.UserInterfaceInfoMapper;
import com.byc.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean increment(long interfaceInfoId, long userId) {
        // 判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if (userInterfaceInfo == null) {
            UserInterfaceInfo temp = new UserInterfaceInfo();
            temp.setUserId(userId);
            temp.setInterfaceInfoId(interfaceInfoId);
            temp.setTotalNum(0);
            temp.setRestNum(1000);
            temp.setStatus(0);
            userInterfaceInfoMapper.insert(temp);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
//        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("restNum = restNum -1, totalNum = totalNum + 1");
        return userInterfaceInfoService.update(updateWrapper);
    }
}
