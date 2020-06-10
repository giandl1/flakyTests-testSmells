package testSmells;

import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.beans.PackageBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class DetectionHelper {
    public static boolean isInProductionClass(MethodBean pMethodBean, ClassBean pProductionClass) {

        for (MethodBean methodBean : pProductionClass.getMethods()) {
            if(methodBean.getName().equals(pMethodBean.getName()))
                return true;
        }

        return false;
    }

    public static Collection<MethodBean> findInvocations(Vector<PackageBean> pPackages) {
        ArrayList<MethodBean> invocations = new ArrayList<>();

        for (PackageBean packageBean : pPackages) {
            for (ClassBean classBean : packageBean.getClasses()) {
                for (MethodBean methodBean : classBean.getMethods()) {
                    methodBean.setBelongingClass(classBean);
                    invocations.add(methodBean);
                }
            }
        }

        return invocations;
    }

    public static boolean checkFileUsage(MethodBean method, String path) {
        String text = method.getTextContent();
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.contains(" File ") && line.contains("\"")) {
                String definedPath = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                if (definedPath.equals(path))
                    return true;
            }
        }
        return false;
    }

}
