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

        for(String project : new CSVParser().getProjectsToAnalyze()){
            String projectName = project.split("/")[4].replace(".git", "");
            ArrayList<FlakyCSVOutput> flakyMethods = new ArrayList<>();
            for(FlakyCSVOutput flaky : flakyCsv){
                if(flaky.getProject().equalsIgnoreCase(projectName))
                    flakyMethods.add(flaky);
            }
            AnalysisOutput output = AnalysisComputation.compute(project, System.getProperty("user.home") + "/FlakySmellStudy/" + projectName, flakyMethods);
            analysises.add(output);
            ReportManager.saveReport(analysises);
        }
        ArrayList<MethodAnalysisOutput> allMethods = new ArrayList<>();
        for(AnalysisOutput analysis : analysises){
            ArrayList<MethodAnalysisOutput> methods = analysis.getMethodsAnalysis();
            allMethods.addAll(methods);
        }
        StatisticalOutput output = new StatisticalAnalysis().getStats(allMethods);
        System.out.println(output.toString());

    }
}

