package com.mds.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mds.auth.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sopp
 * @since 2021-04-05
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户id获取权限列表
     *
     * @param userId
     * @return
     */
    List<SysRole> listRolesByUserId(Long userId);
}
