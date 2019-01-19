package cn.edu.pku.ss.ct.producer.bean;



import cn.edu.pku.ss.ct.common.bean.DataIn;
import cn.edu.pku.ss.ct.common.bean.DataOut;
import cn.edu.pku.ss.ct.common.bean.Producer;
import cn.edu.pku.ss.ct.common.util.DateUtil;
import cn.edu.pku.ss.ct.common.util.NumberUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LocalFileProducer implements Producer {

    private          DataIn  in;
    private          DataOut out;
    private volatile boolean flag = true;

    public void produce() {
        try {
            List<Contact> contacts = in.read(Contact.class);

            while (flag) {

                // 随机产生通话双方的号码
                int call1Index = new Random().nextInt(contacts.size());
                int call2Index;
                while (true) {
                    call2Index = new Random().nextInt(contacts.size());
                    if (call1Index != call2Index)
                        break;
                }

                Contact call1 = contacts.get(call1Index);
                Contact call2 = contacts.get(call2Index);

                // 随机生成通话开始时间
                String startDate = "20180101000000";
                String endDate = "20190101000000";

                long startTime = DateUtil.parse(startDate, "yyyyMMddHHmmss").getTime();
                long endTime = DateUtil.parse(endDate, "yyyyMMddHHmmss").getTime();

                long callTime = startTime + (long)((endTime - startTime) * Math.random());

                String callTimeString = DateUtil.format(new Date(callTime), "yyyyMMddHHmmss");

                // 随机生成通话时长
                String duration = NumberUtil.zeroPrefixFormat(new Random().nextInt(3000), 4);

                // 生成通话记录
                CallLog log = new CallLog(call1.getTel(), call2.getTel(), callTimeString, duration);

                System.out.println(log);

                // 关闭文件
                out.write(log);
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setIn(DataIn in) {
        this.in = in;
    }

    public void setOut(DataOut out) {
        this.out = out;
    }

    public void close() throws IOException {
        if (in != null)
            in.close();
        if (out != null)
            out.close();
    }
}
