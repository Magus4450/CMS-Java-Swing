package DBHelpers;

import Modules.Course;
import Users.Student;
import Users.Teacher;
import Modules.Module;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static DBHelpers.DBUtils.*;
public class DBCRUD {

    private static Statement statement;
    
    DBCRUD(){
        Connection connection = DBUtils.getDBConnection();
        try{
            statement = connection.createStatement();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void insertIntoStudent(String[] data){
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
                `contact`,
                `enrolledCourse`,
                `passedSem`,
                `marks`,
                `enrolledModules`)
            VALUES (
              %s,"%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s");""", dbName, data[1], data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10],data[11],data[12]);

            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertIntoTeacher(String[] data){
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

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertIntoAdmin(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`ADMIN` (
                    `adminId`,
                    `username`,
                    `password`)
                VALUES (
                  "%s","%s","%s");""", dbName, data[0], data[1], data[2]);
            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertIntoCourse(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`COURSE` (
                    `courseName`,
                    `courseDuration`,
                    `courseSemesters`,
                    `courseIsAvailable`,
                    `courseModules`)
                VALUES (
                  "%s","%s","%s","%s","%s");""", dbName, data[1], data[2],data[3],data[4],data[5]);
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


    private static ResultSet getData(String username, String userType){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`%s` WHERE `username` = "%s";""", dbName, userType, username);
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
    public static void updateModuleData(Module m){
        try{
            String sql = String.format("""
                UPDATE `%s`.`MODULE`
                SET
                    `moduleName` = "%s",
                    `moduleTeacher` = "%s",
                    `courseId` = "%s"
                WHERE `moduleCode` = "%s";""", dbName, m.getModuleName(), m.getModuleTeacher(),m.getCourseId(), m.getModuleCode());
            statement.executeUpdate(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
        }

    }

    public static int getCourseCount(){
        try{
            String sql = String.format("""
                SELECT count(*) FROM `%s`.`COURSE`;""", dbName);
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

    public static void updateCourseData(Course c){
        try{

            String sql = String.format("""
                UPDATE `%s`.`COURSE`
                SET
                    `courseName` = "%s",
                    `courseIsAvailable` = %s
                WHERE `courseId` = %s;""", dbName, c.getCourseName(), c.getAvailable(), c.getCourseId());
            statement.executeUpdate(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
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
    public static ResultSet getAllNonEmptyModuleData(){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`MODULE` WHERE `moduleTeacher` != "null";""", dbName);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }
    public static ResultSet getAllModuleData(int semester, int isOptional){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`MODULE` WHERE `moduleSem` = %s AND `isOptional` = %s;""", dbName, semester, isOptional);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }
    public static ResultSet getAllOptionalModule(int isOptional){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`MODULE` WHERE `isOptional` = %s;""", dbName, isOptional);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }
    public static ResultSet getAllActiveCourse(){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`COURSE` WHERE `courseIsAvailable` = 1;""", dbName);

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();
            return null;
        }
    }

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
                    `marks` = "%s",
                    `studentRemarks` = "%s",
                    `enrolledModules` = "%s"
                WHERE `username` = "%s";""", dbName, st.getFirstName(), st.getLastName(), st.getPassword(), st.getAddress(), st.getContact(), st.getPassedSem(), st.getLevel(), st.getMarks(),st.getRemarks(), st.getEnrolledModules(), st.getUsername());
            int count = statement.executeUpdate(sql);
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
            int count = statement.executeUpdate(sql);
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
            String sql = "SELECT * FROM `"+dbName+"`.`MODULE` WHERE `moduleSem` = "+ sem +" AND `courseId` LIKE concat('%','"+enrolledCourse+"','%')";

            return statement.executeQuery(sql);

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return null;
        }

    }

    public static boolean isCourseAvailable(int courseId){
        try{

            String sql = String.format("""
                    SELECT * FROM `%s`.`COURSE` WHERE `courseId` =  %s;""", dbName, courseId);
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                return true;
            }
        } catch (SQLException e){
            System.out.println("Course Data couldn't be fetched");
            e.printStackTrace();

        }
        return false;
    }

    public static void deleteCourseData(String courseName){
        try{
            String sql = String.format("""
                DELETE FROM `%s`.`COURSE` WHERE `courseName` = "%s";""", dbName, courseName);
            statement.executeUpdate(sql);


        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();

        }
    }






}
