package cn.edu.pku.ss.ct.consumer.coprocessor;


import cn.edu.pku.ss.ct.common.bean.BaseDao;
import cn.edu.pku.ss.ct.common.constant.Names;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

// 使用协处理器处理callee的插入，提高性能降低耦合
public class InsertCalleeCoprocessor extends BaseRegionObserver {

    private class CoprocessorDao extends BaseDao {
        public int getRegionNum(String tel, String calltime) {
            return genRegionNum(tel, calltime);
        }
    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        Table table = e.getEnvironment().getTable(TableName.valueOf(Names.TABLE.getValue()));

        CoprocessorDao dao = new CoprocessorDao();
        String rowkey = Bytes.toString(put.getRow());
        String[] values = rowkey.split("_");
        String call1 = values[1];
        String call2 = values[3];
        String calltime = values[2];
        String duration = values[4];
        String flag = values[5];

        if (flag.equals("1")) {
            String calleeRowKey = dao.getRegionNum(call2, calltime) + "_" + call2 + "_" + calltime + "_" + call1 + "_" + duration + "_0";

            Put calleePut = new Put(Bytes.toBytes(calleeRowKey));
            byte[] calleeFamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
            calleePut.addColumn(calleeFamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("flag"), Bytes.toBytes("0"));

            table.put(calleePut);

            table.close();
        }
    }
}
