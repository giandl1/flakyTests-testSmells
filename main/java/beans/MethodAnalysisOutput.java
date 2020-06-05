package beans;

import it.unisa.testSmellDiffusion.beans.MethodBean;

import java.util.ArrayList;

public class MethodAnalysisOutput {
    private String name;
    private String belongingClass;
    private boolean isResourceOptimism;
    private boolean isIndirectTesting;
    private boolean isTestRunWar;
    private boolean isFireAndForget;
    private boolean isConditionalTestLogic;
    private boolean isFlaky;


    public boolean isResourceOptimism() {
        return isResourceOptimism;
    }



    public boolean isIndirectTesting() {
        return isIndirectTesting;
    }



    public boolean isTestRunWar() {
        return isTestRunWar;
    }



    public boolean isFireAndForget() {
        return isFireAndForget;
    }



    public boolean isConditionalTestLogic() {
        return isConditionalTestLogic;
    }



    public boolean isFlaky() {
        return isFlaky;
    }

    public void setFlaky(boolean flaky) {
        isFlaky = flaky;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBelongingClass() {
        return belongingClass;
    }

    public void setBelongingClass(String belongingClass) {
        this.belongingClass = belongingClass;
    }

    public boolean isSmelly() {
        return isResourceOptimism || isConditionalTestLogic || isFireAndForget || isIndirectTesting || isTestRunWar;
    }


    public void flagAsSmelly(ArrayList<MethodBean> smellyMethods, String smellId) {
        for (MethodBean method : smellyMethods) {
            String className = method.getBelongingClass().getBelongingPackage() + "." + method.getBelongingClass().getName();
            if (className.equalsIgnoreCase(this.belongingClass) && method.getName().equalsIgnoreCase(this.name)) {
                if (smellId.equalsIgnoreCase("ro"))
                    this.isResourceOptimism = true;
                else if (smellId.equalsIgnoreCase("faf"))
                    this.isFireAndForget = true;
                else if (smellId.equalsIgnoreCase("trw"))
                    this.isTestRunWar = true;
                else if (smellId.equalsIgnoreCase("ctl"))
                    this.isConditionalTestLogic = true;
                else if (smellId.equalsIgnoreCase("it"))
                    this.isIndirectTesting = true;
            }
        }
    }

    public boolean CoOccurrenceFlakySmell(){
        return isSmelly() && this.isFlaky;
    }

}
