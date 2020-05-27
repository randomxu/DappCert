package com.sdt.dapp.repository;

import com.sdt.dapp.entity.system.SystemUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface SystemUserRepository extends PagingAndSortingRepository<SystemUser, String>, JpaSpecificationExecutor {
    List<SystemUser> findAll();

    SystemUser findByLoginNameAndDel(String loginName, boolean del);

    SystemUser findSystemUserByIdAndDel(String id, boolean del);

    @Modifying
    @Query(value = "update system_user set del = 1 where id = ?1", nativeQuery = true)
    public void deleteByUserId(String userId);

    public int countByLoginNameAndDel(String loginName, boolean del);

    public int countByLoginNameAndDelAndIdNot(String loginName, boolean del, String id);

    List<SystemUser> findByDel(boolean del);

}
