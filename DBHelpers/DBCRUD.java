package DBHelpers;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static DBHelpers.DBUtils.*;
public class DBCRUD {

    Connection connection;
    static Statement statement;

    DBCRUD(){
        connection = DBUtils.getDBConnection();
        try{
            statement = connection.createStatement();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static boolean insertIntoStudent(String[] data){
        try{
            String sql = String.format("""
            INSERT INTO `%s`.`STUDENT` (
                `studentLevel`,
                `studentRemarks`,
                `username`,
                `password`,
                `firstName`,
                `lastName`,
                `address`,
                `contact` )
            VALUES (
              %s,"%s","%s","%s","%s","%s","%s","%s");""", dbName, data[1], data[2],data[3],data[4],data[5],data[6],data[7],data[8]);

            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoTeacher(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`TEACHER` (
                   `username`,
                    `password`,
                    `firstName`,
                    `lastName`,
                    `address`,
                    `contact`)
                VALUES (
                  "%s","%s","%s","%s","%s","%s");""", dbName, data[1], data[2],data[3],data[4],data[5],data[6]);
            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoAdmin(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`ADMIN` (
                    `adminId`,
                    `username`,
                    `password`)
                VALUES (
                  "%s","%s","%s");""", dbName, data[0], data[1], data[2]);
            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void insertIntoCourse(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`COURSE` (
                    `courseId`,
                    `courseName`,
                    `courseDuration`,
                    `courseSemesters`,
                    `courseIsAvailable`,
                    `courseModules`)
                VALUES (
                  "%s","%s","%s","%s","%s","%s");""", dbName, data[0], data[1], data[2],data[3],data[4],data[5]);
            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertIntoModule(String[] data){
        try{
            String sql = String.format("""
                INSERT INTO `%s`.`MODULE` (
                    `moduleCode`,
                    `moduleName`,
                    `moduleLevel`,
                    `moduleCredit`,
                    `isOptional`)
                VALUES(
                  "%s","%s","%s","%s","%s");""", dbName, data[0], data[1], data[2],data[3],data[4]);
            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }



    public static ResultSet getStudentData(String username, String password){
        return getData(username, password, "STUDENT");
    }
    public static ResultSet getTeacherData(String username, String password){
        return getData(username, password, "TEACHER");
    }
    public static ResultSet getAdminData(String username, String password){
        return getData(username, password, "ADMIN");
    }

    private static ResultSet getData(String username, String password, String userType){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`%s` WHERE `username` = "%s" AND `password` = "%s";""", dbName, userType, username, password);
            System.out.println(sql);
            return statement.executeQuery(sql);
//            if (rs.next()){
////                rs.beforeFirst();
//                System.out.println(rs.next());
//                while(rs.next()){
//                    System.out.println(rs.getString("username"));
//                }
//                System.out.println("User logged in!");
//                return true;
//
//            } else {
//                System.out.println("User couldn't be logged in!");
//                return false;
//            }
//            statement.execute(sql);

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return null;
        }
    }
    public static boolean registerTeacher(String username, String password, String firstName, String lastName, String address, String contact){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`TEACHER` WHERE `username` = "%s";""", dbName, username);
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                System.out.println("Username is already used!");
                return false;
            } else {
                String[] arr = {null, username, password, firstName, lastName, address,contact};
                insertIntoTeacher(arr);
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean registerStudent(int level, String remarks, String username, String password, String firstName, String lastName, String address, String contact){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`STUDENT` WHERE `username` = "%s";""", dbName, username);
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                System.out.println("Username is already used!");
                return false;
            } else {
                String[] arr = {null, Integer.toString(level), remarks, username, password, firstName, lastName, address,contact};
                insertIntoStudent(arr);
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
