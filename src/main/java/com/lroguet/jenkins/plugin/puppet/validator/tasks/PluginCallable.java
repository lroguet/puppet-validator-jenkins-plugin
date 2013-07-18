package com.lroguet.jenkins.plugin.puppet.validator.tasks;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.remoting.Callable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.lroguet.jenkins.plugin.puppet.validator.log.Logger;

@SuppressWarnings("serial")
public class PluginCallable implements Callable<Boolean, IOException> {

    private FilePath workspace;
    private BuildListener listener;
    private Logger logger;

    private String command = "puppet parser validate";
    private String[] extensions = { "pp", "PP" };


    public PluginCallable(FilePath workspace, BuildListener listener, String command) {
        super();
        this.workspace = workspace;
        this.listener = listener;
        this.logger = new Logger(this.listener);
        this.prepareCommand(command);
    }


    private void prepareCommand(String command) {
        if (command != null && !command.isEmpty()) {
            if (!command.equalsIgnoreCase(this.command)) {
                this.command = command;
            }
        }
    }


    public Boolean call() throws IOException {

        String base = this.workspace.getRemote();        
        this.logger.info("Workspace: " + base);
        
        Collection<File> classes = FileUtils.listFiles(new File(base), extensions, true);

        if (classes == null || classes.isEmpty()) {
            this.logger.info("The current project does not contain any .pp files.");
            return true;
        } else {
            for (File clazz : classes) {
                try {
                    Process p = Runtime.getRuntime().exec(command + " " + clazz.getAbsolutePath());
                    int exit = p.waitFor();
                    if (exit != 0) {
                        this.logger.error("Could not validate " + clazz.getName() + ". Aborting.");
                        return false;
                    }
                    // Just to make the info nicer, print the relative path to the Puppet class
                    String path = clazz.getAbsolutePath().replace(base, "");
                    this.logger.info(path + " is a valid Puppet class.");
                } catch (InterruptedException e) {
                    this.logger.error("Something went wrong during validation. Aborting.\n" + e.getMessage());
                    return false;
                }
            }
        }

        return true;

    }

}
