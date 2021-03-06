package Users;

import java.sql.ResultSet;
import java.sql.SQLException;

import static DBHelpers.DBCRUD.loginAdmin;


public class Admin{


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




    public boolean login(){
        ResultSet rs = loginAdmin(getUsername(), getPassword());
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
