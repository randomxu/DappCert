package com.sdt.dapp.repository;


import com.sdt.dapp.entity.system.YHBank;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface YHBankRepository extends PagingAndSortingRepository<YHBank, String>, JpaSpecificationExecutor {
    List<YHBank> findAll();

    @Modifying
    @Query(value = "delete from yhbank where bankid = ?1", nativeQuery = true)
    void deleteByBankid(String bankid);

    YHBank findByBankID(String bankid);
}
