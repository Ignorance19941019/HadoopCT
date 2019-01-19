package cn.edu.pku.ss.ct.producer.io;

import cn.edu.pku.ss.ct.common.bean.Data;
import cn.edu.pku.ss.ct.common.bean.DataIn;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalFileDataIn implements DataIn {

    private BufferedReader reader = null;

    public LocalFileDataIn(String inPath) {
        setPath(inPath);
    }

    public void setPath(String inPath) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inPath), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Object read() throws IOException {
        return null;
    }



    public <T extends Data> List<T> read(Class<T> clazz) throws IOException {
        List<T> ts = new ArrayList<T>();


        try {
            String line =null;
            while ((line = reader.readLine()) != null) {
                T t = clazz.newInstance();
                t.setValue(line);
                ts.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ts;
    }

    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }
}
