package com.fariy.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.fariy.blog.dao.pojo.SysUser;
import com.fariy.blog.service.LoginService;
import com.fariy.blog.service.SysUserService;
import com.fariy.blog.utils.JWTUtils;
import com.fariy.blog.vo.ErrorCode;
import com.fariy.blog.vo.Result;
import com.fariy.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final String slat = "mszlu!@#";
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1.检查参数是否合法
         * 2.根据用户名和密码去user表中是否存在
         * 3.如不存在，登录失败
         * 4.如存在，使用jwt,生成token，返回给前端
         * 5.token放入redis当中，redis   token: user 信息 设置过期时间
         * （登录认证时  先认证token字符串是否合法，再去redis认证是否存在）
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + "slat");

        SysUser sysUser = sysUserService.findUser(account,password);

        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("Token"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
