package Modules;

import java.util.ArrayList;

public class Module {
    private String moduleName;
    private String moduleCode;
    private int moduleLevel;
    private int moduleCredit;
    private ArrayList<String> moduleRequirements;


    public ArrayList<String> getModuleRequirements() {
        return moduleRequirements;
    }

    public void setModuleRequirements(ArrayList<String> moduleRequirements) {
        this.moduleRequirements = moduleRequirements;
    }

    public int getModuleCredit() {
        return moduleCredit;
    }

    public void setModuleCredit(int moduleCredit) {
        this.moduleCredit = moduleCredit;
    }

    public int getModuleLevel() {
        return moduleLevel;
    }

    public void setModuleLevel(int moduleLevel) {
        this.moduleLevel = moduleLevel;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
