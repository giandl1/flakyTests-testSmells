package utils;

import beans.FlakyCSVOutput;
import beans.MethodAnalysisOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FlakyCSVParser {

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
            return methods;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
