package cn.edu.pku.ss.ct.analysis;

import cn.edu.pku.ss.ct.analysis.tool.AnalysisBeanTool;
import cn.edu.pku.ss.ct.analysis.tool.AnalysisTextTool;
import org.apache.hadoop.util.ToolRunner;

public class AnalysisData {

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new AnalysisBeanTool(), args);
    }
}
