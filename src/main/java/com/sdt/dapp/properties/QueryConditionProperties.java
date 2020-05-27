package com.sdt.dapp.properties;

import java.util.HashMap;
import java.util.Map;

public class QueryConditionProperties {
    String split = "_";
    String range = "range";
    String lessThan = "lt";
    String lessEqual = "le";
    String moreThan = "mt";
    String moreEqual = "me";
    String like = "like";
    String in = "in";
    String isNull = "null";
    String notEqual = "notEqual";


    public Map<String, String> getConditionMap() {
        Map<String, String> conditionMap = new HashMap();
        conditionMap.put("split", "_");
        conditionMap.put(getRange(), "range");
        conditionMap.put(getLessThan(), "lessThan");
        conditionMap.put(getLessEqual(), "lessEqual");
        conditionMap.put(getMoreThan(), "moreThan");
        conditionMap.put(getMoreEqual(), "moreEqual");
        conditionMap.put(getLike(), "like");
        conditionMap.put(getIn(), "in");
        conditionMap.put(getIsNull(), "null");
        conditionMap.put(getNotEqual(), "notEqual");
        return conditionMap;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getLessThan() {
        return lessThan;
    }

    public void setLessThan(String lessThan) {
        this.lessThan = lessThan;
    }

    public String getLessEqual() {
        return lessEqual;
    }

    public void setLessEqual(String lessEqual) {
        this.lessEqual = lessEqual;
    }

    public String getMoreThan() {
        return moreThan;
    }

    public void setMoreThan(String moreThan) {
        this.moreThan = moreThan;
    }

    public String getMoreEqual() {
        return moreEqual;
    }

    public void setMoreEqual(String moreEqual) {
        this.moreEqual = moreEqual;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getNotEqual() {
        return notEqual;
    }

    public void setNotEqual(String notEqual) {
        this.notEqual = notEqual;
    }
}
