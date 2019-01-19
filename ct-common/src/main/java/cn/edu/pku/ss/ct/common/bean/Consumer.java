package cn.edu.pku.ss.ct.common.bean;

import java.io.Closeable;

public interface Consumer extends Closeable {
    public void consume();
}
