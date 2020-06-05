package utils;

import beans.AnalysisOutput;
import beans.MethodAnalysisOutput;
import it.unisa.testSmellDiffusion.utility.FileUtility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportManager {
    public static void saveReport(ArrayList<AnalysisOutput> analysises) {
        // LOGGER.info("Starting report");
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
        String outputDir = "reports";
        StringBuilder output = new StringBuilder("PROJECT,CLASS,METHOD,RO,IT,TRW,FAF,CTL,FLAKY\n");
        for (AnalysisOutput analysis : analysises) {
            for (MethodAnalysisOutput method : analysis.getMethodsAnalysis()) {
                output.append(analysis.getProjectName()).append(",").append(method.getBelongingClass()).append(",").append(method.getName()).append(",").append(method.isResourceOptimism()).append(",").append(method.isIndirectTesting()).append(",").append(method.isTestRunWar()).append(",").append(method.isFireAndForget()).append(",").append(method.isConditionalTestLogic()).append(",").append(method.isFlaky()).append("\n");

            }
        }
        File out = new File(outputDir);
        if (!out.exists() && !out.mkdirs())
            return;
        FileUtility.writeFile(output.toString(), outputDir + "/" + fileName);

    }
}
