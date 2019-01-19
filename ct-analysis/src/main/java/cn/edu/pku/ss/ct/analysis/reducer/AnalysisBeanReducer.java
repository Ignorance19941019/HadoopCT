package cn.edu.pku.ss.ct.analysis.reducer;

import cn.edu.pku.ss.ct.analysis.kv.AnalysisKey;
import cn.edu.pku.ss.ct.analysis.kv.AnalysisValue;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AnalysisBeanReducer extends Reducer<AnalysisKey, Text, AnalysisKey, AnalysisValue> {
    @Override
    protected void reduce(AnalysisKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int sumCall = 0;
        int sumDuration = 0;

        for (Text value : values) {
            int duration = Integer.parseInt(value.toString());
            sumCall += 1;
            sumDuration += duration;
        }
        
        context.write(key, new AnalysisValue("" + sumCall, "" + sumDuration));
    }
}
