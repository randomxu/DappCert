package com.sdt.dapp.utils;


public class ResponseResult {
    public static final int FAIL = -1;
    private static final int SUCCESS = 0;
    private String msg = "操作成功";

    private int code = SUCCESS;

    private Object data;

    public ResponseResult() {
        super();
    }

    public ResponseResult(String msg, Object data, int code) {
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public static ResponseResult success() {
        return success("操作成功");
    }

    public static ResponseResult passApplySuccess() {
        return success("申请信息提交成功");
    }

    public static ResponseResult success(String msg) {
        return success(msg, null);
    }

    public static ResponseResult successData(Object data) {
        return success("操作成功", data);
    }


    public static ResponseResult success(Object data) {
        return success("操作成功", data);
    }

    public static ResponseResult mutiple(){
        return new ResponseResult("出入证已申请，请勿重新提交", null, 2);
    }

    public static ResponseResult success(String msg, Object data) {
        return new ResponseResult(msg, data, SUCCESS);
    }

    public static ResponseResult error() {
        ResponseResult resultBean = new ResponseResult();
        resultBean.setCode(FAIL);
        resultBean.setMsg("操作失败");
        return resultBean;
    }

    public static ResponseResult error(String msg) {
        ResponseResult resultBean = new ResponseResult();
        resultBean.setCode(FAIL);
        resultBean.setMsg(msg);
        return resultBean;
    }


    public static int getFAIL() {
        return FAIL;
    }

    public static int getSUCCESS() {
        return SUCCESS;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
