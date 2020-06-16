package utils;

import beans.ProjectBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import it.unisa.testSmellDiffusion.utility.FolderToJavaProjectConverter;
import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.util.Vector;

public class MultiModuleParser {
    public static ProjectBean parseProject(String workingDir) throws CoreException {
        Vector<PackageBean> packages = new Vector<>();
        Vector<PackageBean> testPackages = new Vector<>();
        File root = new File(workingDir);
        File mainRoot = new File(workingDir + "/src/main");
        File testRoot = new File(workingDir + "/src/test");
        if (mainRoot.isDirectory() && testRoot.isDirectory()) {
            Vector<PackageBean> modulePackages = FolderToJavaProjectConverter.convert(mainRoot.getAbsolutePath());
            Vector<PackageBean> moduleTestPackages = FolderToJavaProjectConverter.convert(testRoot.getAbsolutePath());
            packages.addAll(modulePackages);
            testPackages.addAll(moduleTestPackages);
        }
        File[] folders = root.listFiles();
        if (folders != null) {
            for (File f : folders) {
                mainRoot = new File(f.getAbsolutePath() + "/src/main");
                testRoot = new File(f.getAbsolutePath() + "/src/test");
                if (mainRoot.isDirectory() && testRoot.isDirectory()) {
                    Vector<PackageBean> modulePackages = FolderToJavaProjectConverter.convert(mainRoot.getAbsolutePath());
                    Vector<PackageBean> moduleTestPackages = FolderToJavaProjectConverter.convert(testRoot.getAbsolutePath());
                    packages.addAll(modulePackages);
                    testPackages.addAll(moduleTestPackages);
                }
                File[] subs = f.listFiles();
                if (subs != null) {
                    for (File sub : subs) {
                        mainRoot = new File(sub.getAbsolutePath() + "/src/main");
                        testRoot = new File(sub.getAbsolutePath() + "/src/test");
                        if (mainRoot.isDirectory() && testRoot.isDirectory()) {
                            Vector<PackageBean> modulePackages = FolderToJavaProjectConverter.convert(mainRoot.getAbsolutePath());
                            Vector<PackageBean> moduleTestPackages = FolderToJavaProjectConverter.convert(testRoot.getAbsolutePath());
                            packages.addAll(modulePackages);
                            testPackages.addAll(moduleTestPackages);
                        }

                    }
                }
            }
        }

        ProjectBean bean = new ProjectBean();
        bean.setPackages(packages);
        bean.setTestPackages(testPackages);
        return bean;
    }
}
