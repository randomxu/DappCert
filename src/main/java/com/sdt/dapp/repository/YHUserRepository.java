package com.sdt.dapp.repository;


import com.sdt.dapp.entity.system.YHBank;
import com.sdt.dapp.entity.system.YHUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface YHUserRepository extends PagingAndSortingRepository<YHUser, String>, JpaSpecificationExecutor {
    List<YHUser> findAll();

    @Modifying
    @Query(value = "delete from yhuser where bankid = ?1", nativeQuery = true)
    void deleteByBankid(String bankid);

    @Modifying
    @Query(value = "delete from yhuser where id = ?1", nativeQuery = true)
    void deleteuseId(String id);

    YHUser findByUserName(String userName);


    @Query(value = "select * from yhuser where (date_format(user_reg_date,'%Y-%m-%d') between ?1 and ?2) or (userid=?3) or (user_name=?4) or (user_bankid=?5)", nativeQuery = true)
    List<YHUser> search(String startTime,String endTime,String userID,String userName,String userBankID);

    @Query(value = "select * from yhuser where (date_format(user_reg_date,'%Y-%m-%d') between ?1 and ?2) or (userid=?3) or (user_bankid=?4)", nativeQuery = true)
    List<YHUser> searchNoUserName(String startTime,String endTime,String userID,String userBankID);
}
