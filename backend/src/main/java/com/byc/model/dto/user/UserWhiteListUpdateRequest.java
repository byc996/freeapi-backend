package com.byc.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户更新请求
 *
 */
@Data
public class UserWhiteListUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * ip 白名单
     */
    private List<String> whiteList;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}