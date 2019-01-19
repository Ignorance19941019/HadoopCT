package cn.edu.pku.ss.ct.common.bean;


import cn.edu.pku.ss.ct.common.api.Column;
import cn.edu.pku.ss.ct.common.api.Rowkey;
import cn.edu.pku.ss.ct.common.api.TableRef;
import cn.edu.pku.ss.ct.common.constant.Names;
import cn.edu.pku.ss.ct.common.constant.ValueConstant;
import cn.edu.pku.ss.ct.common.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public abstract class BaseDao {

    private ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();
    private ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    protected void start() throws IOException {
        getConnection();
        getAdmin();
    }

    protected void end() throws IOException {
        Admin admin = getAdmin();
        Connection connection = getConnection();

        if (admin != null) {
            admin.close();
            adminHolder.remove();
        }

        if (connection != null) {
            connection.close();
            connectionHolder.remove();
        }
    }

    protected void putData(Object obj) throws IOException, IllegalAccessException {

        Class clazz = obj.getClass();
        TableRef tableRef = (TableRef) clazz.getAnnotation(TableRef.class);
        String tableName = tableRef.value();

        Field[] fs = clazz.getDeclaredFields();
        String rowkey = "";
        for (Field f : fs) {
            Rowkey currentRowKey = f.getAnnotation(Rowkey.class);
            if (currentRowKey != null) {
                f.setAccessible(true);
                rowkey = (String) f.get(obj);
                break;
            }
        }


        Connection connection = getConnection();
        Table table = connection.getTable(TableName.valueOf(tableName));

        Put put = new Put(Bytes.toBytes(rowkey));

        for (Field f : fs) {
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                String family = column.family();
                String colName = column.column();
                if (colName == null || colName.equals("")) {
                    colName = f.getName();
                }
                f.setAccessible(true);
                String value = (String) f.get(obj);

                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(colName), Bytes.toBytes(value));
            }
        }

        table.put(put);

        table.close();
    }

    protected void putData(String tbName, List<Put> puts) throws IOException {
        Connection connection = getConnection();
        Table table = connection.getTable(TableName.valueOf(tbName));

        table.put(puts);

        table.close();
    }

    protected void putData(String tbName, Put put) throws IOException {
        Connection connection = getConnection();
        Table table = connection.getTable(TableName.valueOf(tbName));

        table.put(put);

        table.close();
    }

    protected void createNamespaceNX(String namespace) throws IOException {
        Admin admin = getAdmin();
        try {
            admin.getNamespaceDescriptor(namespace);
        } catch (NamespaceNotFoundException e) {
            NamespaceDescriptor namespaceDescriptor =
                    NamespaceDescriptor.create(namespace).build();

            admin.createNamespace(namespaceDescriptor);
        }
    }

    protected void createTableXX(String tbName, String... families) throws IOException {

        createTableXX(tbName, null, null, families);
    }

    protected void createTableXX(String tbName, String coprocessorClass, Integer reginCount, String... families) throws IOException {
        Admin admin = getAdmin();

        TableName tableName = TableName.valueOf(tbName);

        if (admin.tableExists(tableName)) {
            deleteTable(tbName);
        }

        createTable(tbName, coprocessorClass, reginCount, families);
    }

    protected void deleteTable(String tbName) throws IOException {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(tbName);
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }

    private void createTable(String tbName, String coprocessorClass, Integer regionCount, String... families) throws IOException {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(tbName);

        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

        if (families == null || families.length == 0) {
            families = new String[1];
            families[0] = Names.CF_INFO.getValue();
        }

        for (String family : families) {
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(family);
            tableDescriptor.addFamily(columnDescriptor);
        }

        if (coprocessorClass != null && !coprocessorClass.equals("")) {
            tableDescriptor.addCoprocessor(coprocessorClass);
        }

        if (regionCount == null || regionCount <= 1) {
            admin.createTable(tableDescriptor);
        } else {
            byte[][] splitKeys = genSplitKeys(regionCount);
            admin.createTable(tableDescriptor, splitKeys);
        }

    }

    private byte[][] genSplitKeys(int regionCount) {

        int splitKeysCount = regionCount - 1;
        byte[][] bs = new byte[splitKeysCount][];

        List<byte[]> bsList = new ArrayList<byte[]>();
        for (int i = 0; i < splitKeysCount; ++i) {
            String splitKey = i + "|";
            bsList.add(Bytes.toBytes(splitKey));
        }

        Collections.sort(bsList, new Bytes.ByteArrayComparator());

        bsList.toArray(bs);

        return bs;
    }

    protected List<String[]> getStartStopRowkeys(String tel, String start, String end) throws ParseException {
        List<String[]> rowkeyslist = new ArrayList<String[]>();

        String startTime = start.substring(0, 6);
        String endTime = start.substring(0, 6);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(DateUtil.parse(startTime, "yyyyMM"));
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(DateUtil.parse(endTime, "yyyyMM"));


        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            String nowTime = DateUtil.format(startCal.getTime(), "yyyyMM");
            int regionNum = genRegionNum(tel, nowTime);

            String startRow = regionNum + "_" + tel + "_" + nowTime;
            String stopRow = startRow + "|";

            String[] rowkeys = {startRow, stopRow};
            rowkeyslist.add(rowkeys);

            startCal.add(Calendar.MONTH, 1);
        }

        return rowkeyslist;
    }

    protected int genRegionNum(String tel, String date) {
        String usercode = tel.substring(tel.length() - 4);
        String yearMonth = date.substring(0, 6);

        int crc = Math.abs(usercode.hashCode() ^ yearMonth.hashCode());

        return crc % ValueConstant.REGION_COUNT;
    }


    protected synchronized Connection getConnection() throws IOException {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            Configuration conf = HBaseConfiguration.create();
            connection = ConnectionFactory.createConnection(conf);
            connectionHolder.set(connection);
        }
        return connection;
    }

    protected synchronized Admin getAdmin() throws IOException {
        Admin admin = adminHolder.get();
        if (admin == null) {
            admin = getConnection().getAdmin();
            adminHolder.set(admin);
        }
        return admin;
    }
}
