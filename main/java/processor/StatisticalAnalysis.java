package processor;

import beans.MethodAnalysisOutput;
import beans.StatisticalOutput;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class StatisticalAnalysis {
    public StatisticalOutput getStats(ArrayList<MethodAnalysisOutput> methods){
        double roFlaky = 0;
        double itFlaky = 0;
        double ctlFlaky = 0;
        double fafFlaky = 0;
        double trwFlaky = 0;

        for(MethodAnalysisOutput method : methods){
            if(method.isFlaky() && method.isConditionalTestLogic())
                ctlFlaky++;
            if(method.isFlaky() && method.isFireAndForget())
                fafFlaky++;
            if(method.isFlaky() && method.isTestRunWar())
                trwFlaky++;
            if(method.isFlaky() && method.isIndirectTesting())
                itFlaky++;
            if(method.isFlaky() && method.isResourceOptimism())
                roFlaky++;
        }
        double methodsNumber = methods.size();
        StatisticalOutput output = new StatisticalOutput();
        output.setCtlFlakyRate(round(ctlFlaky/methodsNumber, 5)); //nr. flaky tests totale
        output.setFafFlakyRate(round(fafFlaky/methodsNumber, 5));
        output.setItFlakyRate(round(itFlaky/methodsNumber, 5));
        output.setRoFlakyRate(round(roFlaky/methodsNumber, 5));
        output.setTrwFlakyRate(round(trwFlaky/methodsNumber, 5));
        return output;
    }

    public BigDecimal round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }
}
