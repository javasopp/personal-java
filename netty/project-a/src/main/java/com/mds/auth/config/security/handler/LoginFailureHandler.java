package com.mds.auth.config.security.handler;


import com.mds.auth.utils.CommonAccessReturnHandler;
import com.mds.business.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败的回调处理
 * @author sopp
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private CommonAccessReturnHandler commonAccessReturnHandler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        commonAccessReturnHandler.callBackAccessProcess(response, Message.error("用户名或密码错误"));
    }
}
