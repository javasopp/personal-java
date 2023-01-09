package com.mds.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Sopp
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField(value="update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    /**
     * 上次登录时间
     */
    @TableField("last_login")
    private Long lastLogin;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;


    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 下拉菜单的键值对，返回单个用户信息的时候使用,当前的权限信息
     */
    @TableField(exist = false)
    private List<String> checkLableList;

    /**
     * 角色名称 : 列表信息查看。
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 新增时使用 && 修改的时候使用
     */
    @TableField(exist = false)
    private List<Long> roleIds = new ArrayList<>();
}
