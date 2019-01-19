package cn.edu.pku.ss.ct.consumer;


import cn.edu.pku.ss.ct.common.bean.Consumer;
import cn.edu.pku.ss.ct.consumer.bean.CallLogConsumer;

import java.io.IOException;

// 消费flume采集的数据
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        Consumer consumer = new CallLogConsumer();

        consumer.consume();

        consumer.close();

    }
}
