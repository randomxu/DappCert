package com.sdt.dapp.entity.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class SystemUser {
    @Id
    @GeneratedValue(generator = "key_uuid_36")
    @GenericGenerator(name = "key_uuid_36", strategy = "uuid2")
    private String id;

    private String loginName;

    private String userName;

    //@JsonIgnore
    private String password;

    private Timestamp createTime;

    private Timestamp modifyTime;

    // 备注
    private String remark;

    private boolean del = false;

    public SystemUser(){}

    public SystemUser(String loginName,String userName,String password,String remark){
        this.loginName = loginName;
        this.userName = userName;
        this.password = password;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
}
