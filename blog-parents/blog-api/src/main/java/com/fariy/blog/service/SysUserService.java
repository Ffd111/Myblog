package com.fariy.blog.service;

import com.fariy.blog.dao.pojo.SysUser;
import com.fariy.blog.vo.Result;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);
}
