package com.fariy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fariy.blog.dao.mapper.SysUserMapper;
import com.fariy.blog.dao.pojo.SysUser;
import com.fariy.blog.service.LoginService;
import com.fariy.blog.service.SysUserService;
import com.fariy.blog.vo.ErrorCode;
import com.fariy.blog.vo.LoginUserVo;
import com.fariy.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysuser = sysUserMapper.selectById(id);
        if (sysuser == null){
            sysuser = new SysUser();
            sysuser.setNickname("无名");
        }
        return sysuser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *      是否为空，解析是否成功，redis是否存在
         * 2.如果叫校验失败，返回错误
         * 3。如果成功，返回用户数据 id,account,nickname,avatar,可用LoginUserVo包装
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());

        return Result.success(loginUserVo);
    }
}
