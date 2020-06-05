package processor;

import beans.AnalysisOutput;
import beans.MethodAnalysisOutput;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.testMutation.TestMutationUtilities;
import it.unisa.testSmellDiffusion.utility.FolderToJavaProjectConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import testSmells.DetectionHelper;
import testSmells.StructuralDetector;
import utils.GitUtility;
import utils.ReportManager;

import java.util.ArrayList;
import java.util.Vector;

public class AnalysisProcessor {
    public static void compute(String gitURL, String workingDir) throws CoreException {
        try {
            GitUtility.getProjectFromGit(gitURL, workingDir, new TextProgressMonitor());
        } catch (Exception e) {
            System.out.println("PROJECT ALREADY FOUND IN FILESYSTEM");
        }
        TestMutationUtilities utils = new TestMutationUtilities();
        Vector<PackageBean> packages = FolderToJavaProjectConverter.convert(workingDir + "/src/main");
        Vector<PackageBean> testPackages = FolderToJavaProjectConverter.convert(workingDir + "/src/test");
        ArrayList<ClassBean> classes = utils.getClasses(packages);
        StructuralDetector detector = new StructuralDetector();
        AnalysisOutput analysis = new AnalysisOutput();
        analysis.setProjectName(gitURL.split("/")[4].replace(".git", ""));
        ArrayList<MethodAnalysisOutput> allMethods = new ArrayList<>();
        for (ClassBean prodClass : classes) {
            ClassBean testClass = TestMutationUtilities.getTestClassBy(prodClass.getName(), testPackages);
            if (testClass != null) {
                ArrayList<MethodBean> ctlMethods = detector.getConditionalTestLogicMethods(testClass);
                ArrayList<MethodBean> fafMethods = detector.getFireAndForgetMethods(testClass);
                for(MethodBean faf : fafMethods) System.out.println(faf.getName());
                ArrayList<MethodBean> roMethods = detector.getResourceOptimismMethods(testClass);
                ArrayList<MethodBean> trwMethods = detector.getTestRunWarMethods(testClass);
                ArrayList<MethodBean> itMethods = detector.getIndirectTestingMethods(testClass, prodClass, DetectionHelper.findInvocations(packages));
                analysis.setCtlMethods(analysis.getCtlMethods() + ctlMethods.size());
                analysis.setFafMethods(analysis.getFafMethods() + fafMethods.size());
                analysis.setTrwMethods(analysis.getTrwMethods() + trwMethods.size());
                analysis.setItMethods(analysis.getItMethods() + itMethods.size());
                analysis.setRoMethods(analysis.getRoMethods() + roMethods.size());
                for (MethodBean method : testClass.getMethods()) {
                    MethodAnalysisOutput methodAnalysis = new MethodAnalysisOutput();
                    methodAnalysis.setBelongingClass(method.getBelongingClass().getBelongingPackage() + "." + method.getBelongingClass().getName());
                    methodAnalysis.setName(method.getName());
                    methodAnalysis.flagAsSmelly(roMethods, "ro");
                    methodAnalysis.flagAsSmelly(fafMethods, "faf");
                    methodAnalysis.flagAsSmelly(trwMethods, "trw");
                    methodAnalysis.flagAsSmelly(itMethods, "it");
                    methodAnalysis.flagAsSmelly(ctlMethods, "ctl");
                    allMethods.add(methodAnalysis);
                }

            }
        }
        analysis.setMethodsAnalysis(allMethods);
        System.out.println(allMethods.size());
        ArrayList<AnalysisOutput> analysises = new ArrayList<>();
        analysises.add(analysis);
        ReportManager.saveReport(analysises);
        System.out.println("PROJECT: " + analysis.getProjectName());
        System.out.println("SMELLY METHODS: " + analysis.getSmellyMethods());
        System.out.println("Resource Optimism Methods: " + analysis.getRoMethods());
        System.out.println("Indirect Testing Methods: " + analysis.getItMethods());
        System.out.println("Test Run War Methods: " + analysis.getTrwMethods());
        System.out.println("Fire and Forget Methods: " + analysis.getFafMethods());
        System.out.println("Conditional Test Logic Methods: " + analysis.getCtlMethods());
        System.out.println("Flaky Tests: " + analysis.getFlakyMethods());

    }
}

