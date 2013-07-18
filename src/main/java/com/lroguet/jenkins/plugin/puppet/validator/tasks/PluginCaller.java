package com.lroguet.jenkins.plugin.puppet.validator.tasks;

import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

import com.lroguet.jenkins.plugin.puppet.validator.Plugin;
import com.lroguet.jenkins.plugin.puppet.validator.log.Logger;

public class PluginCaller extends Builder {

    private String command;


    @DataBoundConstructor
    public PluginCaller(String command) {
        this.command = command;
    }


    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {

        Logger logger = new Logger(listener);
        logger.printSeparator();
        logger.info("| " + Plugin.NAME + " begins.");
        logger.printSeparator();

        if (build != null && build.getResult() != null && build.getResult().isWorseThan(Result.SUCCESS)) {
            logger.error("Will not run. The build was not successful.");
            throw new AbortException();
        } else {
            Boolean result = launcher.getChannel().call(new PluginCallable(build.getWorkspace(), listener, this.command));
            if (!result) {
                build.setResult(Result.UNSTABLE);
            }
        }

        logger.printSeparator();
        logger.info("| " + Plugin.NAME + " is done.");
        logger.printSeparator();

        return true;
    }

    @Extension(ordinal = 99)
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public DescriptorImpl() {
            super(PluginCaller.class);
            load();
        }


        @Override
        public String getDisplayName() {
            return Plugin.NAME;
        }


        @SuppressWarnings("rawtypes")
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

    }


    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

}
