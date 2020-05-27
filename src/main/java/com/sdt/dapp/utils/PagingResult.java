package com.sdt.dapp.utils;

import org.springframework.data.domain.Page;

import java.util.Iterator;

public class PagingResult<T> {

    private long count;
    private int code;
    private Iterator<T> data;

    public PagingResult(Page<T> page) {
        data = page.iterator();
        count = page.getTotalElements();
    }

    public PagingResult(){

    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Iterator<T> getData() {
        return data;
    }

    public void setData(Iterator<T> data) {
        this.data = data;
    }
}
