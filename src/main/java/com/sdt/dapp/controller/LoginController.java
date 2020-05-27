package com.sdt.dapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdt.dapp.entity.system.SystemUser;
import com.sdt.dapp.repository.SystemUserRepository;
import com.sdt.dapp.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("")
public class LoginController {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private SystemUserRepository repository;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    //退出页面
    @GetMapping("/logout")
    public String logoutPage() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseResult login(@RequestBody SystemUser systemUser){

        SystemUser user = repository.findByLoginNameAndDel(systemUser.getUserName(),false);
        if (null == user){
            return  new ResponseResult("用户名错误",null,ResponseResult.getFAIL());
        }
        if (!user.getPassword().equals(systemUser.getPassword())){
            return  new ResponseResult("密码错误",null,ResponseResult.getFAIL());
        }
        return  new ResponseResult("登录成功",null,ResponseResult.getSUCCESS());
    }



    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    @ResponseBody
    @PostMapping(value = "/savelogin")
    public ResponseResult savelogin(@RequestBody String userInfo,HttpServletRequest request) throws Exception {
        JSONObject json = JSONObject.parseObject(userInfo);
        request.getSession().setAttribute("username", json.get("userName"));
        request.getSession().setAttribute("password", json.get("password"));
        System.out.println(request.getSession().getAttribute("username"));
        System.out.println(request.getSession().getAttribute("password"));
        return ResponseResult.success();
    }

    @ResponseBody
    @GetMapping(value = "/getlogin")
    public ModelMap getlogin(HttpServletRequest request) throws Exception {
        ModelMap map = new ModelMap();
        map.put("username",request.getSession().getAttribute("username"));
        return map;
    }
}
