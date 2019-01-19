package cn.edu.pku.ss.ct.producer;

import cn.edu.pku.ss.ct.common.bean.Producer;
import cn.edu.pku.ss.ct.producer.bean.LocalFileProducer;
import cn.edu.pku.ss.ct.producer.io.LocalFileDataIn;
import cn.edu.pku.ss.ct.producer.io.LocalFileDataOut;

import java.io.IOException;


// 生产数据供kafka消费
public class Bootstrap {
    public static void main(String args[]) throws IOException {

        if (args.length < 2) {
            System.out.println("参数不正确！");
            System.exit(1);
        }


        Producer producer = new LocalFileProducer();

        producer.setIn(new LocalFileDataIn(args[0]));
        producer.setOut(new LocalFileDataOut(args[1]));

        producer.produce();

        producer.close();

    }
}
