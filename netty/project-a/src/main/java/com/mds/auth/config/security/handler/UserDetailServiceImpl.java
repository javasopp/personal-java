package com.mds.auth.config.security.handler;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mds.auth.entity.SysUser;
import com.mds.auth.mapper.SysUserMapper;
import com.mds.auth.service.SysUserService;
import com.mds.auth.config.security.MyPasswordEncoder;
import com.mds.auth.dto.UserDto;
import com.mds.auth.utils.JwtUtils;
import com.mds.business.common.Constants;
import com.mds.business.common.exception.ErrorException;
import com.mds.business.common.exception.LoginErrorException;
import com.mds.business.common.message.Message;
import com.mds.business.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author sopp
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailServiceImpl implements UserDetailsService {

    /**
     * 超时时间：默认300s，即5分钟
     */
    @Value("${jwt.expiration}")
    private int expirationTime;

    private final SysUserService sysUserService;

    private final JwtUtils jwtUtils;

    private final SysUserMapper sysUserMapper;

    private final RedisUtil redisUtil;

    private final MyPasswordEncoder passwordEncoder;

    /**
     * 通过用户名查找用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     *
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {

        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        // ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = sysUserService.getUserAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

    /**
     * 用户登录的接口方法
     *
     * @param userDto
     * @return
     */
    public Message userLogin(UserDto userDto) throws ErrorException, LoginErrorException {
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", userDto.getUsername()));
        preHandUserInfo(sysUser, userDto);

        final UserDetails userDetails = this.loadUserByUsername(sysUser.getUsername());

        final String token = jwtUtils.generateToken(sysUser.getUsername());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        List<Integer> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_admin")) {
                roles.add(1);
            } else if (authority.getAuthority().equals("ROLE_normal")) {
                roles.add(2);
            }
        }

        Map<String, Object> map = new HashMap<>(4);

        map.put("token", token);
        map.put("name", sysUser.getUsername());
        map.put("userId", sysUser.getId());
        map.put("roles", roles);

        redisUtil.set(Constants.TOKEN_PRE + sysUser.getUsername(), token, expirationTime);
        return Message.success(map);
    }

    /**
     * 前置处理用户状态等信息
     *
     * @param sysUser
     * @param userDto
     * @throws ErrorException
     */
    private void preHandUserInfo(SysUser sysUser, UserDto userDto) throws ErrorException, LoginErrorException {
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名错误");
        }
        if (redisUtil.hasKey(Constants.TOKEN_PRE + sysUser.getUsername())) {
            throw new LoginErrorException("请勿重复登录！");
        }
        String httpPassword = passwordEncoder.encode(userDto.getPassword());
        if (!httpPassword.equals(sysUser.getPassword())) {
            log.info("我是密码:{},查询出来的密码:{}", httpPassword, sysUser.getPassword());
            throw new UsernameNotFoundException("密码错误");
        }
        if (0 == sysUser.getStatus()) {
            throw new ErrorException("当前用户已经被禁用了");
        }
    }

    public Message userLogout(long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            return Message.error("当前用户已经不存在了!");
        }
        // 删除登录的key
        redisUtil.del(Constants.TOKEN_PRE + sysUser.getUsername());
        redisUtil.del(Constants.ROLE_PRE + sysUser.getUsername());
        return Message.success();
    }
}
