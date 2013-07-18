package com.lroguet.jenkins.plugin.puppet.validator.log;

import hudson.model.BuildListener;

import java.io.Serializable;

public class Logger implements Serializable {

    private static final long serialVersionUID = 4159420599005165907L;
    private BuildListener listener;


    public Logger(BuildListener listener) {
        this.listener = listener;
    }


    public void printSeparator() {
        this.listener.getLogger().println("+----------------------------------------------------------------------+");
    }


    public void info(String message) {
        this.listener.getLogger().println("[INFO] " + message);

    }


    public void warn(String message) {
        this.listener.getLogger().println("[WARN] " + message);

    }


    public void error(String message) {
        this.listener.getLogger().println("[ERROR] " + message);

    }

}
