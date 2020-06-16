package utils;

import beans.AnalysisOutput;
import beans.MethodAnalysisOutput;
import it.unisa.testSmellDiffusion.utility.FileUtility;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

class StreamGobbler implements Runnable {
    private final InputStream is;
    private final PrintStream os;

    StreamGobbler(InputStream is, PrintStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        try {
            while (is.read() != -1);
        } catch (
                IOException x) {
            // Handle error
        }
    }
}


public class SurefireReportExplorer {
    public static void main(String[] args) throws Exception {
        String cmd = "\"cmd.exe\", \"/c\",cd C:\\Users\\Psycho\\IdeaProjects\\okhttp && mvn compile";
        String line;
        BufferedReader br;
        BufferedReader br2;
        Hashtable<String, Integer> methods = new Hashtable<>();
        ArrayList<String> flakyMethods = new ArrayList<>();
        Process p = Runtime.getRuntime().exec(cmd);
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        br2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        while (br2.readLine() != null);
        p.waitFor();
        cmd = "\"cmd.exe\", \"/c\",cd C:\\Users\\Psycho\\IdeaProjects\\okhttp && mvn test -fn";
        for (int k = 0; k < 5; k++) {
            System.out.println("Executing tests, round " + k);
            p = Runtime.getRuntime().exec(cmd);
            Thread errorGobbler
                    = new Thread(new StreamGobbler(p.getErrorStream(), System.err));
            Thread outputGobbler
                    = new Thread(new StreamGobbler(p.getInputStream(), System.out));
            errorGobbler.start();
            outputGobbler.start();
            p.waitFor();
            errorGobbler.join();   // Handle condition where the
            outputGobbler.join();  // process ends before the threads finish
            System.out.println("End testing, round" + k);
            File xmls = new File("C:\\Users\\Psycho\\IdeaProjects\\okhttp");
            ArrayList<String> lines = new ArrayList<>();
            for (File dir : xmls.listFiles()) {
                if (dir != null && dir.isDirectory()) {
                    File[] subdirs = dir.listFiles();
                    for (File subdir : subdirs) {
                        if (subdir != null && subdir.getName().equalsIgnoreCase("target")) {
                            File[] dirs = subdir.listFiles();
                            for (File surefire : dirs) {
                                if (surefire != null && surefire.getName().equalsIgnoreCase("surefire-reports")) {
                                    File[] files = surefire.listFiles();
                                    for (File file : files) {
                                        if (file.getName().contains(".xml")) {
                                            try {
                                                br = new BufferedReader(new FileReader(file));
                                                while ((line = br.readLine()) != null) {
                                                    lines.add(line);
                                                }
                                                for (int i = 0; i < lines.size(); i++) {
                                                    String string = lines.get(i);
                                                    if (string.contains("<testcase ")) {
                                                        String[] words = string.split(" ");
                                                        String testMethod = words[3].replace("name=", "").replaceAll("\"", "");
                                                        String className = words[4].replace("classname=", "").replaceAll("\"", "");
                                                        String fullMethodName = className + "." + testMethod;
                                                        if (!flakyMethods.contains(fullMethodName)) {
                                                            int passed = 1;
                                                            if (lines.get(i + 1).contains("<failure "))
                                                                passed = 0;
                                                            if (!methods.containsKey(fullMethodName))
                                                                methods.put(fullMethodName, passed);
                                                            else {
                                                                if (methods.get(fullMethodName) == 0 && passed == 1)
                                                                    flakyMethods.add(fullMethodName);
                                                                else if (methods.get(fullMethodName) == 1 && passed == 0)
                                                                    flakyMethods.add(fullMethodName);
                                                            }
                                                        }
                                                    }
                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }


            }
        }

        String fileName = "flaky.csv";
        String outputDir = "reports";
        StringBuilder output = new StringBuilder();
        for (String flaky : flakyMethods)
            output.append(flaky).append("\n");
        File out = new File(outputDir);
        if (!out.exists() && !out.mkdirs())
            return;
        FileUtility.writeFile(output.toString(), outputDir + "/" + fileName);

    }
}
