package com.mds.auth.config.security.handler;


import com.mds.auth.utils.CommonAccessReturnHandler;
import com.mds.business.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt权限异常。
 * @author sopp
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private CommonAccessReturnHandler commonAccessReturnHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        commonAccessReturnHandler.callBackAccessProcess(response, Message.error(accessDeniedException.getMessage()));
    }
}
