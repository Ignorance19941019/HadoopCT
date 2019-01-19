package cn.edu.pku.ss.ct.consumer.dao;

import cn.edu.pku.ss.ct.common.bean.BaseDao;
import cn.edu.pku.ss.ct.common.constant.Names;
import cn.edu.pku.ss.ct.common.constant.ValueConstant;
import cn.edu.pku.ss.ct.consumer.bean.CallLog;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseDao extends BaseDao {
    public void init() throws IOException {
        start();

        createNamespaceNX(Names.NAMESPACE.getValue());
        createTableXX(Names.TABLE.getValue(), "cn.edu.pku.ss.ct.consumer.coprocessor.InsertCalleeCoprocessor", ValueConstant.REGION_COUNT, Names.CF_CALLER.getValue(), Names.CF_CALLEE.getValue());

        end();
    }

    public void insertData(CallLog log) throws IOException, IllegalAccessException {
        log.setRowkey(genRegionNum(log.getCall1(), log.getCalltime()) + "_" + log.getCall1() + "_" + log.getCalltime() + "_" + log.getCall2() + "_" + log.getDuration());
        putData(log);
    }

    public void insertData(String value) throws IOException {

        String[] values = value.split("\t");
        String call1 = values[0];
        String call2 = values[1];
        String calltime = values[2];
        String duration = values[3];

        // 主叫用户
        String callerRowKey = genRegionNum(call1, calltime) + "_" + call1 + "_" + calltime + "_" + call2 + "_" + duration + "_1";
        Put callerPut = new Put(Bytes.toBytes(callerRowKey));
        byte[] callerFamily = Bytes.toBytes(Names.CF_CALLER.getValue());

        callerPut.addColumn(callerFamily, Bytes.toBytes("call1"), Bytes.toBytes(call1));
        callerPut.addColumn(callerFamily, Bytes.toBytes("call2"), Bytes.toBytes(call2));
        callerPut.addColumn(callerFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
        callerPut.addColumn(callerFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
        callerPut.addColumn(callerFamily, Bytes.toBytes("flag"), Bytes.toBytes("1"));

//        // 被叫用户, 被协处理器插入
//        String calleeRowKey = genRegionNum(call2, calltime) + "_" + call2 + "_" + calltime + "_" + call1 + "_" + duration + "_0";
//        Put calleePut = new Put(Bytes.toBytes(calleeRowKey));
//        byte[] calleeFamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
//        calleePut.addColumn(calleeFamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
//        calleePut.addColumn(calleeFamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
//        calleePut.addColumn(calleeFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
//        calleePut.addColumn(calleeFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
//        calleePut.addColumn(calleeFamily, Bytes.toBytes("flag"), Bytes.toBytes("0"));
//
//        List<Put> puts = new ArrayList<Put>();
//        puts.add(callerPut);
//        puts.add(calleePut);

        putData(Names.TABLE.getValue(), callerPut);
    }
}
