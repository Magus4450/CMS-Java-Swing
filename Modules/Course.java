package Modules;


import DBHelpers.DBCRUD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    private String courseName;
    private int courseDuration;
    private int courseSemesters;
    private int isAvailable;
    private int courseId;
    private String courseModules;

    public Course(int courseId) {
        this.courseId = courseId;

        try{
            ResultSet rs = DBCRUD.getCourseData(courseId);
            assert rs != null;
            if(rs.next()){
                setCourseDuration(rs.getInt("courseDuration"));
                setCourseName(rs.getString("courseName"));
                setCourseSemesters(rs.getInt("courseSemesters"));
                setCourseModules(rs.getString("courseModules"));
                setAvailable(rs.getInt("courseIsAvailable"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

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

    public void setAvailable(int available) {
        isAvailable = available;
    }

    public int getAvailable(){
        return isAvailable;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    public int getCourseDuration() {
        return courseDuration;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseModules() {
        return courseModules;
    }

    public void setCourseModules(String courseModules) {
        this.courseModules = courseModules;
    }
}
