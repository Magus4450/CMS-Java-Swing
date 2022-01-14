package Users;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DBHelpers.DBCRUD.*;


public class Student extends User {


    private int level;
    private String remarks;

    public Student(String username, String password, String firstName, String lastName, String address, String contact){
        super(username, password, firstName, lastName,address, contact);
        this.level = 4;
        this.remarks = null;
    }

    public Student(String username, String password, String firstName, String lastName, String address, String contact, String level, String remarks){
        super(username, password, firstName, lastName,address, contact);
        this.level = Integer.parseInt(level);
        this.remarks = remarks;
    }

    public Student(String username, String password){
        super(username, password);
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
        return registerStudent(getLevel(), getRemarks(),getUsername(), getPassword(),getFirstName(), getLastName(), getAddress(),getContact());
    }

    @Override
    public boolean login(){
        ResultSet rs = getStudentData(getUsername(), getPassword());
        try{
            if(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");

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

    public static void main(String[] args) {
        //
    }
}
