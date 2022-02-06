package Users;
import DBHelpers.DBCRUD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static DBHelpers.DBCRUD.*;


public class Student extends User {


    private int level;
    private String remarks;
    private int enrolledCourse;
    private int passedSem;
    private String marks;
    private String enrolledModules;


    public Student(String username, String password, String firstName, String lastName, String address, String contact){
        super(username, password, firstName, lastName,address, contact);
        this.level = 4;
        this.remarks = "null null null null null null";
        this.marks = "TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD";
        this.enrolledModules = null;
    }

    public Student(String username, String password, String firstName, String lastName, String address, String contact, String level, String remarks){
        super(username, password, firstName, lastName,address, contact);
        this.level = Integer.parseInt(level);
        this.remarks = "null null null null null null";
        this.marks = "TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD TBD";
        this.enrolledModules = null;
    }

    public Student(String username, String password){

        super(username, password);
        login();
    }
    public Student(String username){
        super(username);
        ResultSet rs = DBCRUD.getStudentData(getUsername());
        try{
            if(rs.next()){

                setPassword(rs.getString("password"));
                setFirstName(rs.getString("firstName"));
                setLastName(rs.getString("lastName"));
                setAddress(rs.getString("address"));
                setContact(rs.getString("contact"));
                setEnrolledCourse(rs.getInt("enrolledCourse"));
                setPassedSem(rs.getInt("passedSem"));
                setLevel(rs.getInt("studentLevel"));
                setMarks(rs.getString("marks"));
                setEnrolledModules(rs.getString("enrolledModules"));
                setRemarks(rs.getString("studentRemarks"));

            }



        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public boolean register(){
        return registerStudent(getLevel(), getRemarks(),getUsername(), getPassword(),getFirstName(), getLastName(), getAddress(),getContact(), getEnrolledCourse(), getPassedSem(), getMarks(), getEnrolledModules());
    }

    @Override
    public boolean login(){
        ResultSet rs = loginStudent(getUsername(), getPassword());
        try{
            if(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");
                setFirstName(rs.getString("firstName"));
                setLastName(rs.getString("lastName"));
                setAddress(rs.getString("address"));
                setContact(rs.getString("contact"));
                setEnrolledCourse(rs.getInt("enrolledCourse"));
                setPassedSem(rs.getInt("passedSem"));
                setLevel(rs.getInt("studentLevel"));
                setMarks(rs.getString("marks"));
                setEnrolledModules(rs.getString("enrolledModules"));
                setRemarks(rs.getString("studentRemarks"));

                return getUsername().equals(username) && getPassword().equals(password);
            }
            return false;


        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void promoteSem() throws SQLException {
        setPassedSem(getPassedSem()+1);

        ArrayList<String> allModules = null;
        ArrayList<String> enrolledModules = new ArrayList<>(Arrays.asList(getEnrolledModules().split(" ")));

        ResultSet rs = DBCRUD.getCourseData(getEnrolledCourse());

        if(rs.next()){
            allModules = new ArrayList<>(Arrays.asList(rs.getString("courseModules").split(" ")));
        }
        assert allModules != null;
        if(getPassedSem() < 4){
            int start = 4*getPassedSem();
            for(int i = 0; i < 4; i++){
                enrolledModules.add(allModules.get(start + i));
            }
        }else if (getPassedSem() == 4){
            int start = 16;
            for(int i = 0; i < 2; i++){
                enrolledModules.add(allModules.get(start + i));
            }
            enrolledModules.add("(Elective)");
        }else if (getPassedSem() == 5){
            int start = 18;
            for(int i = 0; i < 2; i++){
                enrolledModules.add(allModules.get(start + i));
            }
            enrolledModules.add("(Elective)");
        }

        setEnrolledModules(enrolledModules.toString().replace("[","").replace("]","").replace(",",""));

        if(getPassedSem()<2){
            setLevel(4);
        }else if(getPassedSem()<4){
            setLevel(5);
        }else{
            setLevel(6);
        }
    }

    public void setMarksModule(String moduleCode, int mark){
        String[] s = enrolledModules.split(" ");
        String[] m = marks.split(" ");
        ArrayList<String> modules = new ArrayList<>(List.of(s));
        ArrayList<String> marks = new ArrayList<>(List.of(m));
        for(String module: modules){
            if (module.equals(moduleCode)){
                marks.set(modules.indexOf(module),Integer.toString(mark));
            }
        }

        setEnrolledModules(modules.toString().replace("[", "").replace("]", "").replace(",", ""));
        setMarks(marks.toString().replace("[", "").replace("]", "").replace(",", ""));
    }
    public int getEnrolledCourse() {
        return enrolledCourse;
    }

    public void setEnrolledCourse(int enrolledCourse) {
        this.enrolledCourse = enrolledCourse;
    }

    public int getPassedSem() {
        return passedSem;
    }

    public void setPassedSem(int passedSem) {
        this.passedSem = passedSem;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getEnrolledModules() {
        return enrolledModules;
    }

    public void setEnrolledModules(String enrolledModules) {
        this.enrolledModules = enrolledModules;
    }
}
