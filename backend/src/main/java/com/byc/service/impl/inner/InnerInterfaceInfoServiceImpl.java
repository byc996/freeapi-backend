package com.byc.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byc.common.model.entity.InterfaceInfo;
import com.byc.common.service.InnerInterfaceInfoService;
import com.byc.common.exception.BusinessException;
import com.byc.common.model.ErrorCode;
import com.byc.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfoByUrlAndMethod(String url, String method) {
        if (StringUtils.isAnyBlank(url)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("url", url.trim());
        map.put("method", method.trim());
//        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("url", url);
//        queryWrapper.eq("method", method);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoMapper.selectByMap(map);
        if (interfaceInfoList.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        if (interfaceInfoList.size() > 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "存在重复的url");
        }
        return interfaceInfoList.get(0);
    }
}
