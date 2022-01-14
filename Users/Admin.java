package Users;

import java.sql.ResultSet;
import java.sql.SQLException;

import static DBHelpers.DBCRUD.getAdminData;
import static DBHelpers.DBCRUD.getTeacherData;

public class Admin{

    private int id;
    private String username;
    private String password;


    public Admin(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean login(){
        ResultSet rs = getAdminData(getUsername(), getPassword());
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
}
