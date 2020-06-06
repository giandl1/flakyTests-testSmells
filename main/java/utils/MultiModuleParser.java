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
        File[] folders = root.listFiles();
        if (folders != null) {
            for (File f : folders) {
                if (f != null && f.isDirectory()) {
                    boolean mainOk = false;
                    boolean testOk = false;
                    File[] subFolders = f.listFiles();
                    if (subFolders != null) {
                        for (File f2 : subFolders) {
                            if (f2 != null && f2.getName().equalsIgnoreCase("src")) {
                                File[] sub2 = f2.listFiles();
                                if (sub2 != null) {
                                    for (File f3 : sub2) {
                                        if (f3 != null && f3.getName().equalsIgnoreCase("main"))
                                            mainOk = true;
                                        if (f3 != null && f3.getName().equalsIgnoreCase("test"))
                                            testOk = true;
                                        if (mainOk && testOk) break;
                                    }
                                }
                            }
                            if (mainOk && testOk) {
                                Vector<PackageBean> modulePackages = FolderToJavaProjectConverter.convert(f.getAbsolutePath() + "/src/main");
                                Vector<PackageBean> moduleTestPackages = FolderToJavaProjectConverter.convert(f.getAbsolutePath() + "/src/test");
                                packages.addAll(modulePackages);
                                testPackages.addAll(moduleTestPackages);
                            }
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
