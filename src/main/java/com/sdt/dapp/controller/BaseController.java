package com.sdt.dapp.controller;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class BaseController {


    protected Map getMap(Map<String, Object> paramMap) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> advQuery = new LinkedHashMap<>();
        for (String key : paramMap.keySet()) {
            if ("page".equals(key) || "limit".equals(key)) {
                if ("page".equals(key))
                    map.put("current", Integer.parseInt(paramMap.get(key).toString()));
                else
                    map.put("pageSize", Integer.parseInt(paramMap.get(key).toString()));
            } else {
                advQuery.put(key, paramMap.get(key));
            }
        }
        map.put("advQuery", advQuery);
        map.put("keyword", "");
        return map;
    }

}
