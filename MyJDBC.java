

import java.sql.*;

public class MyJDBC {

    public static void main(String[] args) {

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "Poophead009");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM people");

            while (resultSet.next()){
                System.out.println(resultSet.getString("firstname"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

    }
}
