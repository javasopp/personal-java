package com.mds.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mds.auth.config.security.MyPasswordEncoder;
import com.mds.auth.dto.CommonPage;
import com.mds.auth.entity.SysPermission;
import com.mds.auth.entity.SysRole;
import com.mds.auth.entity.SysUser;
import com.mds.auth.entity.SysUserRole;
import com.mds.auth.mapper.SysPermissionMapper;
import com.mds.auth.mapper.SysUserMapper;
import com.mds.auth.mapper.SysUserRoleMapper;
import com.mds.auth.service.SysRoleService;
import com.mds.auth.service.SysUserService;
import com.mds.auth.utils.JwtUtils;
import com.mds.auth.vo.CheckLable;
import com.mds.business.common.Constants;
import com.mds.business.common.exception.ErrorException;
import com.mds.business.common.message.Message;
import com.mds.business.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sopp
 * @since 2021-04-05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 超时时间：默认300s，即5分钟
     */
    @Value("${jwt.expiration}")
    private int expirationTime;

    private final JwtUtils jwtUtils;

    private final SysRoleService sysRoleService;

    private final UserRoleServiceImpl userRoleService;

    private final RedisUtil redisUtil;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysPermissionMapper sysPermissionMapper;

    private final SysUserMapper sysUserMapper;

    private final MyPasswordEncoder myPasswordEncoder;

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    /**
     * 通过用户id查询权限
     *
     * @param userId
     * @return
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {

        SysUser sysUser = sysUserMapper.selectById(userId);

        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";

        if (redisUtil.hasKey(Constants.ROLE_PRE + sysUser.getUsername())) {
            authority = (String) redisUtil.get(Constants.ROLE_PRE + sysUser.getUsername());

        } else {
            // 获取角色编码
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

            if (roles.size() > 0) {
                String roleCodes = roles.stream().
                        map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 此处不需要菜单，设计中只需要权限，故而去掉菜单信息。
            // 获取权限操作编码
            List<Long> permissionIds = sysUserMapper.getNavPermissionIds(userId);
            if (permissionIds.size() > 0) {

                List<SysPermission> permissionList = sysPermissionMapper.selectList(
                        new QueryWrapper<SysPermission>().in("id", permissionIds));
                String permissionPerms = permissionList.stream().map(
                        p -> p.getPerms()).collect(Collectors.joining(","));

                authority = authority.concat(permissionPerms);
            }

            redisUtil.set(Constants.ROLE_PRE + sysUser.getUsername(), authority, expirationTime);
        }

        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers = sysUserMapper.listByMenuId(menuId);

        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public Message getUserInfoList(CommonPage commonPage) {
        IPage<SysUser> pages = new Page<>(commonPage.getCurrent(), commonPage.getSize());
        QueryWrapper<SysUser> wq = new QueryWrapper<>();
        if (StringUtils.isNotBlank(commonPage.getSearchInfo())) {
            String searchInfo = commonPage.getSearchInfo();
            wq.like("username", searchInfo).or().like("description", searchInfo);
        }
        wq.orderByDesc("update_time");

//        List<SysUser> list = new ArrayList<>();
//        if (StringUtils.isNotBlank(commonPage.getSearchInfo())) {
//            list = sysUserMapper.selectList(wq);
//        } else {
//            list = sysUserMapper.selectPage(pages, wq).getRecords();
//
//        }
        if (StringUtils.isNotBlank(commonPage.getSearchInfo())) {
            pages = sysUserMapper.selectPage(new Page<>(), wq);
        } else {
            pages = sysUserMapper.selectPage(pages, wq);
        }

        if (pages.getTotal() == Constants.ZERO) {
            return Message.noData();
        } else {
            // 存放角色列表信息
            List<Long> roleIds = new ArrayList<>();
            pages.getRecords().forEach(user -> {
                String roleInfo = "";
                List<SysRole> sysRoleList = sysRoleService.listRolesByUserId(user.getId());
                for (SysRole sysRole : sysRoleList) {
                    roleInfo += sysRole.getName() + ",";
                    roleIds.add(sysRole.getId());
                }
                if (StringUtils.isNotBlank(roleInfo)) {
                    roleInfo = roleInfo.substring(0, roleInfo.length() - 1);
                    user.setRoleName(roleInfo);
                    user.setRoleIds(roleIds);
                }
            });
            return Message.successList(pages.getRecords(), (int) pages.getTotal());
        }
    }

    @Override
    public Message getUserInfo(long userId) {
        SysUser sysUser = getById(userId);
        List<String> checkLableList = new ArrayList<>();
        List<SysRole> sysRoleList = sysRoleService.listRolesByUserId(userId);
        for (SysRole sysRole : sysRoleList) {
            checkLableList.add(String.valueOf(sysRole.getId()));
        }
        if (!checkLableList.isEmpty()) {
            sysUser.setCheckLableList(checkLableList);
        }
        return Message.success(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message saveUserInfo(SysUser sysUser) throws ErrorException {
        preHandleUserInfo(sysUser);
        save(sysUser);
        setCommonUserRoles(sysUser);

        return Message.success();
    }

    /**
     * 预先对用户进行处理
     *
     * @param sysUser
     * @throws ErrorException
     */
    private void preHandleUserInfo(SysUser sysUser) throws ErrorException {
        QueryWrapper<SysUser> wq = new QueryWrapper<SysUser>().eq("username", sysUser.getUsername());
        if (null != sysUserMapper.selectOne(wq)) {
            throw new ErrorException("不可以重复新增用户!");
        }
        if (sysUser.getRoleIds().size() > 1) {
            throw new ErrorException("不能同时拥有2个角色!");
        }
        setCommonPassword(sysUser);
    }

    /**
     * 公共设置密码操作
     *
     * @param sysUser
     */
    private void setCommonPassword(SysUser sysUser) {
        // 默认密码
        String password = sysUser.getPassword();
        if (!StringUtils.isNotBlank(password)) {
            password = Constants.DEFULT_PASSWORD;
        }
        password = myPasswordEncoder.encode(password);
        sysUser.setPassword(password);
    }

    /**
     * 公共设置用户权限关联信息
     *
     * @param sysUser
     */
    private void setCommonUserRoles(SysUser sysUser) throws ErrorException {
        if (sysUser.getRoleIds().size() > 1) {
            throw new ErrorException("不能同时拥有2个角色!");
        }
        // 删除所有的用户和角色关联信息，重新新增
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", sysUser.getId()));
        List<SysUserRole> list = new ArrayList<>();
        for (Long roleId : sysUser.getRoleIds()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        userRoleService.saveBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateUserInfo(SysUser sysUser) throws ErrorException {
        setCommonPassword(sysUser);
        setCommonUserRoles(sysUser);
        updateById(sysUser);
        return Message.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteUserInfoByIds(List<Long> ids, HttpServletRequest httpServletRequest) throws ErrorException {
        preHandleDeleteUser(ids, httpServletRequest);

        removeByIds(ids);
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().in("user_id", ids));
        return Message.success();
    }

    /**
     * 前置处理：防止删除当前登录用户
     *
     * @param ids
     * @param httpServletRequest
     * @throws ErrorException
     */
    private void preHandleDeleteUser(List<Long> ids, HttpServletRequest httpServletRequest) throws ErrorException {
        String jwt = httpServletRequest.getHeader(jwtUtils.getHeader());

        Claims claim = jwtUtils.getClaimByToken(jwt);
        String username = claim.getSubject();
        SysUser sysUser = sysUserMapper.selectOne(
                new QueryWrapper<SysUser>().eq("username", username));
        if (ids.contains(sysUser.getId())) {
            throw new ErrorException("请勿删除当前登录账户!");
        }
    }

    @Override
    public Message getSelectRoleInfo() {
        List<CheckLable> list = new ArrayList<>();
        List<SysRole> sysroleList = sysRoleService.list(null);
        for (SysRole sysRole : sysroleList) {
            CheckLable checkLable = new CheckLable(sysRole.getId(), sysRole.getName());
            list.add(checkLable);
        }
        return list.isEmpty() ? Message.noData() : Message.success(list);
    }
}
