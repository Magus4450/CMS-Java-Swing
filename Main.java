import DBHelpers.DBCRUD;
import DBHelpers.DBUtils;
import GUI.CourseInfo;
import GUI.CourseRegister;
import GUI.Login;
import Users.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static DBHelpers.DBCRUD.getAllCourseData;
import static DBHelpers.DBCRUD.getCourseData;

public class Main {

    public static void main(String[] args) {

        new DBUtils();

        String a;
        new Login();
//        Student s = new Student("magus", "magus");
//        System.out.println(s.getAddress());
//        System.out.println(s.getEnrolledModules());
//        ArrayList<String> modules = new ArrayList<>();
//        modules.add("A");
//        modules.add("B");
//        String a = modules.toString();
//        a = a.replace("[", "")
//                        .replace("]", "").replace(",", "");
//        System.out.println("A" + a + "A");

//        ResultSet rs = getAllCourseData();
//        try{
//            new CourseInfo("BSc(Hons) Data Science");
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }

    }
}
