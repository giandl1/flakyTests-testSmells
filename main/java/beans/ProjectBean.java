package beans;

import it.unisa.testSmellDiffusion.beans.PackageBean;

import java.util.Vector;

public class ProjectBean {
    private Vector<PackageBean> packages;
    private Vector<PackageBean> testPackages;

    public Vector<PackageBean> getPackages() {
        return packages;
    }

    public void setPackages(Vector<PackageBean> packages) {
        this.packages = packages;
    }

    public Vector<PackageBean> getTestPackages() {
        return testPackages;
    }

    public void setTestPackages(Vector<PackageBean> testPackages) {
        this.testPackages = testPackages;
    }
}
