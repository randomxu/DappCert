package com.sdt.dapp.entity.system;

import java.io.Serializable;
import java.sql.Timestamp;


public class YHRevoke{
    private Integer id;
    private String userName;
    private String userID;
    private String userPhone;
    private String userBankID;
    private Timestamp userRegDate;
    private Timestamp bankEndDate;
    private String state;
    private String judge;

    public YHRevoke(){}

    public YHRevoke(Integer id, String userName, String userID, String userPhone, String userBankID, Timestamp userRegDate, Timestamp bankEndDate, String state, String judge) {
        this.id = id;
        this.userName = userName;
        this.userID = userID;
        this.userPhone = userPhone;
        this.userBankID = userBankID;
        this.userRegDate = userRegDate;
        this.bankEndDate = bankEndDate;
        this.state = state;
        this.judge = judge;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }
}
