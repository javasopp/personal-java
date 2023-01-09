package com.mds.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mds.auth.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sopp
 * @since 2021-04-05
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id获取权限信息
     *
     * @param userId
     * @return
     */
    List<Long> getNavPermissionIds(Long userId);

    /**
     * 通过菜单id获取用户列表
     *
     * @param menuId
     * @return
     */
    List<SysUser> listByMenuId(Long menuId);
}
