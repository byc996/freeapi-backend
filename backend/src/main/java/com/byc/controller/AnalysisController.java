package com.byc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byc.annotation.AuthCheck;
import com.byc.common.model.BaseResponse;
import com.byc.common.model.ResultUtils;
import com.byc.common.model.entity.InterfaceInfo;
import com.byc.common.model.entity.UserInterfaceInfo;
import com.byc.constant.UserConstant;
import com.byc.common.exception.BusinessException;
import com.byc.common.model.ErrorCode;
import com.byc.mapper.UserInterfaceInfoMapper;
import com.byc.model.vo.InterfaceInfoStatisticVO;
import com.byc.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoStatisticVO>> listTopInvokeInterfaceInfo(@RequestParam int k) {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(k);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (list.isEmpty()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoStatisticVO> interfaceInfoStatisticVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoStatisticVO interfaceInfoStatisticVO = new InterfaceInfoStatisticVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoStatisticVO);
            int total = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoStatisticVO.setTotal(total);
            return interfaceInfoStatisticVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoStatisticVOList);
    }
}
