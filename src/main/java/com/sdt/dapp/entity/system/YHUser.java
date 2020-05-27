package com.sdt.dapp.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class YHUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="userName",nullable=false,length=100)
    private String userName;
    @Column(name="userID",nullable=false,length=18,unique=true)
    private String userID;
    @Column(name="userPhone",nullable=false,length=20,unique=true)
    private String userPhone;
    @Column(name="userBankID",length=18)
    private String userBankID;
    @Column(name="userSigCert",length=4000)
    private String userSigCert;
    @Column(name="userEncCert",length=4000)
    private String userEncCert;
    @Column(name="userSigPk",length=640)
    private String userSigPk;
    @Column(name="userEncPk",length=640)
    private String userEncPk;
    @Column(name="userEncIndex",nullable=false)
    private Integer userEncIndex;
    @Column(name="userRegDate")
    private Timestamp userRegDate;
    @Column(name="bankEndDate")
    private Timestamp bankEndDate;

    public YHUser(){}

    public YHUser(String userName, String userID, String userPhone, String userBankID, String userSigCert, String userEncCert, String userSigPk, String userEncPk, Integer userEncIndex, Timestamp userRegDate, Timestamp bankEndDate) {
        this.userName = userName;
        this.userID = userID;
        this.userPhone = userPhone;
        this.userBankID = userBankID;
        this.userSigCert = userSigCert;
        this.userEncCert = userEncCert;
        this.userSigPk = userSigPk;
        this.userEncPk = userEncPk;
        this.userEncIndex = userEncIndex;
        this.userRegDate = userRegDate;
        this.bankEndDate = bankEndDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserBankID() {
        return userBankID;
    }

    public void setUserBankID(String userBankID) {
        this.userBankID = userBankID;
    }

    public String getUserSigCert() {
        return userSigCert;
    }

    public void setUserSigCert(String userSigCert) {
        this.userSigCert = userSigCert;
    }

    public String getUserEncCert() {
        return userEncCert;
    }

    public void setUserEncCert(String userEncCert) {
        this.userEncCert = userEncCert;
    }

    public String getUserSigPk() {
        return userSigPk;
    }

    public void setUserSigPk(String userSigPk) {
        this.userSigPk = userSigPk;
    }

    public String getUserEncPk() {
        return userEncPk;
    }

    public void setUserEncPk(String userEncPk) {
        this.userEncPk = userEncPk;
    }

    public Integer getUserEncIndex() {
        return userEncIndex;
    }

    public void setUserEncIndex(Integer userEncIndex) {
        this.userEncIndex = userEncIndex;
    }

    public Timestamp getUserRegDate() {
        return userRegDate;
    }

    public void setUserRegDate(Timestamp userRegDate) {
        this.userRegDate = userRegDate;
    }

    public Timestamp getBankEndDate() {
        return bankEndDate;
    }

    public void setBankEndDate(Timestamp bankEndDate) {
        this.bankEndDate = bankEndDate;
    }
}
