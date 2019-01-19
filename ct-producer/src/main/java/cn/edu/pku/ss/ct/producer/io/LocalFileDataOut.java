package cn.edu.pku.ss.ct.producer.io;

import cn.edu.pku.ss.ct.common.bean.DataOut;

import java.io.*;

public class LocalFileDataOut implements DataOut {
    private PrintWriter writer = null;

    public LocalFileDataOut(String outPath) {
        setPath(outPath);
    }

    public void setPath(String outPath) {
        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outPath), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(Object data) throws Exception {
        write(data.toString());
    }

    public void write(String data) throws Exception {
        writer.println(data);
        writer.flush();
    }

    public void close() throws IOException {
        if (writer != null)
            writer.close();
    }
}
