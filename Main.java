import DBHelpers.DBUtils;
import GUI.AdminPanel;
import GUI.Login;
import GUI.StudentPanel;
import Users.Admin;
import Users.Student;

import java.sql.SQLException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws SQLException {

        new DBUtils();
//
        new StudentPanel(new Student("student2", "student2"));
//        new Login();
//        new AdminPanel(new Admin("admin","admin"));

    }
}
