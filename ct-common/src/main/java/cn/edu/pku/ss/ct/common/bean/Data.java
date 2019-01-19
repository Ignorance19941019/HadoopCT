package cn.edu.pku.ss.ct.common.bean;

public abstract class Data implements Val {

    public String content;

    public void setValue(Object value) {
        content = (String)value;
    }

    public String getValue() {
        return content;
    }
}
