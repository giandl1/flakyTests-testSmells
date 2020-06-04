package flakyTests;


import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class FlakyTestsProcessor {
    public ArrayList<String> getFlakyMethods(ClassBean pTestSuite, String path) throws IOException {
        Hashtable<String, Integer> passedTests = new Hashtable<>();
        Hashtable<String, Integer> flaky = new Hashtable<>();
        ArrayList<String> flakyMethods = new ArrayList<>();
        Collection<MethodBean> methods = pTestSuite.getMethods();
        boolean firstTime = true;
        for (MethodBean method : methods)
            flaky.put(method.getName(), 0);
        for (int x = 0; x < 10; x++) {
            String cmd = "\"cmd.exe\", \"/c\", mvn -Dtest=" + pTestSuite.getBelongingPackage() + "." + pTestSuite.getName() + " test -f " + path;
            System.out.println("START TESTING");
            String s;
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if (firstTime) {
                while ((s = stdOut.readLine()) != null) {
                    System.out.println(s);
                    if (s.startsWith("Running ")) {
                        String[] splitted = s.split(" ");
                        String testName = splitted[1];
                        s = stdOut.readLine();
                        System.out.println(s);
                        if (s.startsWith("Tests run")) {
                            splitted = s.split(",");
                            String[] splitted2 = splitted[0].split(" ");
                            int runs = Integer.parseInt(splitted2[splitted2.length - 1]);
                            splitted2 = splitted[1].split(" ");
                            int failures = Integer.parseInt(splitted2[splitted2.length - 1]);
                            splitted2 = splitted[2].split(" ");
                            int errors = Integer.parseInt(splitted2[splitted2.length - 1]);
                            splitted2 = splitted[3].split(" ");
                            int skipped = Integer.parseInt(splitted2[splitted2.length - 1]);
                            if (failures + errors + skipped == 0) {
                                passedTests.put(testName, 1);
                            } else {
                                passedTests.put(testName, 0);
                            }
                        }
                    }
                }
            } else {
                while ((s = stdOut.readLine()) != null) {
                    if (s.startsWith("Running ")) {
                        String[] splitted = s.split(" ");
                        String testName = splitted[1];
                        s = stdOut.readLine();
                        if (s.startsWith("Tests run")) {
                            splitted = s.split(",");
                            String[] splitted2 = splitted[0].split(" ");
                            int runs = Integer.parseInt(splitted2[splitted2.length - 1]);
                            splitted2 = splitted[1].split(" ");
                            int failures = Integer.parseInt(splitted2[splitted2.length - 1]);
                            splitted2 = splitted[2].split(" ");
                            int errors = Integer.parseInt(splitted2[splitted2.length - 1]);
                            splitted2 = splitted[3].split(" ");
                            int skipped = Integer.parseInt(splitted2[splitted2.length - 1]);
                            int isFlaky = flaky.get(testName);
                            if (isFlaky == 0) {
                                int passed = passedTests.get(testName);
                                boolean hasFailed = (failures + errors + skipped) == 0;
                                if (hasFailed && passed == 1)
                                    flaky.replace(testName, 1);
                                else if (!hasFailed && passed == 0)
                                    flaky.replace(testName, 1);
                            }

                        }
                    }
                }
            }
            System.out.println("END TESTING");
            firstTime = false;
        }
        return flakyMethods;
    }
}
