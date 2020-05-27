package com.sdt.dapp.controller.system;

import com.sdt.dapp.controller.BaseController;
import com.sdt.dapp.entity.system.SystemUser;
import com.sdt.dapp.repository.SystemUserRepository;
import com.sdt.dapp.service.system.SystemUserService;
import com.sdt.dapp.utils.PageRequestUtil;
import com.sdt.dapp.utils.PagingResult;
import com.sdt.dapp.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/systemUser")
public class SystemUserController extends BaseController {
    @Autowired
    private PageRequestUtil pageRequestUtil;

    @Autowired
    private SystemUserService service;


    @Autowired
    private SystemUserRepository repository;

    @GetMapping(value = "")
    public String systemUserPage() {
        return "system/userManager";
    }

    @ResponseBody
    @GetMapping(value = "/findAll")
    public PagingResult<SystemUser> findAll(@RequestParam Map<String, Object> params) {
        Map map = getMap(params);
        Map advQuery = (Map) map.get("advQuery");
        advQuery.put("del", 0);
        PageRequest pageRequest = pageRequestUtil.genericPageRequestByRequest(map);
        return service.findSystemUsers(pageRequest, advQuery, "");
    }


    @ResponseBody
    @GetMapping(value = "/resetPassword/{id}")
    public ResponseResult resetPassword(@PathVariable("id") String id) {
        return service.resetPassword(id);
    }

    @GetMapping(value = "/editPasswordPage")
    public String editPasswordPage() {
        return "system/editPassword";
    }

    @ResponseBody
    @PostMapping(value = "/updatePassword")
    public ResponseResult updatePassword(@RequestBody SystemUser systemUser) {
        return service.updatePassword(systemUser.getId(), systemUser.getUserName(), systemUser.getRemark());
    }


    @ResponseBody
    @PostMapping(value = "/save")
    public ResponseResult save(@RequestBody SystemUser systemUser) {
        return service.save(systemUser);
    }


    @ResponseBody
    @GetMapping(value = "/deleteUser/{id}")
    public ResponseResult deleteUser(@PathVariable("id") String id) {
        return service.deleteUser(id);
    }


    @ResponseBody
    @PostMapping(value = "/checkLoginName")
    public ResponseResult checkLoginName(@RequestParam(value = "loginName") String loginName, @RequestParam(value = "id") String id) {
        return service.checkLoginName(loginName, id);
    }
}
