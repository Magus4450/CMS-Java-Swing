package Users;

import java.sql.ResultSet;
import java.sql.SQLException;

import static DBHelpers.DBCRUD.*;

public class Teacher extends User{
    private String teacherModules;

    public Teacher(String username, String password, String firstName, String lastName, String address, String contact){
        super(username, password, firstName, lastName,address, contact);
        this.teacherModules = null;
    }

    public Teacher(String username){
        super(username);
        ResultSet rs = getTeacherData(username);
        try{
            if(rs.next()){
                setPassword(rs.getString("password"));
                setFirstName(rs.getString("firstName"));
                setLastName(rs.getString("lastName"));
                setAddress(rs.getString("address"));
                setContact(rs.getString("contact"));
                setTeacherModules(rs.getString("teacherModules"));

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Teacher(String username, String password){
        super(username, password);
    }



    @Override
    public boolean register(){
        return registerTeacher(getUsername(), getPassword(),getFirstName(), getLastName(), getAddress(),getContact(), getTeacherModules());
    }

    @Override
    public boolean login(){
        ResultSet rs = loginTeacher(getUsername(), getPassword());
        try{
            if(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");
                setFirstName(rs.getString("firstName"));
                setLastName(rs.getString("lastName"));
                setAddress(rs.getString("address"));
                setContact(rs.getString("contact"));
                setTeacherModules(rs.getString("teacherModules"));

                if(getUsername().equals(username) && getPassword().equals(password)){
                    return true;
                }
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public String getTeacherModules() {
        return teacherModules;
    }
    public void setTeacherModules(String teacherModules) {
        this.teacherModules = teacherModules;
    }


}
