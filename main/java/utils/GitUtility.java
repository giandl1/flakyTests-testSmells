package utils;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ProgressMonitor;
import java.io.File;

public class GitUtility {

    public static void getProjectFromGit(String gitURL, String workingDir, ProgressMonitor monitor) throws Exception {
        CloneCommand command = Git.cloneRepository().setURI(gitURL).setDirectory(new File(workingDir));
        if (monitor != null){
            command.setProgressMonitor(monitor);
        }
        command.call();

    }
}
