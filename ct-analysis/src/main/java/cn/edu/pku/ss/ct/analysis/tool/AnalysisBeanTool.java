package cn.edu.pku.ss.ct.analysis.tool;

import cn.edu.pku.ss.ct.analysis.io.MySQLBeanOutputFormat;
import cn.edu.pku.ss.ct.analysis.kv.AnalysisKey;
import cn.edu.pku.ss.ct.analysis.kv.AnalysisValue;
import cn.edu.pku.ss.ct.analysis.mapper.AnalysisBeanMapper;
import cn.edu.pku.ss.ct.analysis.reducer.AnalysisBeanReducer;
import cn.edu.pku.ss.ct.common.constant.Names;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.util.Tool;

public class AnalysisBeanTool implements Tool {
    public int run(String[] args) throws Exception {

        Job job = Job.getInstance();
        job.setJarByClass(AnalysisBeanTool.class);

        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(Names.CF_CALLER.getValue()));

        //mapper
        TableMapReduceUtil.initTableMapperJob(
                Names.TABLE.getValue(),
                scan,
                AnalysisBeanMapper.class,
                AnalysisKey.class,
                Text.class,
                job
        );
        job.setReducerClass(AnalysisBeanReducer.class);

        job.setOutputKeyClass(AnalysisKey.class);
        job.setOutputValueClass(AnalysisValue.class);

        job.setOutputFormatClass(MySQLBeanOutputFormat.class);

        return job.waitForCompletion(true) ?
                JobStatus.State.SUCCEEDED.getValue() : JobStatus.State.FAILED.getValue();
    }

    public void setConf(Configuration configuration) {

    }

    public Configuration getConf() {
        return null;
    }
}
