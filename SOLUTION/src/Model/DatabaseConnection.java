package Model;

/**
 * DatabaseConnection klasa sadrzi metode za povezivanje i prekidanje veze s bazom podataka.
 * DatabaseConnection klasa takodje sadrzi metode za brisanje svih podataka iz baze podataka i za cuvanje troskova u bazi podataka.
 *
 * @version 1.0
 *
 * Metode:
 * - connect() : Connection - povezuje se s bazom podataka
 * - disconnect(Connection connection) : void - prekida vezu s bazom podataka
 * - getConnection() : Connection - vraca vezu s bazom podataka
 * - isConnected() : boolean - proverava da li je veza s bazom podataka uspostavljena
 * - deleteAllData() : void - brise sve podatke iz baze podataka i resetuje auto-increment vrednosti
 * - saveExpenseToDatabase(Expense expense) : void - cuva trosak u bazi podataka
 *
 * @see Expense
 *
 */

import javax.swing.*;
import java.sql.*;


public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://db4free.net:3306/zavrsni_projekt";
    private static final String USERNAME = "kristina_sep";
    private static final String PASSWORD = "Amoric456!";

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection successful!");
            return connection;
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
        return null;
    }

//    public static void disconnect(Connection connection) {
//        if (connection != null) {
//            try {
//                connection.close();
//                System.out.println("Disconnected from DB...");
//            } catch (SQLException e) {
//                System.err.println("SQL Exception: " + e.getMessage());
//            }
//        }
//    }

    public static Connection getConnection() {
        return connect();
    }

//    public static boolean isConnected() {
//        try (Connection conn = getConnection()) {
//            return conn != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    /**
     * Deletes all data from the database and resets auto-increment values.
     */
    public static void deleteAllData() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            if (conn != null) {
                stmt.executeUpdate("DELETE FROM expenses");

                stmt.executeUpdate("ALTER TABLE expenses AUTO_INCREMENT = 1");

                System.out.println("All data deleted and auto-increment values reset.");
                JOptionPane.showMessageDialog(null, "Database cleared successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting data.");
        }
    }

    /**
     * Saves an expense to the database.
     */
    public static void saveExpenseToDatabase(Expense expense) {
        String expenseInsertSql = "INSERT INTO expenses (title, category, paid_by, date, amount, split_between, debts) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement expenseInsertStmt = conn.prepareStatement(expenseInsertSql)) {

            // 1. Dodaj trošak u bazu
            expenseInsertStmt.setString(1, expense.getName());
            expenseInsertStmt.setString(2, expense.getCategory().name()); // Convert enum to String
            expenseInsertStmt.setString(3, expense.getPaidBy());
            expenseInsertStmt.setDate(4, new Date(expense.getDate().getTime()));
            expenseInsertStmt.setDouble(5, expense.getAmount());
            expenseInsertStmt.setString(6, String.join(", ", expense.getSplitBetween())); // Spajanje imena korisnika
            expenseInsertStmt.setString(7, expense.getDebts());

            expenseInsertStmt.executeUpdate();

            System.out.println("Expense saved to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}