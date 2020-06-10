package testSmells;

import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;

import java.util.ArrayList;
import java.util.Collection;


public class StructuralDetector {
    public ArrayList<MethodBean> getResourceOptimismMethods(ClassBean pTestSuite) {
        ArrayList<MethodBean> roMethods = new ArrayList<>();
        Collection<MethodBean> testMethods = pTestSuite.getMethods();
        for (MethodBean testMethod : testMethods) {
            String textContent = testMethod.getTextContent();
            String[] lines = textContent.split("\n");
            for (String line : lines) {
                String fileObjectName = "";
                if (line.contains(" File ") && line.contains("\"")) {
                    String[] splitLine = line.split("\\s+");
                    for (int j = 0; j < splitLine.length; j++) {
                        if (splitLine[j].equals("File")) {
                            fileObjectName = splitLine[j + 1];
                            if (fileObjectName.contains("=")) {       //DELETE "=" IN CASES SUCH AS File x=("...");
                                fileObjectName = fileObjectName.split("=")[0];
                            }
                        }
                    }
                    if (!textContent.contains(fileObjectName + ".exists()") && (!textContent.contains(fileObjectName + ".canRead()")) &&
                            !textContent.contains(fileObjectName + ".canWrite()") && !textContent.contains(fileObjectName + ".canExecute()")) {
                        roMethods.add(testMethod);
                        break;
                    }
                }
            }
        }
        return roMethods;
    }


    public ArrayList<MethodBean> getIndirectTestingMethods(ClassBean pTestSuite, ClassBean pProductionClass, Collection<MethodBean> pMethods) {
        ArrayList<MethodBean> itMethods = new ArrayList<>();
        for (MethodBean testMethod : pTestSuite.getMethods()) {
            Collection<MethodBean> calls = testMethod.getMethodCalls();
            if (calls != null && calls.size() > 0) {
                for (MethodBean methodCall : testMethod.getMethodCalls()) {
                    if (!DetectionHelper.isInProductionClass(methodCall, pProductionClass)) {
                        itMethods.add(testMethod);
                        break;
                    }
                }
            }

        }
        return itMethods;
    }

    public ArrayList<MethodBean> getTestRunWarMethods(ClassBean pTestSuite) {
        boolean smelly;
        ArrayList<MethodBean> trwMethods = new ArrayList<>();
        Collection<MethodBean> testMethods = pTestSuite.getMethods();
        for (MethodBean testMethod : testMethods) {
            smelly = false;
            String textContent = testMethod.getTextContent();
            String[] lines = textContent.split("\n");
            for (String line : lines) {
                if (line.contains(" File ") && line.contains("\"")) {
                    String definedPath = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")); //EXTRACT FILE PATH
                    for (MethodBean otherTestMethod : testMethods) {
                        if (!otherTestMethod.getName().equals(testMethod.getName()) && DetectionHelper.checkFileUsage(otherTestMethod, definedPath)) {
                            smelly = true;
                            trwMethods.add(testMethod);
                            break;
                        }
                    }
                }
                if (smelly) break;
            }
        }
        return trwMethods;
    }


    public ArrayList<MethodBean> getFireAndForgetMethods(ClassBean pTestSuite) {
        boolean smelly = false;
        ArrayList<MethodBean> fafMethods = new ArrayList<>();
        for (MethodBean testMethod : pTestSuite.getMethods()) {
            Collection<MethodBean> calls = testMethod.getMethodCalls();
            String textContent = testMethod.getTextContent();
            String[] lines = textContent.split("\n");
            for (MethodBean call : calls) {
                for (int k = 0; k < lines.length; k++) {
                    if (lines[k].matches("\\s*" + call.getName() + "\\s*\\(.*") && lines[k + 1].contains("Thread.sleep")) {
                        smelly = true;
                        fafMethods.add(testMethod);
                        break;
                    }
                }
                if (smelly) break;
            }
        }
        return fafMethods;
    }

    public ArrayList<MethodBean> getConditionalTestLogicMethods(ClassBean pTestSuite) {
        ArrayList<MethodBean> ctlMethods = new ArrayList<>();
        for (MethodBean testMethod : pTestSuite.getMethods()) {
            String[] lines = testMethod.getTextContent().split("\n");
            for (String line : lines) {
                if (line.matches("\\s*if\\s*\\(.*")) {
                    ctlMethods.add(testMethod);
                    break;
                }
            }
        }
        return ctlMethods;
    }

}


