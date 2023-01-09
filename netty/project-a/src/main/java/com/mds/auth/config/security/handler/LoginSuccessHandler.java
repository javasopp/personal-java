package com.mds.auth.config.security.handler;


import com.mds.auth.utils.CommonAccessReturnHandler;
import com.mds.auth.utils.JwtUtils;
import com.mds.business.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功的回调
 * @author sopp
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private CommonAccessReturnHandler commonAccessReturnHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 生成jwt，并放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), jwt);
        commonAccessReturnHandler.callBackAccessProcess(response, Message.success());
    }

}
