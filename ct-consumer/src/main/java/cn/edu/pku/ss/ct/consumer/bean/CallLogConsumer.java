package cn.edu.pku.ss.ct.consumer.bean;

import cn.edu.pku.ss.ct.common.bean.Consumer;
import cn.edu.pku.ss.ct.common.constant.Names;
import cn.edu.pku.ss.ct.consumer.dao.HBaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class CallLogConsumer implements Consumer {
    public void consume() {
        try {
            Properties prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("consumer.properties"));

            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);

            consumer.subscribe(Arrays.asList(Names.TOPIC.getValue()));

            HBaseDao dao = new HBaseDao();
            dao.init();

            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.value());
                    dao.insertData(consumerRecord.value());
//                    CallLog log = new CallLog(consumerRecord.value());
//                    dao.insertData(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {

    }
}
