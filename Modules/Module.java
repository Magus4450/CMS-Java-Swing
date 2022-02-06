package Modules;


import DBHelpers.DBCRUD;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Module {
    private String moduleName;
    private String moduleCode;
    private int moduleLevel;
    private int moduleCredit;
    private int isOptional;
    private int moduleSem;
    private String moduleTeacher;
    private int courseId;

    public Module(String moduleCode) throws SQLException {
        ResultSet rs = DBCRUD.getModuleData(moduleCode);
        setModuleCode(moduleCode);
        if(rs.next()){
            setModuleName(rs.getString("moduleName"));
            setModuleLevel(rs.getInt("moduleLevel"));
            setModuleCredit(rs.getInt("moduleCredit"));
            setIsOptional(rs.getInt("isOptional"));
            setModuleSem(rs.getInt("moduleSem"));
            setCourseId(rs.getInt("courseId"));

        }
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

    public int getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(int isOptional) {
        this.isOptional = isOptional;
    }

    public int getModuleSem() {
        return moduleSem;
    }

    public void setModuleSem(int moduleSem) {
        this.moduleSem = moduleSem;
    }

    public String getModuleTeacher() {
        return moduleTeacher;
    }

    public void setModuleTeacher(String moduleTeacher) {
        this.moduleTeacher = moduleTeacher;
//        DBCRUD.updateModuleData(getModuleCode(),getModuleName())
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
