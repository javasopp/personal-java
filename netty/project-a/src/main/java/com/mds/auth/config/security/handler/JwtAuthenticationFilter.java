package com.mds.auth.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.mds.auth.entity.SysUser;
import com.mds.auth.service.SysUserService;
import com.mds.business.util.MyUtils;
import com.mds.auth.utils.JwtUtils;
import com.mds.business.common.Constants;
import com.mds.business.common.message.Message;
import com.mds.business.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author sopp
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Value("${jwt.list}")
    private List<String> list;

    @Autowired
    private MyUtils myUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws JwtException {
        String url = request.getRequestURI();
        boolean isContain = myUtils.contain(url, list);
        if (isContain) {
            chain.doFilter(request, response);
        } else {
            String jwt = request.getHeader(jwtUtils.getHeader());
            if (StringUtils.isEmpty(jwt)) {
                chain.doFilter(request, response);
                return;
            }

            Claims claim = jwtUtils.getClaimByToken(jwt);

            if (claim == null) {
                returnJsonResult(response, Message.tokenError("token异常!"));
                return;
            }
            if (jwtUtils.isTokenExpired(claim)) {
                returnJsonResult(response, Message.tokenError("token已过期!"));
                return;
            }
            String username = claim.getSubject();
            if (!redisUtil.hasKey(Constants.TOKEN_PRE + username)) {
                returnJsonResult(response, Message.tokenError("请重新登录!"));
                return;
            }

            // 获取用户的权限等信息

            SysUser sysUser = sysUserService.getByUsername(username);
            if (sysUser == null) {
                returnJsonResult(response, Message.tokenError("登录过期，请重新登录!"));
                return;
            }
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

            SecurityContextHolder.getContext().setAuthentication(token);

            chain.doFilter(request, response);
        }
    }

    /**
     * 返回前端处理参数,因为filter先于全局异常，无法抛出异常
     */
    private void returnJsonResult(HttpServletResponse response, Message message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSON.toJSONString(message).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
        return;
    }
}
