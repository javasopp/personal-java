package com.mds.auth.config.security.handler;


import com.mds.auth.utils.CommonAccessReturnHandler;
import com.mds.business.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理拦截jwt异常信息
 * @author sopp
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private CommonAccessReturnHandler commonAccessReturnHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        commonAccessReturnHandler.callBackAccessProcess(response, Message.tokenError("请先登录"));
    }
}
