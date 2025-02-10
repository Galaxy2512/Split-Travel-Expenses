package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://db4free.net:3306/zavrsni_projekt";
    private static final String USERNAME = "kristina_sep";
    private static final String PASSWORD = "Amoric456!";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return null;
        }
    }
}