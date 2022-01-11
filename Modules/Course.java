package Modules;

import Users.Teacher;
import java.util.ArrayList;

public class Course {
    private String courseName;
    private int courseDuration; // In years
    private int courseSemesters;
    private boolean isAvailable;
    private ArrayList<Module> modules;

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }


    public void setCourseSemesters(int courseSemesters) {
        this.courseSemesters = courseSemesters;
    }

    public int getCourseSemesters() {
        return courseSemesters;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean getAvailable(){
        return isAvailable;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

}
