package com.sdt.dapp.service.system;


import com.sdt.dapp.entity.system.SystemUser;
import com.sdt.dapp.repository.SystemUserRepository;
import com.sdt.dapp.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class SystemUserService {

    @Autowired
    private TableQueryUtil tableQueryUtil;

    @Autowired
    private SystemUserRepository repository;


    public PagingResult<SystemUser> findSystemUsers(PageRequest pageRequest, Map advQuery, String keyword) {
        return tableQueryUtil.findAll(advQuery, keyword, pageRequest, SystemUser.class, repository);
    }

    public ResponseResult save(SystemUser user) {
        try {
            // 新建
            if (StringUtils.isEmpty(user.getId())) {
                user.setPassword("123456");
            } else {
                SystemUser systemUser = repository.findSystemUserByIdAndDel(user.getId(), false);
                user.setPassword(systemUser.getPassword());
            }
            repository.save(user);
            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error();
        }
    }

    public ResponseResult resetPassword(String id) {
        SystemUser systemUser = repository.findSystemUserByIdAndDel(id, false);
        systemUser.setPassword("123456");
        repository.save(systemUser);
        return ResponseResult.success();
    }


    // 将enable改为0
    public ResponseResult deleteUser(String id) {
        try {
            repository.deleteByUserId(id);
            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error();
        }
    }

    // 检测登录名唯一性
    public ResponseResult checkLoginName(String loginName, String id) {
        int count = 0;
        // 新增
        if (StringUtils.isEmpty(id)) {
            count = repository.countByLoginNameAndDel(loginName, false);
        } else {
            count = repository.countByLoginNameAndDelAndIdNot(loginName, false, id);
        }
        if (count == 0)
            return ResponseResult.success();
        else
            return ResponseResult.error();
    }

    public ResponseResult updatePassword(String id, String passwordOld, String passwordNew) {
        SystemUser systemUser = repository.findSystemUserByIdAndDel(id, false);
        if (!passwordOld.equals(systemUser.getPassword())) {
            return ResponseResult.error("旧密码输入错误！");
        }
        systemUser.setPassword(passwordNew);
        repository.save(systemUser);
        return ResponseResult.success();
    }

}
