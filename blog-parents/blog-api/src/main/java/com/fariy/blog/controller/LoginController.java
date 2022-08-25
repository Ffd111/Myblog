package com.fariy.blog.controller;

import com.fariy.blog.service.LoginService;
import com.fariy.blog.vo.Result;
import com.fariy.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping()
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
