package com.mds.auth.controller;


import com.mds.auth.config.security.handler.UserDetailServiceImpl;
import com.mds.auth.dto.UserDto;
import com.mds.business.common.exception.ErrorException;
import com.mds.business.common.exception.LoginErrorException;
import com.mds.business.common.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录登出接口
 *
 * @author sopp
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final UserDetailServiceImpl userDetailService;


    /**
     * showdoc
     * @catalog 登录管理
     * @title 登录访问接口
     * @description 登录访问接口
     * @method post
     * @url /login
     * @param username  string 用户名
     * @param password  string 密码
     * @json_param {"username":"zhangsan","password":"12345678"}
     * @return {"code":0,"msg":"Successful operation","data":{"name":"zhangsan","userId":3,"token":"23213asdsadsadsada","roles":[1,2]},"count":0}
     * @return_param userId    int	用户主键
     * @return_param name   string	用户名
     * @return_param token   string	token，除开下拉菜单不需要此token，后续所有接口都需要这个token，5分钟有效期。
     * @return_param roles   int数组	角色信息，主要是id数组，1-表示超管，2-表示普通用户
     * @return_param count   int	分页信息
     * @remark 登录不需要token，都可以调用，暂时未加上加密，先调试接口。
     * @number 1
     */
    /**
     * 登录访问接口
     *
     * @param userDto
     * @return
     * @throws ErrorException
     */
    @PostMapping("/login")
    public Message login(@RequestBody UserDto userDto) throws ErrorException, LoginErrorException {
        return userDetailService.userLogin(userDto);
    }


    /**
     * showdoc
     * @catalog 登录管理
     * @title 退出登录
     * @description 退出登录
     * @method get
     * @url /logout/{单个用户id}
     * @return {"code":0,"msg":"Successful operation","data":{"id":1,"username":"admin","password":"96e79218965eb72c92a549dd5a330112","createTime":20210112221353,"updateTime":20210116165732,"lastLogin":20201230083837,"status":1,"checkLableList":[6,3],"roleName":null,"roleIds":[]},"count":0}
     * @return_param code    int	状态码，对应状态码，请查看状态码表
     * @return_param msg   string	返回信息
     * @return_param data   string	数据信息，如果是新增，修改，删除等操作，此处均无需要处理的内容
     * @return_param count   int	分页统计
     * @remark 必须在header带token才可以。token过期时间为 : 5分钟。token实例："token":"sdfsejfkdsjfklsdjflkdsjflksdjfklsjfldks"。
     * @number 2
     */
    /**
     * 退出登录
     * @param userId
     * @return
     */
    @GetMapping("/logout/{userId}")
    public Message logout(@PathVariable("userId") long userId) {
        return userDetailService.userLogout(userId);
    }
}
