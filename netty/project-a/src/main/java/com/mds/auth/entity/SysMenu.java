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
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名字
     */
    @NotBlank(message = "菜单名称不能为空")
    @TableField("name")
    private String name;

    /**
     * 菜单URL
     */
    @TableField("path")
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @NotBlank(message = "菜单授权码不能为空")
    @TableField("perms")
    private String perms;

    /**
     * 路径
     */
    @TableField("component")
    private String component;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField(value="update_time", fill = FieldFill.UPDATE)
    private Long updateTime;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;


    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
