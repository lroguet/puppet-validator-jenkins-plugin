package com.lroguet.jenkins.plugin.puppet.validator;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

public class PluginSettings implements Serializable {

    private static final long serialVersionUID = -440938276857732825L;
    private String puppetParserValidateCommand;


    @DataBoundConstructor
    public PluginSettings(String puppetParserValidateCommand) {
        super();
        this.puppetParserValidateCommand = puppetParserValidateCommand;
    }


    public String getPuppetParserValidateCommand() {
        return puppetParserValidateCommand;
    }


    public void setPuppetParserValidateCommand(String puppetParserValidateCommand) {
        this.puppetParserValidateCommand = puppetParserValidateCommand;
    }

}
