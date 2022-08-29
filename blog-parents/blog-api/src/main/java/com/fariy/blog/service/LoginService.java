package com.fariy.blog.service;

import com.fariy.blog.dao.pojo.SysUser;
import com.fariy.blog.vo.Result;
import com.fariy.blog.vo.params.LoginParam;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);
}
