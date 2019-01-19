package cn.edu.pku.ss.ct.common.bean;

import java.io.Closeable;

public interface Producer extends Closeable {
    //   生产数据
    public void produce();

    public void setIn(DataIn in);

    public void setOut(DataOut out);
}
