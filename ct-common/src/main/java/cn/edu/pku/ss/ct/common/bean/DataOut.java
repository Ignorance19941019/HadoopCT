package cn.edu.pku.ss.ct.common.bean;

import java.io.Closeable;

public interface DataOut extends Closeable {
    public void setPath(String outPath);

    public void write(Object data) throws Exception;
    public void write(String data) throws Exception;

}
