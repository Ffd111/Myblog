package com.fariy.blog.service.impl;

import com.fariy.blog.dao.mapper.SysUserMapper;
import com.fariy.blog.dao.pojo.SysUser;
import com.fariy.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysuser = sysUserMapper.selectById(id);
        if (sysuser == null){
            sysuser = new SysUser();
            sysuser.setNickname("无名");
        }
        return sysuser;
    }
}
