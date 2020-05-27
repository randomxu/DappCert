package com.sdt.dapp.entity.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

public class RootcaMng {
    public static String PATH = "E:\\1_ca_new\\cacert.pem";
    public static String REQPATH = "E:\\1_ca_new\\";
    public static String EXPORT_PATH = "C:\\Users\\sdt\\Desktop\\cert\\";
    public static String STORE_PATH = "E:\\1_ca_new\\userCert\\";
    public static String CMD_START = "cmd /c e: & cd E:\\1_ca_new & ";
    public static String GEN_SIG_SK = "c:\\openssl\\bin\\openssl genpkey -engine egsm -algorithm SM2 -out ";
    public static String GEN_CER_REQ1 = "c:\\openssl\\bin\\openssl req -engine egsm -new -out ";
    public static String GEN_CER_REQ2 = " -subj /CN=%1/OU=XA/O=SDT/C=CN -batch";
    public static String GEN_ENC_SK = "c:\\openssl\\bin\\openssl genpkey -engine egsm -algorithm SM2 -out ";
    public static String GEN_ENCER_REQ1 = "c:\\openssl\\bin\\openssl req -engine egsm -new -out ";
    public static String GEN_ENCCER_REQ2 = " -subj /CN=%1/OU=XA/O=SDT/C=CN -batch";
    public static String GEN_PEM = "c:\\openssl\\bin\\openssl ca -engine egsm -config openssl_sig.cnf -name CA_default -days 3650 -policy policy_anything -cert cacert.pem -in ";
    public static String GEN_ENCPEM = "c:\\openssl\\bin\\openssl ca -engine egsm -config openssl_enc.cnf -name CA_default -days 3650 -policy policy_anything -cert cacert.pem -in ";

    private String certName;
    private String pkVer;
    private Date issueTime;
    private Date endTime;

    public RootcaMng(String certName, String pkVer, Date issueTime, Date endTime) {
        this.certName = certName;
        this.pkVer = pkVer;
        this.issueTime = issueTime;
        this.endTime = endTime;
    }

    public RootcaMng(){}

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getPkVer() {
        return pkVer;
    }

    public void setPkVer(String pkVer) {
        this.pkVer = pkVer;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
