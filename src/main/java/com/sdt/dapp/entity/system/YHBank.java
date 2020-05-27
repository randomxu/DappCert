package com.sdt.dapp.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

@Entity
public class YHBank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="bankName",nullable=false,length=200,unique=true)
    private String bankName;
    @Column(name="bankID",nullable=false,length=18,unique=true)
    private String bankID;
    @Column(name="bankType",nullable=false,length=1)
    private String bankType;
    @Column(name="bankSigReq",nullable=false,length=4000)
    private String bankSigReq;
    @Column(name="bankSigCert",length=4000)
    private String bankSigCert;
    @Column(name="bankEncCert",length=4000)
    private String bankEncCert;
    @Column(name="bankSigPk",length=640)
    private String bankSigPk;
    @Column(name="bankEncPk",length=640)
    private String bankEncPk;
    @Column(name="bankEncIndex",nullable=false,unique=true)
    private Integer bankEncIndex;
    @Column(name="bankRegDate")
    private Timestamp bankRegDate;
    @Column(name="bankEndDate")
    private Timestamp bankEndDate;

    public YHBank(){}
    public YHBank(String bankName, String bankID, String bankType, String bankSigReq, String bankSigCert, String bankEncCert, String bankSigPk, String bankEncPk, Integer bankEncIndex, Timestamp bankRegDate, Timestamp bankEndDate) {
        this.bankName = bankName;
        this.bankID = bankID;
        this.bankType = bankType;
        this.bankSigReq = bankSigReq;
        this.bankSigCert = bankSigCert;
        this.bankEncCert = bankEncCert;
        this.bankSigPk = bankSigPk;
        this.bankEncPk = bankEncPk;
        this.bankEncIndex = bankEncIndex;
        this.bankRegDate = bankRegDate;
        this.bankEndDate = bankEndDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankSigReq() {
        return bankSigReq;
    }

    public void setBankSigReq(String bankSigReq) {
        this.bankSigReq = bankSigReq;
    }

    public String getBankSigCert() {
        return bankSigCert;
    }

    public void setBankSigCert(String bankSigCert) {
        this.bankSigCert = bankSigCert;
    }

    public String getBankEncCert() {
        return bankEncCert;
    }

    public void setBankEncCert(String bankEncCert) {
        this.bankEncCert = bankEncCert;
    }

    public String getBankSigPk() {
        return bankSigPk;
    }

    public void setBankSigPk(String bankSigPk) {
        this.bankSigPk = bankSigPk;
    }

    public String getBankEncPk() {
        return bankEncPk;
    }

    public void setBankEncPk(String bankEncPk) {
        this.bankEncPk = bankEncPk;
    }

    public Integer getBankEncIndex() {
        return bankEncIndex;
    }

    public void setBankEncIndex(Integer bankEncIndex) {
        this.bankEncIndex = bankEncIndex;
    }

    public Timestamp getBankRegDate() {
        return bankRegDate;
    }

    public void setBankRegDate(Timestamp bankRegDate) {
        this.bankRegDate = bankRegDate;
    }

    public Timestamp getBankEndDate() {
        return bankEndDate;
    }

    public void setBankEndDate(Timestamp bankEndDate) {
        this.bankEndDate = bankEndDate;
    }
}
