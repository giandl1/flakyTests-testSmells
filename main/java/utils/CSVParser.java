package utils;

import beans.FlakyCSVOutput;
import beans.MethodAnalysisOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class CSVParser {

    public ArrayList<FlakyCSVOutput> getFlakyMethods() {
        String csvFile = "resources/historical_rerun_flaky_tests.csv";
        BufferedReader br;
        String line;
        String csvSplitBy = ",";
        ArrayList<FlakyCSVOutput> methods = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] strings = line.split(csvSplitBy);
                FlakyCSVOutput output = new FlakyCSVOutput();
                output.setProject(strings[0]);
                output.setTestClass(strings[2]);
                output.setTestName(strings[3]);
                methods.add(output);
            }
            File file = new File("resources/flakyMethods");
            File[] dirs = file.listFiles();
            if (dirs != null) {
                for (File f : dirs) {
                    String[] project = f.getName().split("-",2);
                    String projectName = "";
                    for (String str : project)
                        projectName += str;
                    projectName = projectName.replaceFirst(project[0], "");
                    File sub = new File(f.getAbsolutePath() + "/flakyMethods");
                    for (File test : sub.listFiles()) {
                        FlakyCSVOutput output = new FlakyCSVOutput();
                        output.setProject(projectName);
                        output.setTestClass(test.getName().split("-")[0]);
                        output.setTestName(test.getName().split("-")[1].replace(".java", ""));
                        methods.add(output);
                    }
                }
            }
            return methods;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getProjectsToAnalyze() {
        String csvFile = "resources/historical_projects.csv";
        ArrayList<String> projects = new ArrayList<>();
        BufferedReader br;
        String line;
        String csvSplitBy = ",";
        ArrayList<FlakyCSVOutput> methods = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] strings = line.split(csvSplitBy);
                projects.add("https://github.com/" + strings[1].replace("git@github.com:", "").replace(".git", ""));
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getProjectFlakyRevisions() {
        String csvFile = "resources/historical_rerun_flaky_tests.csv";
        ArrayList<String> revs = new ArrayList<>();
        BufferedReader br;
        String line;
        String csvSplitBy = ",";
        ArrayList<FlakyCSVOutput> methods = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] strings = line.split(csvSplitBy);
                String projectRev = strings[0] + "/" + strings[1];
                if (!revs.contains(projectRev))
                    revs.add(projectRev);
            }
            return revs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getProjects2ndSource() {
        String csvFile = "resources/projects2.csv";
        ArrayList<String> revs = new ArrayList<>();
        BufferedReader br;
        String line;
        ArrayList<FlakyCSVOutput> methods = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                revs.add(line);
            }
            return revs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

