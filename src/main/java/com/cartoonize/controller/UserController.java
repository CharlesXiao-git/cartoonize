package com.cartoonize.controller;

import com.cartoonize.model.vo.User;
import com.cartoonize.service.IUserService;
import com.cartoonize.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@Api(tags = "User API Interfaces")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/registry/{username}{password}")
    @ApiOperation("Registry user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username", defaultValue = "",required = true),
            @ApiImplicitParam(name = "password", value = "password",  defaultValue = "",required = true)
    })
    public String registry(@RequestParam(required = true) String username,@RequestParam(required = true) String password) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        this.userService.saveUser(user);
        return username + " created";
    }

    @GetMapping("/getToken")
    @ApiOperation("Get token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username", defaultValue = "",required = true),
            @ApiImplicitParam(name = "password", value = "password",  defaultValue = "",required = true)
    })
    public String getToken(@RequestParam(required = true) String username,@RequestParam(required = true) String password) {
        User user = this.userService.findByUserName(username);
        String token = null;
        if(Objects.nonNull(user)){
            if (user.getPassword()!= null && user.getPassword().equals(password) ){
                token = TokenUtils.sign(user);
            }else{
                token = "password is incorrect.";
            }
        }
        return token;
    }

    @GetMapping("/listUser")
    @ApiOperation(value = "List Users")
    public String listUser() {
        List<User> users = userService.findAll();
        JSONObject json = new JSONObject();
        json.put("users",users);
        return json.toString();
    }

}
