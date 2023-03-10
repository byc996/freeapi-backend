package com.byc.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byc.common.ErrorCode;
import com.byc.exception.BusinessException;
import com.byc.mapper.InterfaceInfoMapper;
import com.byc.model.entity.InterfaceInfo;
import com.byc.model.enums.PostGenderEnum;
import com.byc.model.enums.PostReviewStatusEnum;
import com.byc.service.InterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author buyic
* @description 针对表【interface_info(interface info)】的数据库操作Service实现
* @createDate 2023-03-09 12:56:36
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {


        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank( name, url, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }
}



