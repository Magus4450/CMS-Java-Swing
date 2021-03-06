package DBHelpers;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

import static DBHelpers.DBCRUD.*;

public class DBUtils {

    private static final String url = "jdbc:mysql://localhost:3306/";


    private boolean isDataDumped = false;

    public static final String dbName = "CMS2059521";
    private static Connection connection;
    private static Statement statement;

    public DBUtils(String user, String pass) {
        try {
            connection = DriverManager.getConnection(url, user, pass);

            statement = connection.createStatement();
            JOptionPane.showMessageDialog(null, "Database Connection Successful.", "Database Connected", JOptionPane.INFORMATION_MESSAGE);
            new DBCRUD();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection failed. Make sure the server is online and you have entered correct credentials.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        this.createDatabase();
        this.dumpData();
    }

    public static Connection getDBConnection() {
        return connection;
    }

    private void createDatabase() {
        try {
            statement.execute("CREATE DATABASE " + dbName);

            System.out.println("Database " + dbName + " created successfully!");

            this.createTables();
        } catch (SQLException e) {
            System.out.println("Database already exists!");
            this.isDataDumped = true;
        }

    }


    private void createTables() {

        this.createTeacherTable();
        this.createStudentTable();
        this.createAdminTable();
        this.createCourseTable();
        this.createModuleTable();
    }

    private void createTeacherTable() {
        try {
            String sql = String.format("""
                    CREATE TABLE `%s`.`TEACHER` (
                      `teacherId` INT NOT NULL AUTO_INCREMENT,
                       `username` VARCHAR(45) NULL,
                        `password` VARCHAR(200) NULL,
                        `firstName` VARCHAR(45) NULL,
                        `lastName` VARCHAR(45) NULL,
                        `address` VARCHAR(45) NULL,
                        `contact` VARCHAR(45) NULL,
                        `teacherModules` VARCHAR(200) NULL,
                      PRIMARY KEY (`teacherId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table teacher created successfully");
        } catch (SQLException e) {
            System.out.println("Table teacher couldn't be created");
            e.printStackTrace();
        }
    }

    private void createStudentTable() {
        try {
            String sql = String.format("""
                    CREATE TABLE `%s`.`STUDENT` (
                      `studentId` INT NOT NULL AUTO_INCREMENT,
                      `studentLevel` INT NOT NULL,
                      `studentRemarks` VARCHAR(200) NULL,
                      `username` VARCHAR(45) NULL,
                        `password` VARCHAR(200) NULL,
                        `firstName` VARCHAR(45) NULL,
                        `lastName` VARCHAR(45) NULL,
                        `address` VARCHAR(45) NULL,
                        `contact` VARCHAR(45) NULL,
                        `enrolledCourse` VARCHAR(100) NULL,
                        `passedSem` INT NULL,
                        `marks` VARCHAR(200) NULL,
                        `enrolledModules` VARCHAR(300) NULL,
                      PRIMARY KEY (`studentId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table student created successfully");
        } catch (SQLException e) {
            System.out.println("Table student couldn't be created");
            e.printStackTrace();
        }
    }

    private void createAdminTable() {
        try {
            String sql = String.format("""
                    CREATE TABLE `%s`.`ADMIN` (
                      `adminId` INT NOT NULL AUTO_INCREMENT,
                      `username` VARCHAR(45) NULL,
                      `password` VARCHAR(200) NULL,
                      PRIMARY KEY (`adminId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table admin created successfully");
        } catch (SQLException e) {
            System.out.println("Table admin couldn't be created");
            e.printStackTrace();
        }
    }

    private void createModuleTable() {
        try {
            String sql = String.format("""
                    CREATE TABLE `%s`.`MODULE` (
                      `moduleCode` VARCHAR(10) NOT NULL,
                      `moduleName` VARCHAR(100) NULL,
                      `moduleLevel` INT NULL,
                      `moduleCredit` INT NULL,
                      `isOptional` INT NOT NULL,
                      `moduleSem` INT NOT NULL,
                      `moduleTeacher` VARCHAR(100) NULL,
                      `courseId` VARCHAR(100) NOT NULL,
                      PRIMARY KEY (`moduleCode`));""", dbName);

            statement.execute(sql);
            System.out.println("Table module created successfully");
        } catch (SQLException e) {
            System.out.println("Table module couldn't be created");
            e.printStackTrace();
        }
    }

    private void createCourseTable() {
        try {

            // courseTeachers -> Store id of teachers
            // courseModules -> Store id of modules
            String sql = String.format("""
                    CREATE TABLE `%s`.`COURSE` (
                      `courseId` INT NOT NULL AUTO_INCREMENT,
                      `courseName` VARCHAR(100) NOT NULL,
                      `courseDuration` INT NOT NULL,
                      `courseSemesters` INT NOT NULL,
                      `courseIsAvailable` INT NOT NULL,
                      `courseModules` VARCHAR(200) NOT NULL,
                      PRIMARY KEY (`courseId`));""", dbName);

            statement.execute(sql);
            System.out.println("Table course created successfully");
        } catch (SQLException e) {
            System.out.println("Table course couldn't be created");
            e.printStackTrace();
        }
    }


    private void dumpData() {
        if (!this.isDataDumped) {
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
            while (sc.hasNextLine()) {
                data = sc.nextLine().split(",");
                insertIntoStudent(data);
            }
            System.out.println("Student data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("studentData.csv not Found!");
            e.printStackTrace();
        }
    }

    private void dumpTeacherData() {
        try {
            Scanner sc = new Scanner(new File("src/DummyData/teacherData.csv"));
            String[] data;
            while (sc.hasNextLine()) {
                data = sc.nextLine().split(",");
                insertIntoTeacher(data);
            }
            System.out.println("Teacher data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("teacherData.csv not Found!");
            e.printStackTrace();
        }
    }

    private void dumpAdminData() {
        try {
            Scanner sc = new Scanner(new File("src/DummyData/adminData.csv"));
            String[] data;
            while (sc.hasNextLine()) {
                data = sc.nextLine().split(",");
                insertIntoAdmin(data);
            }
            System.out.println("Admin data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("adminData.csv not Found!");
            e.printStackTrace();
        }

    }

    private void dumpCourseData() {
        try {
            Scanner sc = new Scanner(new File("src/DummyData/courseData.csv"));
            String[] data;
            while (sc.hasNextLine()) {
                data = sc.nextLine().split(",");
                insertIntoCourse(data);
            }
            System.out.println("Course data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("courseData.csv not Found!");
            e.printStackTrace();
        }

    }

    private void dumpModuleData() {
        try {
            Scanner sc = new Scanner(new File("src/DummyData/moduleData.csv"));
            String[] data;
            while (sc.hasNextLine()) {
                data = sc.nextLine().split(",");
                insertIntoModule(data);
            }
            System.out.println("Module data dumped into database successfully!");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("moduleData.csv not Found!");
            e.printStackTrace();
        }
    }
}
