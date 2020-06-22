package processor;

import beans.AnalysisOutput;
import beans.FlakyCSVOutput;
import beans.MethodAnalysisOutput;
import beans.ProjectBean;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.testMutation.TestMutationUtilities;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import testSmells.DetectionHelper;
import testSmells.StructuralDetector;
import utils.GitUtility;
import utils.MultiModuleParser;

import java.util.ArrayList;
import java.util.Vector;

public class AnalysisComputation {
    public static AnalysisOutput compute(String gitURL, String rev, String workingDir, ArrayList<FlakyCSVOutput> flakyMethods) throws CoreException {
        try {
            GitUtility.getProjectFromGit(gitURL, rev, workingDir);
        } catch (Exception e) {
            System.out.println("PROJECT ALREADY FOUND IN FILESYSTEM");
        }
        ProjectBean project = MultiModuleParser.parseProject(workingDir);
        Vector<PackageBean> packages = project.getPackages();
        Vector<PackageBean> testPackages = project.getTestPackages();
        TestMutationUtilities utils = new TestMutationUtilities();
        ArrayList<ClassBean> classes = utils.getClasses(packages);
        StructuralDetector detector = new StructuralDetector();
        AnalysisOutput analysis = new AnalysisOutput();
        String projectName = gitURL.split("/")[4].replace(".git", "");

        analysis.setProjectName(projectName);
        ArrayList<MethodAnalysisOutput> allMethods = new ArrayList<>();
        for (ClassBean testClass : utils.getClasses(testPackages)) {
            if (testClass != null && testClass.getTextContent().contains("@Test")) {
                ArrayList<MethodBean> ctlMethods = detector.getConditionalTestLogicMethods(testClass);
                ArrayList<MethodBean> fafMethods = detector.getFireAndForgetMethods(testClass);
                ArrayList<MethodBean> roMethods = detector.getResourceOptimismMethods(testClass);
                ArrayList<MethodBean> trwMethods = detector.getTestRunWarMethods(testClass);
                ArrayList<MethodBean> itMethods = null;
                ClassBean prodClass = TestMutationUtilities.getProductionClassBy(testClass.getName(), packages);
                if (prodClass != null)
                    itMethods = detector.getIndirectTestingMethods(testClass, prodClass, DetectionHelper.findInvocations(packages));
                analysis.setCtlMethods(analysis.getCtlMethods() + ctlMethods.size());
                analysis.setFafMethods(analysis.getFafMethods() + fafMethods.size());
                analysis.setTrwMethods(analysis.getTrwMethods() + trwMethods.size());
                if (itMethods != null)
                    analysis.setItMethods(analysis.getItMethods() + itMethods.size());
                analysis.setRoMethods(analysis.getRoMethods() + roMethods.size());
                for (MethodBean method : testClass.getMethods()) {
                    MethodAnalysisOutput methodAnalysis = new MethodAnalysisOutput();
                    methodAnalysis.setBelongingClass(method.getBelongingClass().getBelongingPackage() + "." + method.getBelongingClass().getName());
                    methodAnalysis.setName(method.getName());
                    methodAnalysis.setBelongingProjectRev(projectName + "/" + rev);
                    methodAnalysis.flagAsSmelly(roMethods, "ro");
                    methodAnalysis.flagAsSmelly(fafMethods, "faf");
                    methodAnalysis.flagAsSmelly(trwMethods, "trw");
                    if (itMethods != null)
                        methodAnalysis.flagAsSmelly(itMethods, "it");
                    methodAnalysis.flagAsSmelly(ctlMethods, "ctl");
                    for (FlakyCSVOutput flaky : flakyMethods) {
                        if (flaky.getTestClass().equalsIgnoreCase(testClass.getBelongingPackage() + "." + testClass.getName())
                                && method.getName().equalsIgnoreCase(flaky.getTestName())) {
                            methodAnalysis.setFlaky(true);
                            analysis.setFlakyMethods(analysis.getFlakyMethods() + 1);
                        }
                    }
                    if (methodAnalysis.isCoOccurrentFlakySmell())
                        analysis.setFlakySmellyMethods(analysis.getFlakySmellyMethods() + 1);
                    allMethods.add(methodAnalysis);
                }

            }
        }
        analysis.setMethodsAnalysis(allMethods);
        System.out.println("PROJECT: " + analysis.getProjectName());
        System.out.println("SMELLY METHODS: " + analysis.getSmellyMethods());
        System.out.println("Resource Optimism Methods: " + analysis.getRoMethods());
        System.out.println("Indirect Testing Methods: " + analysis.getItMethods());
        System.out.println("Test Run War Methods: " + analysis.getTrwMethods());
        System.out.println("Fire and Forget Methods: " + analysis.getFafMethods());
        System.out.println("Conditional Test Logic Methods: " + analysis.getCtlMethods());
        System.out.println("Flaky Tests: " + analysis.getFlakyMethods());
        System.out.println("Flaky and smelly tests: " + analysis.getFlakySmellyMethods());
        return analysis;

    }
}

