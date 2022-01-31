package DBHelpers;

import Users.Student;
import Users.Teacher;
import com.mysql.cj.protocol.Resultset;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

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
            System.out.println(Arrays.toString(data));
            String sql = String.format("""
            INSERT INTO `%s`.`STUDENT` (
                `studentLevel`,
                `studentRemarks`,
                `username`,
                `password`,
                `firstName`,
                `lastName`,
                `address`,
                `contact`,
                `enrolledCourse`,
                `passedSem`,
                `marks`,
                `enrolledModules`)
            VALUES (
              %s,"%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s");""", dbName, data[1], data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12]);

            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoTeacher(String[] data){
        System.out.println(Arrays.toString(data));
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`TEACHER` (
                   `username`,
                    `password`,
                    `firstName`,
                    `lastName`,
                    `address`,
                    `contact`,
                    `teacherModules`)
                VALUES (
                  "%s","%s","%s","%s","%s","%s","%s");""", dbName, data[1], data[2],data[3],data[4],data[5],data[6],data[7]);
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
                    `isOptional`,
                    `moduleSem`,
                    `moduleTeacher`,
                    `courseId`)
                VALUES(
                  "%s","%s",%s,%s,%s,%s,"%s",%s);""", dbName, data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7]);
            statement.executeUpdate(sql);

        } catch (SQLException e){

            e.printStackTrace();
        }
    }


    public static ResultSet loginStudent(String username, String password){
        return login(username, password, "STUDENT");
    }
    public static ResultSet loginTeacher(String username, String password){
        return login(username, password, "TEACHER");
    }
    public static ResultSet loginAdmin(String username, String password){
        return login(username, password, "ADMIN");
    }

    private static ResultSet login(String username, String password, String userType){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`%s` WHERE `username` = "%s" AND `password` = "%s";""", dbName, userType, username, password);
            System.out.println(sql);
            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return null;
        }
    }
    public static ResultSet getStudentData(String username){
        return getData(username, "STUDENT");
    }
    public static ResultSet getTeacherData(String username){
        return getData(username, "TEACHER");
    }
    public static ResultSet getAdminData(String username){
        return getData(username, "ADMIN");
    }

    private static ResultSet getData(String username, String userType){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`%s` WHERE `username` = "%s";""", dbName, userType, username);
            System.out.println(sql);
            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getStudentDataFromModule(String moduleCode){
        try{
            String sql = "SELECT * FROM `"+dbName+"`.`STUDENT` WHERE `enrolledModules` LIKE concat('%','"+moduleCode+"','%')";
            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean registerTeacher(String username, String password, String firstName, String lastName, String address, String contact, String teacherModules){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`TEACHER` WHERE `username` = "%s";""", dbName, username);
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                System.out.println("Username is already used!");
                return false;
            } else {
                String[] arr = {null, username, password, firstName, lastName, address,contact, teacherModules};
                insertIntoTeacher(arr);
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean registerStudent(int level, String remarks, String username, String password, String firstName, String lastName, String address, String contact, int enrolledCourse, int passedSem, String marks, String enrolledModules){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`STUDENT` WHERE `username` = "%s";""", dbName, username);
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                System.out.println("Username is already used!");
                return false;
            } else {
                String[] arr = {null, Integer.toString(level), remarks, username, password, firstName, lastName, address,contact, String.valueOf(enrolledCourse), Integer.toString(passedSem), marks, enrolledModules};
                insertIntoStudent(arr);
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public static ResultSet getCourseData(int courseId){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`COURSE` WHERE `courseId` = %s;""", dbName, courseId);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }
    public static int getCourseId(String courseName){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`COURSE` WHERE `courseName` = "%s";""", dbName, courseName);

            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                return rs.getInt("courseId");
            }

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
        return -1;
    }

    public static ResultSet getModuleData(String moduleCode){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`MODULE` WHERE `moduleCode` = "%s";""", dbName, moduleCode);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }
    public static boolean updateModuleData(String moduleCode, String moduleName){
        try{
            String sql = String.format("""
                UPDATE `%s`.`MODULE`
                SET
                    `moduleName` = "%s"
                WHERE `moduleCode` = "%s";""", dbName, moduleName, moduleCode);
            int count = statement.executeUpdate(sql);
            System.out.println("Rows Affected: " + count);
            return (count > 0);


        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
        return false;

    }

    public static boolean updateCourseData(int courseId, String courseName, int isCourseAvailable){
        try{
            String sql = String.format("""
                UPDATE `%s`.`COURSE`
                SET
                    `courseName` = "%s",
                    `courseIsAvailable` = %s
                WHERE `courseId` = %s;""", dbName, courseName, isCourseAvailable, courseId);
            System.out.println(sql);
            int count = statement.executeUpdate(sql);
            System.out.println("Rows Affected: " + count);
            return (count > 0);


        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
        return false;
    }

    public static ResultSet getAllCourseData(){
        return getAllData("COURSE");
    }
    public static ResultSet getAllTeacherData(){
       return getAllData("TEACHER");
    }
    public static ResultSet getAllStudentData(){
        return getAllData("STUDENT");
    }
    public static ResultSet getAllModuleData(){return  getAllData("MODULE");}

    public static ResultSet getAllData(String tableName){

        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`%s`;""", dbName, tableName);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateStudentData(Student st){
        try{
            String sql = String.format("""
                UPDATE `%s`.`STUDENT`
                SET
                    `firstName` = "%s",
                    `lastName` = "%s",
                    `password` = "%s",
                    `address` = "%s",
                    `contact` = "%s",
                    `passedSem` = %s,
                    `studentLevel` = %s,
                    `marks` = "%s"
                WHERE `username` = "%s";""", dbName, st.getFirstName(), st.getLastName(), st.getPassword(), st.getAddress(), st.getContact(), st.getPassedSem(), st.getLevel(), st.getMarks(),st.getUsername());
            System.out.println(sql);
            int count = statement.executeUpdate(sql);
            System.out.println("Rows Affected: " + count);
            return (count > 0);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
        return false;

    }
    public static boolean updateTeacherData(Teacher t){
        try{
            String sql = String.format("""
                UPDATE `%s`.`TEACHER`
                SET
                    `firstName` = "%s",
                    `lastName` = "%s",
                    `password` = "%s",
                    `address` = "%s",
                    `contact` = "%s",
                    `teacherModules` = "%s"
                WHERE `username` = "%s";""", dbName, t.getFirstName(), t.getLastName(), t.getPassword(), t.getAddress(), t.getContact(),t.getTeacherModules(),t.getUsername());
            System.out.println(sql);
            int count = statement.executeUpdate(sql);
            System.out.println("Rows Affected: " + count);
            return (count > 0);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
        return false;

    }

    public static int getStudentCount(String moduleCode, int sem){
        try{

            sem -= 1;
            String sql = "SELECT count(*) FROM `"+dbName+"`.`STUDENT` WHERE `passedSem` = " + sem + " AND`enrolledModules` LIKE concat('%','"+moduleCode+"','%')";
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                return rs.getInt("count(*)");

            }
        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }

        return -1;

    }
    public static int getStudentCount(int courseId){
        try{

            String sql = String.format("""
                    SELECT count(*) FROM `%s`.`STUDENT` WHERE `enrolledCourse` =  %s;""", dbName, courseId);
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                return rs.getInt("count(*)");

            }
        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }

        return -1;

    }


    public static ResultSet getModules(String enrolledCourse, int sem){

        try{
            String sql = String.format("""
                SELECT courseId FROM `%s`.`COURSE` WHERE `courseName` = "%s";""", dbName, enrolledCourse);

            ResultSet rs = statement.executeQuery(sql);
            int courseId = 0;
            if(rs.next()){
                courseId = rs.getInt("courseId");
            }
            sql = String.format("""
                SELECT * FROM `%s`.`MODULE` WHERE `courseId` = %s AND `moduleSem` = %s;""", dbName, courseId, sem);

            rs = statement.executeQuery(sql);
            return rs;

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return null;
        }

    }







    public static void main(String[] args) {
//        ResultSet rs = getCourseData("BSc(Hons) Computer Science");
//        try{
//            if(rs.next()){
//                System.out.println(rs.toString());
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//        }

    }

}
