package processor;


import beans.AnalysisOutput;
import beans.FlakyCSVOutput;
import beans.MethodAnalysisOutput;
import beans.StatisticalOutput;
import utils.CSVParser;
import utils.ReportManager;

import java.util.ArrayList;

public class AnalysisMain {
    public static void main(String[] args) throws Exception {
        ArrayList<FlakyCSVOutput> flakyCsv = new CSVParser().getFlakyMethods();
        ArrayList<AnalysisOutput> analysises = new ArrayList<>();
        ArrayList<String> revs = new CSVParser().getProjectFlakyRevisions();
        ArrayList<String> urls = new CSVParser().getProjectsToAnalyze();
        for (String projectRev : revs) {
            String project = projectRev.split("/")[0];
            String rev = projectRev.split("/")[1];
            String projectURL = "";
            for (String url : urls) {
                if (url.toLowerCase().contains(project.toLowerCase()))
                    projectURL = url;
            }
            System.out.println(projectRev);
            ArrayList<FlakyCSVOutput> flakyMethods = new ArrayList<>();
            for (FlakyCSVOutput flaky : flakyCsv) {
                if (flaky.getProject().equalsIgnoreCase(project))
                    flakyMethods.add(flaky);
            }
            AnalysisOutput output = AnalysisComputation.compute(projectURL, rev, "E:/FlakySmellStudy/" + projectRev, flakyMethods);
            analysises.add(output);

        }
        ReportManager.saveReport(analysises);
        ArrayList<MethodAnalysisOutput> allMethods = new ArrayList<>();
        for (AnalysisOutput analysis : analysises) {
            ArrayList<MethodAnalysisOutput> methods = analysis.getMethodsAnalysis();
            for (MethodAnalysisOutput method : methods) {
                boolean duplicate = false;
                if (method.isFlaky()) {
                    for (MethodAnalysisOutput m : allMethods) {
                        if (m.getBelongingClass().equalsIgnoreCase(method.getBelongingClass()) && m.getName().equalsIgnoreCase(method.getName())
                                && (m.isResourceOptimism() == method.isResourceOptimism()) && (m.isIndirectTesting() == method.isIndirectTesting()
                                && (m.isTestRunWar() == method.isTestRunWar()) && (m.isFireAndForget() == method.isFireAndForget()
                                && (m.isConditionalTestLogic() == method.isConditionalTestLogic())))) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (!duplicate)
                        allMethods.add(method);
                }
            }
        }
        StatisticalOutput output = new StatisticalAnalysis().getStats(allMethods);
        System.out.println(output.toString());

    }
}

