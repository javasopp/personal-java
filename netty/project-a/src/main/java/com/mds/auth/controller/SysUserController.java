package com.mds.auth.controller;


import com.mds.auth.dto.CommonPage;
import com.mds.auth.entity.SysUser;
import com.mds.auth.service.SysUserService;
import com.mds.business.common.exception.ErrorException;
import com.mds.business.common.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author sopp
 * @since 2021-04-05
 */
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * showdoc
     * @catalog 权限管理
     * @title 获取用户列表
     * @description 获取用户列表
     * @method post
     * @url /sys/user/list
     * @param current  int 当前页
     * @param size  int 每页大小
     * @param searchInfo  string 搜索内容：用户名，描述。
     * @json_param {"current":1,"size":10}
     * @return {"code":0,"msg":"Successful operation","data":[{"id":1,"username":"admin","password":"96e79218965eb72c92a549dd5a330112","createTime":20210112221353,"updateTime":20210116165732,"lastLogin":20201230083837,"status":1,"checkLableList":null,"roleName":"超级管理员,普通用户","roleIds":[]}],"count":2}
     * @return_param id    int	主键
     * @return_param username   string	用户名
     * @return_param password   string	密码
     * @return_param lastLogin   int	上次登录时间(时间戳)
     * @return_param roleName   string	菜单上的角色名称展示
     * @return_param roldIds   int数组	返回前端用于展示角色的id集合。
     * @return_param status   int	状态:1-启用，暂时只有启用。
     * @return_param createTime   bigint	创建时间(时间戳)
     * @return_param updateTime   bigint	更新时间(时间戳)
     * @remark 必须在header带token才可以。token过期时间为 : 5分钟。token实例："token":"sdfsejfkdsjfklsdjflkdsjflksdjfklsjfldks"。
     * @number 1
     */
    /**
     * 获取用户列表
     *
     * @param commonPage
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Message list(@Valid @RequestBody CommonPage commonPage) {
        return sysUserService.getUserInfoList(commonPage);
    }

    /**
     * showdoc
     * @catalog 权限管理
     * @title 保存用户信息
     * @description 保存用户信息
     * @method post
     * @url /sys/user/save
     * @param username  string 用户名
     * @param password  string 密码
     * @param roleIds  int数组 角色id数组，从下拉框中获取选中的key的值
     * @param description  string 描述
     * @json_param {"username":"zhangsan","password":"123456","roleIds":[6,3],"description":"测试用户新增"}
     * @return {"code":0,"msg":"Successful operation","data":null,"count":0}
     * @return_param code    int	状态码，对应状态码，请查看状态码表
     * @return_param msg   string	返回信息
     * @return_param data   string	数据信息，如果是新增，修改，删除等操作，此处均无需要处理的内容
     * @return_param count   int	分页统计
     * @remark 必须在header带token才可以。token过期时间为 : 5分钟。token实例："token":"sdfsejfkdsjfklsdjflkdsjflksdjfklsjfldks"。
     * @number 3
     */
    /**
     * 保存用户信息
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Message save(@Valid @RequestBody SysUser sysUser) throws ErrorException {
        return sysUserService.saveUserInfo(sysUser);
    }


    /**
     * showdoc
     * @catalog 权限管理
     * @title 更新用户信息
     * @description 更新用户信息
     * @method post
     * @url /sys/user/update
     * @param id  int id主键
     * @param username  string 用户名
     * @param password  string 密码
     * @param roleIds  int数组 角色id数组，从下拉框中获取选中的key的值
     * @param description  string 描述
     * @json_param {"id":3,"username":"zhangsan","password":"12345678","roleIds":[6],"description":"测试用户修改"}
     * @return {"code":0,"msg":"Successful operation","data":null,"count":0}
     * @return_param code    int	状态码，对应状态码，请查看状态码表
     * @return_param msg   string	返回信息
     * @return_param data   string	数据信息，如果是新增，修改，删除等操作，此处均无需要处理的内容
     * @return_param count   int	分页统计
     * @remark 必须在header带token才可以。token过期时间为 : 5分钟。token实例："token":"sdfsejfkdsjfklsdjflkdsjflksdjfklsjfldks"。
     * @number 4
     */
    /**
     * 更新用户信息
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Message update(@Valid @RequestBody SysUser sysUser) throws ErrorException {
        return sysUserService.updateUserInfo(sysUser);
    }


    /**
     * showdoc
     * @catalog 权限管理
     * @title 批量删除用户信息
     * @description 批量删除用户信息
     * @method post
     * @url /sys/user/delete
     * @param id  int数组 用户的主键id集合
     * @json_param [4, 5]
     * @return {"code":0,"msg":"Successful operation","data":null,"count":0}
     * @return_param code    int	状态码，对应状态码，请查看状态码表
     * @return_param msg   string	返回信息
     * @return_param data   string	数据信息，如果是新增，修改，删除等操作，此处均无需要处理的内容
     * @return_param count   int	分页统计
     * @remark 必须在header带token才可以。token过期时间为 : 5分钟。token实例："token":"sdfsejfkdsjfklsdjflkdsjflksdjfklsjfldks"。
     * @number 5
     */
    /**
     * 批量删除用户信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Message delete(@RequestBody List<Long> ids, HttpServletRequest httpServletRequest) throws ErrorException {
        return sysUserService.deleteUserInfoByIds(ids, httpServletRequest);
    }


    /**
     * showdoc
     * @catalog 权限管理
     * @title 查询权限下拉菜单
     * @description 查询权限下拉菜单
     * @method get
     * @url /sys/user/select
     * @return {"code":0,"msg":"Successful operation","data":[{"key":3,"value":"普通用户"},{"key":6,"value":"超级管理员"}],"count":0}
     * @return_param key int 角色主键
     * @return_param value string value值
     * @remark 这里是备注信息
     * @number 6
     */
    /**
     * 查询下拉菜单
     *
     * @return
     */
    @GetMapping("/select")
    public Message getSelectRoleInfo() {
        return sysUserService.getSelectRoleInfo();
    }
}
