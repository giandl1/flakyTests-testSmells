package utils;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;

public class GitUtility {

    public static void getProjectFromGit(String gitURL, String rev, String workingDir) throws Exception {
        CloneCommand command = Git.cloneRepository().setURI(gitURL).setDirectory(new File(workingDir));
        TextProgressMonitor monitor = new TextProgressMonitor();
        if (monitor != null){
            command.setProgressMonitor(monitor);
        }
        Git git = command.call();
        CheckoutCommand checkout = git.checkout().setName(rev);
        checkout.setProgressMonitor(monitor);
        checkout.call();

    }

    public static void main(String[] args) throws Exception{
        CloneCommand command = Git.cloneRepository().setURI("https://github.com/wro4j/wro4j.git").setDirectory(new File("resources/project"));
        TextProgressMonitor monitor = new TextProgressMonitor();
        if (monitor != null){
            command.setProgressMonitor(monitor);
        }
        Git git = command.call();
        CheckoutCommand checkout = git.checkout().setName("0f0e9e0b0d256e7183d27aeccd9830cc100de77b");
        checkout.setProgressMonitor(monitor);
        checkout.call();
    }
}
