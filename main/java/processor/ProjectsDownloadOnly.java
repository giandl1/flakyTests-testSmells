package processor;
import beans.FlakyCSVOutput;
import utils.CSVParser;
import utils.GitUtility;

import java.util.ArrayList;

public class ProjectsDownloadOnly {
    public static void main(String[] args){
        ArrayList<String> revs = new CSVParser().getProjectFlakyRevisions();
        ArrayList<String> urls = new CSVParser().getProjectsToAnalyze();
        for (String projectRev : revs) {
            String project = projectRev.split("/")[0];
            String rev = projectRev.split("/")[1];
            String projectURL = "";
            for (String url : urls) {
                if (url.toLowerCase().contains(project.toLowerCase()))
                    projectURL = url;
            }

            System.out.println(projectRev);
            System.out.println("url: " + projectURL);
            ArrayList<FlakyCSVOutput> flakyMethods = new ArrayList<>();
            try {
                GitUtility.getProjectFromGit(projectURL, rev, "E:/FlakySmellStudy/" + projectRev);
            } catch (Exception ignored) {
            }
        }

    }
}
