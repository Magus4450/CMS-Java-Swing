package Users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static DBHelpers.DBCRUD.*;

public class Teacher extends User{
    private ArrayList<Module> moduleList;

    public Teacher(String username, String password, String firstName, String lastName, String address, String contact){
        super(username, password, firstName, lastName,address, contact);
    }

    public Teacher(String username, String password){
        super(username, password);
    }

    @Override
    public boolean register(){
        return registerTeacher(getUsername(), getPassword(),getFirstName(), getLastName(), getAddress(),getContact());
    }

    @Override
    public boolean login(){
        ResultSet rs = getTeacherData(getUsername(), getPassword());
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
    public ArrayList<Module> getModuleList() {
        return moduleList;
    }
    public void setModuleList(ArrayList<Module> moduleList) {
        this.moduleList = moduleList;
    }


}
