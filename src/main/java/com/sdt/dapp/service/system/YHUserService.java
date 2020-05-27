package com.sdt.dapp.service.system;


import com.sdt.dapp.entity.system.YHBank;
import com.sdt.dapp.entity.system.YHUser;
import com.sdt.dapp.repository.YHBankRepository;
import com.sdt.dapp.repository.YHUserRepository;
import com.sdt.dapp.utils.PagingResult;
import com.sdt.dapp.utils.ResponseResult;
import com.sdt.dapp.utils.TableQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

@Service
public class YHUserService {

    @Autowired
    private TableQueryUtil tableQueryUtil;

    @Autowired
    private YHUserRepository repository;


    public PagingResult<YHUser> findYHUser(PageRequest pageRequest, Map advQuery, String keyword) {
        return tableQueryUtil.findAll(advQuery, keyword, pageRequest, YHUser.class, repository);
    }

    public ResponseResult addUser(YHUser bankUser) {
        try{
            bankUser.setUserName(bankUser.getUserName());
            bankUser.setUserID(bankUser.getUserID());
            bankUser.setUserPhone(bankUser.getUserPhone());
            bankUser.setUserBankID(bankUser.getUserBankID());
            bankUser.setUserEncIndex(1);
            repository.save(bankUser);
        }catch(Exception e){
            return ResponseResult.error();
        }
        return ResponseResult.success();
    }

    public ResponseResult deleteUser(String isStr) {
        System.out.printf("isstr:",isStr);
        if (isStr!=null &&!isStr.equals("")) {
            String[] ids = isStr.split(",");
            System.out.printf("ids:",ids);
            for (String id : ids) {
                System.out.printf("id:",id);
                if(id!=null &&!id.equals("")){
                    repository.deleteuseId(id);
                }
            }
        }
        return ResponseResult.success();
    }

    public YHUser getcerinfo(String bankid) {
        return repository.findByUserName(bankid);
    }
}
