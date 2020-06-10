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
                            } else if (f2 != null) {
                                boolean mainsubOk = false;
                                boolean testsubOk = false;
                                File[] sub3 = f2.listFiles();
                                if(sub3 != null){
                                    for(File f4 : sub3){
                                       if(f4 != null && f4.getName().equalsIgnoreCase("src")){
                                           File[] sub4 = f4.listFiles();
                                           if(sub4 != null){
                                               for(File f5 : sub4){
                                                   if (f5 != null && f5.getName().equalsIgnoreCase("main"))
                                                       mainsubOk = true;
                                                   if (f5 != null && f5.getName().equalsIgnoreCase("test"))
                                                       testsubOk = true;
                                                   if (mainsubOk && testsubOk) break;
                                               }
                                           }
                                       }
                                    }
                                }
                                if(mainsubOk && testsubOk){
                                    Vector<PackageBean> modulePackages = FolderToJavaProjectConverter.convert(f2.getAbsolutePath() + "/src/main");
                                    Vector<PackageBean> moduleTestPackages = FolderToJavaProjectConverter.convert(f2.getAbsolutePath() + "/src/test");
                                    packages.addAll(modulePackages);
                                    testPackages.addAll(moduleTestPackages);
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
