package DBHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class DBUtils {

    static final String url = "jdbc:mysql://localhost:3306/jdbc";
    static final String user = "root";
    static final String password = "Poophead009";

    private boolean isDataDumped = false;

    static final String dbName = "CMS2059521";
    Connection connection;
    Statement statement;

    public DBUtils(){
        try{
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();
            System.out.println("Connection Established");
        } catch(Exception e){
            System.out.println("SQL Server is Offline!");
            e.printStackTrace();
        }
        this.createDatabase();
        this.dumpData();
    }

    private void createDatabase(){
        try{
            statement.execute("CREATE DATABASE " + dbName);

            System.out.println("Database " + dbName + " created successfully!");

            this.createTables();
        } catch(SQLException e){
            System.out.println("Database already exists!");
        }

    }


    private void createTables(){

//        this.createUserTable();
        this.createTeacherTable();
        this.createStudentTable();
        this.createAdminTable();
        this.createCourseTable();
        this.createModuleTable();
    }

    private void createTeacherTable(){
        try{
            String sql = String.format("""
                CREATE TABLE `%s`.`TEACHER` (
                  `teacherId` INT NOT NULL AUTO_INCREMENT,
                   `username` VARCHAR(45) NULL,
                    `password` VARCHAR(200) NULL,
                    `firstName` VARCHAR(45) NULL,
                    `middleName` VARCHAR(45) NULL,
                    `lastName` VARCHAR(45) NULL,
                    `address` VARCHAR(45) NULL,
                    `contact` VARCHAR(45) NULL,
                  PRIMARY KEY (`teacherId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table teacher created successfully");
        } catch (SQLException e){
            System.out.println("Table teacher couldn't be created");
            e.printStackTrace();
        }
    }

    private void createStudentTable(){
        try{
            String sql = String.format("""
                CREATE TABLE `%s`.`STUDENT` (
                  `studentId` INT NOT NULL AUTO_INCREMENT,
                  `studentLevel` INT NOT NULL,
                  `studentRemarks` VARCHAR(200) NULL,
                  `username` VARCHAR(45) NULL,
                    `password` VARCHAR(200) NULL,
                    `firstName` VARCHAR(45) NULL,
                    `middleName` VARCHAR(45) NULL,
                    `lastName` VARCHAR(45) NULL,
                    `address` VARCHAR(45) NULL,
                    `contact` VARCHAR(45) NULL,
                  PRIMARY KEY (`studentId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table student created successfully");
        } catch (SQLException e){
            System.out.println("Table student couldn't be created");
            e.printStackTrace();
        }
    }

    private void createAdminTable(){
        try{
            String sql = String.format("""
                CREATE TABLE `%s`.`ADMIN` (
                  `adminId` INT NOT NULL AUTO_INCREMENT,
                  `username` VARCHAR(45) NULL,
                    `password` VARCHAR(200) NULL,
                    `firstName` VARCHAR(45) NULL,
                    `middleName` VARCHAR(45) NULL,
                    `lastName` VARCHAR(45) NULL,
                    `address` VARCHAR(45) NULL,
                    `contact` VARCHAR(45) NULL,
                  PRIMARY KEY (`adminId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table admin created successfully");
        } catch (SQLException e){
            System.out.println("Table admin couldn't be created");
            e.printStackTrace();
        }
    }

    private void createModuleTable(){
        try{
            // moduleRequirements -> store id of modules
            String sql = String.format("""
                CREATE TABLE `%s`.`MODULE` (
                  `moduleCode` VARCHAR(10) NOT NULL,
                  `moduleName` VARCHAR(45) NULL,
                  `moduleLevel` INT NULL,
                  `moduleCredit` INT NULL,
                  `isOptional` INT NOT NULL,
                  PRIMARY KEY (`moduleCode`));""", dbName);

            statement.execute(sql);
            System.out.println("Table module created successfully");
        } catch (SQLException e){
            System.out.println("Table module couldn't be created");
            e.printStackTrace();
        }
    }

    private void createCourseTable(){
        try{

            // courseTeachers -> Store id of teachers
            // courseModules -> Store id of modules
            String sql = String.format("""
                CREATE TABLE `%s`.`course` (
                  `courseId` INT NOT NULL,
                  `courseName` VARCHAR(100) NOT NULL,
                  `courseDuration` INT NOT NULL,
                  `courseSemesters` INT NOT NULL,
                  `courseIsAvailable` INT NOT NULL,
                  `courseModules` VARCHAR(200) NOT NULL,
                  PRIMARY KEY (`courseId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table course created successfully");
        } catch (SQLException e){
            System.out.println("Table course couldn't be created");
            e.printStackTrace();
        }
    }

    public boolean insertIntoStudent(String[] data){
        try{

            String sql = String.format("""
            INSERT INTO `%s`.`STUDENT` (
                `studentId`,
                `studentLevel`,
                `studentRemarks`,
                `username`,
                `password`,
                `firstName`,
                `middleName`,
                `lastName`,
                `address`,
                `contact` )
            VALUES (
              "%s","%s","%s","%s","%s","%s","%s","%s","%s","%s");""", dbName, data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9]);
            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertIntoTeacher(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`TEACHER` (
                    `teacherId`,
                   `username`,
                    `password`,
                    `firstName`,
                    `middleName`,
                    `lastName`,
                    `address`,
                    `contact`)
                VALUES (
                  "%s","%s","%s","%s","%s","%s","%s","%s");""", dbName, data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7]);
            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertIntoAdmin(String[] data){
        try{

            String sql = String.format("""
                INSERT INTO `%s`.`ADMIN` (
                    `adminId`,
                    `username`,
                    `password`,
                    `firstName`,
                    `middleName`,
                    `lastName`,
                    `address`,
                    `contact`)
                VALUES (
                  "%s","%s","%s","%s","%s","%s","%s","%s");""", dbName, data[0], data[1], data[2],data[3],data[4],data[5],data[6],data[7]);
            statement.executeUpdate(sql);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void insertIntoCourse(String[] data){
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

    public void insertIntoModule(String[] data){
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



    private void dumpData(){
        if(!this.isDataDumped){
            this.dumpAdminData();
            this.dumpStudentData();
            this.dumpTeacherData();
            this.dumpModuleData();
            this.dumpCourseData();
            this.isDataDumped = true;
        }

    }
    private void dumpStudentData() {

        try {
            Scanner sc = new Scanner(new File("src/DummyData/studentData.csv"));
            String[] data;
            while (sc.hasNext()) {
                data = sc.next().split(",");
                this.insertIntoStudent(data);
            }
            System.out.println("Student data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("studentData.csv not Found!");
            e.printStackTrace();

        }
    }

    private void dumpTeacherData(){
        try{
            Scanner sc = new Scanner(new File("src/DummyData/teacherData.csv"));
            String[] data;
            while (sc.hasNext()){
                data = sc.next().split(",");
                this.insertIntoTeacher(data);
            }
            System.out.println("Teacher data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {

            System.out.println("studentData.csv not Found!");
            e.printStackTrace();
        }
    }

    private void dumpAdminData(){
        try{
            Scanner sc = new Scanner(new File("src/DummyData/adminData.csv"));
            String[] data;
            while (sc.hasNext()){
                data = sc.next().split(",");
                this.insertIntoAdmin(data);
            }
            System.out.println("Admin data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e){
            System.out.println("studentData.csv not Found!");
            e.printStackTrace();
        }

    }

    private void dumpCourseData(){
        try{
            Scanner sc = new Scanner(new File("src/DummyData/courseData.csv"));
            String[] data;
            while (sc.hasNextLine()){
                data = sc.nextLine().split(",");
                this.insertIntoCourse(data);
            }
            System.out.println("Course data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e){
            System.out.println("courseData.csv not Found!");
            e.printStackTrace();
        }

    }

    private void dumpModuleData(){
        try{
            Scanner sc = new Scanner(new File("src/DummyData/moduleData.csv"));
            String[] data;
            while (sc.hasNextLine()){
                data = sc.nextLine().split(",");
                this.insertIntoModule(data);
            }
            System.out.println("Module data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e){
            System.out.println("moduleData.csv not Found!");
            e.printStackTrace();
        }

    }

    public boolean loginStudent(String username, String password){
        return this.login(username, password, "STUDENT");
    }
    public boolean loginTeacher(String username, String password){
        return this.login(username, password, "TEACHER");
    }
    public boolean loginAdmin(String username, String password){
        return this.login(username, password, "ADMIN");
    }

    private boolean login(String username, String password, String userType){
        try{
            String sql = String.format("""
                SELECT * FROM `%s`.`%s` WHERE `username` = "%s" AND `password` = "%s";""", dbName, userType, username, password);
            System.out.println(sql);
            ResultSet rs = statement.executeQuery(sql);
            if (rs != null){
                while(rs.next()){
                    System.out.println(rs.getString("username"));
                }
                System.out.println("User logged in!");
                return true;

            } else {
                System.out.println("User couldn't be logged in!");
                return false;
            }
//            statement.execute(sql);

        } catch (SQLException e){
            System.out.println("User couldn't be logged in!");
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        new DBUtils();
    }
}
