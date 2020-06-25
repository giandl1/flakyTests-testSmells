package processor;

import beans.MethodAnalysisOutput;
import beans.StatisticalOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class LastFullReportAnalyzer {
    public static void main(String[] args) throws Exception {
        File f = new File("reports/202006221502.csv");
        BufferedReader br = new BufferedReader(new FileReader(f));
        br.readLine();
        String line;
        ArrayList<MethodAnalysisOutput> methods = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            MethodAnalysisOutput method = new MethodAnalysisOutput();
            String words[] = line.split(",");
            method.setBelongingProjectRev(words[0]);
            method.setBelongingClass(words[1]);
            method.setName(words[2]);
            method.setResourceOptimism(Boolean.parseBoolean(words[3]));
            method.setIndirectTesting(Boolean.parseBoolean(words[4]));
            method.setTestRunWar(Boolean.parseBoolean(words[5]));
            method.setFireAndForget(Boolean.parseBoolean(words[6]));
            method.setConditionalTestLogic(Boolean.parseBoolean(words[7]));
            method.setFlaky(Boolean.parseBoolean(words[8]));
            if (method.isFlaky()) {
                boolean duplicated = false;
                for (MethodAnalysisOutput m : methods) {
                    if (m.getBelongingClass().equalsIgnoreCase(method.getBelongingClass()) && m.getName().equalsIgnoreCase(method.getName())
                            && (m.isResourceOptimism() == method.isResourceOptimism()) && (m.isIndirectTesting() == method.isIndirectTesting()
                            && (m.isTestRunWar() == method.isTestRunWar()) && (m.isFireAndForget() == method.isFireAndForget()
                            && (m.isConditionalTestLogic() == method.isConditionalTestLogic())))) {
                        duplicated = true;
                        break;
                    }
                }
                if (!duplicated)
                    methods.add(method);
            }
        }

        StatisticalOutput output = new StatisticalAnalysis().getStats(methods);
        System.out.println(output);
        System.out.println(methods.size());

    }
}
