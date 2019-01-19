package cn.edu.pku.ss.ct.common.constant;

import cn.edu.pku.ss.ct.common.bean.Val;

public enum Names implements Val {
    NAMESPACE("ct"),
    TABLE("ct:calllog"),
    CF_CALLER("caller"),
    CF_CALLEE("callee"),
    CF_INFO("info"),
    TOPIC("ct");

    private String name;

    private Names(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.name = (String) value;
    }

    public String getValue() {
        return name;
    }
}
