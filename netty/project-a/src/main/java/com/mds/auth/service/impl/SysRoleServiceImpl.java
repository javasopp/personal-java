package com.mds.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mds.auth.entity.SysRole;
import com.mds.auth.entity.SysUser;
import com.mds.auth.mapper.SysRoleMapper;
import com.mds.auth.mapper.SysUserMapper;
import com.mds.auth.mapper.SysUserRoleMapper;
import com.mds.auth.service.SysRoleService;
import com.mds.business.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sopp
 * @since 2021-04-05
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysUserMapper sysUserMapper;

    private final RedisUtil redisUtil;

    @Override
    public List<SysRole> listRolesByUserId(Long userId) {

        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
                .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

        return sysRoles;
    }

    /**
     * 清空redis缓存
     *
     * @param roleId
     */
    private void clearUserAuthorityInfo(long roleId) {
        List<SysUser> sysUsers = sysUserMapper.selectList(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id = " + roleId));

        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    private void clearUserAuthorityInfo(String username) {
        redisUtil.del("auth:" + username);
    }
}
