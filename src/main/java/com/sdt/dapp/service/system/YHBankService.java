package com.sdt.dapp.service.system;


import com.sdt.dapp.entity.system.SystemUser;
import com.sdt.dapp.entity.system.YHBank;
import com.sdt.dapp.repository.SystemUserRepository;
import com.sdt.dapp.repository.YHBankRepository;
import com.sdt.dapp.utils.PagingResult;
import com.sdt.dapp.utils.ResponseResult;
import com.sdt.dapp.utils.TableQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class YHBankService {

    @Autowired
    private TableQueryUtil tableQueryUtil;

    @Autowired
    private YHBankRepository repository;


    public PagingResult<YHBank> findYHBank(PageRequest pageRequest, Map advQuery, String keyword) {
        return tableQueryUtil.findAll(advQuery, keyword, pageRequest, YHBank.class, repository);
    }

    public ResponseResult deleteUser(String isStr) {
        System.out.printf("isstr:",isStr);
        if (isStr!=null &&!isStr.equals("")) {
            String[] ids = isStr.split(",");
            System.out.printf("ids:",ids);
            for (String id : ids) {
                System.out.printf("id:",id);
                if(id!=null &&!id.equals("")){
                    repository.deleteByBankid(id);
                }
            }
        }
        return ResponseResult.success();
    }

    public YHBank getcerinfo(String bankid) {
        return repository.findByBankID(bankid);
    }
}
