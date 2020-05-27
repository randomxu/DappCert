package com.sdt.dapp.pojo.system;

import com.sdt.dapp.entity.system.SystemUser;

import java.util.ArrayList;
import java.util.List;

public class SelectPojo {
    private String name;

    private Object value;

    public SelectPojo(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static List<SelectPojo> getUsers(List<SystemUser> users) {
        List<SelectPojo> lists = new ArrayList<SelectPojo>();
        for (SystemUser user : users) {
            lists.add(new SelectPojo(user.getUserName(), user.getLoginName()));
        }
        return lists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
