package com.byc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byc.common.model.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author buyic
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-03-14 13:19:14
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

    @Update("UPDATE user_interface_info SET restNum = 30 where isDelete = 0")
    void refreshRestNum();
}




