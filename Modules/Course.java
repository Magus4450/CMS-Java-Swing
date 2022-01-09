package Modules;

import Users.Teacher;
import java.util.ArrayList;

public class Course {
    private String courseName;
    private int courseCredit;
    private int courseDuration; // In years
    private int courseSemesters;
    private boolean isAvailable;
    private ArrayList<Teacher> teachers;
    private ArrayList<Module> modules;

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    public int getCourseCredit() {
        return courseCredit;
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

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }
}
