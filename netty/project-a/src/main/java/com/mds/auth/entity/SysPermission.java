package com.mds.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 权限权限表
 * </p>
 *
 * @author Sopp
 * @since 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 权限标题
     */
    @TableField("name")
    private String name;

    /**
     * 路径
     */
    @TableField("path")
    private String path;

    /**
     * 权限权限编码
     */
    @TableField("perms")
    private String perms;

}
