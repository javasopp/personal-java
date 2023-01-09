package com.mds.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mds.auth.dto.CommonPage;
import com.mds.auth.entity.SysUser;
import com.mds.business.common.exception.ErrorException;
import com.mds.business.common.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sopp
 * @since 2021-04-05
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名获取用户信息
     *
     * @param username
     * @return
     */
    SysUser getByUsername(String username);

    /**
     * 通过用户id获取权限
     *
     * @param userId
     * @return
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 通过用户名清楚用户权限信息
     *
     * @param username
     */
    void clearUserAuthorityInfo(String username);

    /**
     * 通过菜单id，清空所有权限信息
     * @param menuId
     */
    void clearUserAuthorityInfoByMenuId(Long menuId);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    Message getUserInfo(long userId);

    /**
     * 获取用户信息-分页
     *
     * @param commonPage
     * @return
     */
    Message getUserInfoList(CommonPage commonPage);

    /**
     * 保存用户信息
     * @param sysUser
     * @return
     * @throws ErrorException
     */
    Message saveUserInfo(SysUser sysUser) throws ErrorException;

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     * @throws ErrorException
     */
    Message updateUserInfo(SysUser sysUser) throws ErrorException;

    /**
     * 删除用户信息
     * @param ids
     * @param httpServletRequest
     * @return
     * @throws ErrorException
     */
    Message deleteUserInfoByIds(List<Long> ids, HttpServletRequest httpServletRequest) throws ErrorException;

    /**
     * 获取选中的角色信息
     * @return
     */
    Message getSelectRoleInfo();
}
