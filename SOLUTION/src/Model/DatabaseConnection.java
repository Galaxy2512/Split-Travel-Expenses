package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa DatabaseConnection omogućava spajanje na bazu i proveru administratorskih prava.
 */
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

    /**
     * Proverava da li korisnik ima administratorska prava.
     *
     * @param username korisničko ime
     * @return true ako je korisnik admin, false ako nije
     *
     *
     *
     */
    public static boolean isAdmin(String username) {
        // Lista korisnika koji su administratori
        List<String> adminUsers = Arrays.asList("Kristina");

        // Proveravamo da li se korisnik nalazi na listi admina
        if (adminUsers.contains(username)) {
            System.out.println("User " + username + " is an admin.");
            return true;
        }

        System.out.println("User " + username + " is NOT an admin.");
        return false;
    }



}
