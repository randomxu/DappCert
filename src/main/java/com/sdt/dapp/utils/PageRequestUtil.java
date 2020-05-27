package com.sdt.dapp.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class PageRequestUtil {


    String sorterSpilt = "_";
    String pageSizeName = "pageSize";
    String currentName = "current";
    String sorterName = "sorter";
    String descendingName = "descend";

    /**
     * 根据传入的参数封装为PageRequest对象
     *
     * @param map
     * @return
     */
    public PageRequest genericPageRequestByRequest(Map map) {
        PageRequest pageRequest;
        Sort sort;
        Integer pageSize = (Integer) map.get(pageSizeName);
        Integer pageNumber = (Integer) map.get(currentName);
        String sorter = (String) map.get(sorterName);
        String sortName;
        String sortOrder = null;
        if (sorter != null && !StringUtils.isEmpty(sorter)) {
            if (sorter.contains(sorterSpilt) && !sorter.endsWith(sorterSpilt)) {
                sortOrder = sorter.split(sorterSpilt)[1];
            }
            sortName = sorter.split(sorterSpilt)[0];
            Sort.Direction direction = Sort.Direction.ASC;
            if (descendingName.equalsIgnoreCase(sortOrder)) {
                direction = Sort.Direction.DESC;
            }
            Sort.Order order = new Sort.Order(direction, sortName);
            sort = Sort.by(order);
            pageRequest = PageRequest.of(
                    pageNumber - 1,
                    pageSize,
                    sort);
        }else{
            pageRequest = PageRequest.of(
                    pageNumber - 1,
                    pageSize);
        }
        return pageRequest;
    }

}
