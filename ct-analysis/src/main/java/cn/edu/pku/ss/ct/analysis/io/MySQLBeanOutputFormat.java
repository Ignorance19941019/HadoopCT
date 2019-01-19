package cn.edu.pku.ss.ct.analysis.io;

import cn.edu.pku.ss.ct.analysis.kv.AnalysisKey;
import cn.edu.pku.ss.ct.analysis.kv.AnalysisValue;
import cn.edu.pku.ss.ct.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLBeanOutputFormat extends OutputFormat<AnalysisKey, AnalysisValue> {

    protected static class MySQLRecordWriter extends RecordWriter<AnalysisKey, AnalysisValue> {

        private Connection connection = null;

        private Jedis jedis = null;

        public MySQLRecordWriter() {
            connection = JDBCUtil.getConnection();
            jedis = new Jedis("hadoop02", 6379);
        }

        @Override
        public void write(AnalysisKey key, AnalysisValue value) throws IOException, InterruptedException {

            PreparedStatement pstat = null;
            try {
                String insertSQL = "insert into ct_call (telid, dateid, sumcall, sumduration) values (?, ?, ?, ?)";
                pstat = connection.prepareStatement(insertSQL);

                pstat.setInt(1, Integer.parseInt(jedis.hget("ct_user", key.getTel())));
                pstat.setInt(2, Integer.parseInt(jedis.hget("ct_date", key.getDate())));
                pstat.setInt(3, Integer.parseInt(value.getSumCall()));
                pstat.setInt(4, Integer.parseInt(value.getSumDuration()));
                pstat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (pstat != null) {
                    try {
                        pstat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public MySQLBeanOutputFormat() {
        super();
    }

    @Override
    public RecordWriter<AnalysisKey, AnalysisValue> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }


    private FileOutputCommitter committer = null;

    public static Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get("mapreduce.output.fileoutputformat.outputdir");
        return name == null ? null : new Path(name);
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        if (this.committer == null) {
            Path output = getOutputPath(taskAttemptContext);
            this.committer = new FileOutputCommitter(output, taskAttemptContext);
        }

        return this.committer;
    }
}
