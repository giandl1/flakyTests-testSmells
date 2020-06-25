package processor;

import beans.MethodAnalysisOutput;
import beans.StatisticalOutput;
import utils.ReportManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class StatisticalAnalysis {
    public StatisticalOutput getStats(ArrayList<MethodAnalysisOutput> methods) {
        double roFlaky = 0;
        double itFlaky = 0;
        double ctlFlaky = 0;
        double fafFlaky = 0;
        double trwFlaky = 0;
        double smellyFlaky = 0;
        ArrayList<MethodAnalysisOutput> flakyAndSmelly = new ArrayList<>();
        for (MethodAnalysisOutput method : methods)
            if (method.isSmelly())
                flakyAndSmelly.add(method);
        System.out.println(flakyAndSmelly.size());

        for (MethodAnalysisOutput method : flakyAndSmelly) {
            if(method.isSmelly() && method.isFlaky())
                smellyFlaky++;
            if (method.isFlaky() && method.isConditionalTestLogic())
                ctlFlaky++;
            if (method.isFlaky() && method.isFireAndForget())
                fafFlaky++;
            if (method.isFlaky() && method.isTestRunWar())
                trwFlaky++;
            if (method.isFlaky() && method.isIndirectTesting())
                itFlaky++;
            if (method.isFlaky() && method.isResourceOptimism())
                roFlaky++;
        }
        System.out.println(smellyFlaky);
        double methodsNumber = methods.size();
        System.out.println(methodsNumber);
        StatisticalOutput output = new StatisticalOutput();
        output.setCtlFlakyRate(round(ctlFlaky / methodsNumber, 5).multiply(new BigDecimal(100))); //nr. flaky tests totale
        output.setFafFlakyRate(round(fafFlaky / methodsNumber, 5).multiply(new BigDecimal(100)));
        output.setItFlakyRate(round(itFlaky / methodsNumber, 5).multiply(new BigDecimal(100)));
        output.setRoFlakyRate(round(roFlaky / methodsNumber, 5).multiply(new BigDecimal(100)));
        output.setTrwFlakyRate(round(trwFlaky / methodsNumber, 5).multiply(new BigDecimal(100)));
        output.setSmellyFlaky(round(smellyFlaky/methodsNumber,5).multiply(new BigDecimal(100)));
        ReportManager.smallReport(flakyAndSmelly);
        return output;
    }

    public BigDecimal round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }
}
