package beans;

import java.util.ArrayList;

public class AnalysisOutput {
    private String projectName;
    private String projectGitAddress;
    private ArrayList<MethodAnalysisOutput> methodsAnalysis;
    private int roMethods;
    private int fafMethods;
    private int trwMethods;
    private int ctlMethods;
    private int itMethods;
    private int smellyMethods;
    private int flakyMethods;
    private int flakySmellyMethods;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectGitAddress() {
        return projectGitAddress;
    }

    public void setProjectGitAddress(String projectGitAddress) {
        this.projectGitAddress = projectGitAddress;
    }

    public ArrayList<MethodAnalysisOutput> getMethodsAnalysis() {
        return methodsAnalysis;
    }

    public void setMethodsAnalysis(ArrayList<MethodAnalysisOutput> methodsAnalysis) {
        this.methodsAnalysis = methodsAnalysis;
    }

    public int getRoMethods() {
        return roMethods;
    }

    public void setRoMethods(int roMethods) {
        this.roMethods = roMethods;
    }

    public int getFafMethods() {
        return fafMethods;
    }

    public void setFafMethods(int fafMethods) {
        this.fafMethods = fafMethods;
    }

    public int getTrwMethods() {
        return trwMethods;
    }

    public void setTrwMethods(int trwMethods) {
        this.trwMethods = trwMethods;
    }

    public int getCtlMethods() {
        return ctlMethods;
    }

    public void setCtlMethods(int ctlMethods) {
        this.ctlMethods = ctlMethods;
    }

    public int getItMethods() {
        return itMethods;
    }

    public void setItMethods(int itMethods) {
        this.itMethods = itMethods;
    }

    public int getSmellyMethods() {
        return roMethods + trwMethods + fafMethods + ctlMethods + itMethods;
    }

    public void setSmellyMethods(int smellyMethods) {
        this.smellyMethods = smellyMethods;
    }

    public int getFlakyMethods() {
        return flakyMethods;
    }

    public void setFlakyMethods(int flakyMethods) {
        this.flakyMethods = flakyMethods;
    }

    public int getFlakySmellyMethods() {
        return flakySmellyMethods;
    }

    public void setFlakySmellyMethods(int flakySmellyMethods) {
        this.flakySmellyMethods = flakySmellyMethods;
    }
}
