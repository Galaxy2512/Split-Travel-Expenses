package Model;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseConnection upravlja radom s MySQL bazom podataka.
 *
 * @version 2.5
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://db4free.net:3306/zavrsni_projekt";
    private static final String USERNAME = "kristina_sep";
    private static final String PASSWORD = "Amoric456!";

    /**
     * Stvara novu konekciju s bazom podataka.
     *
     * @return Konekcija s bazom
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return null;
        }
    }



    /**
     * Dohvaća sve korisnike iz baze.
     *
     * @return Lista korisničkih imena
     */
    public static List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT name FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Dohvaća sve troškove iz baze.
     *
     * @return ResultSet sa svim troškovima
     */
    public static ResultSet getAllExpenses() {
        String sql = "SELECT e.id, e.name, e.category, u.name AS paid_by, e.date, e.amount " +
                "FROM expenses e JOIN users u ON e.paid_by = u.id";
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Spremanje troška u bazu.
     */
    public static void saveExpenseToDatabase(String name, String category, String paidBy, Date date, double amount, List<String> users) {
        int paidById = addUser(paidBy);
        if (paidById == -1) return;

        String expenseSql = "INSERT INTO expenses (name, category, paid_by, date, amount) VALUES (?, ?, ?, ?, ?)";
        String expenseUserSql = "INSERT INTO expense_users (expense_id, user_id) VALUES (?, ?)";
        String debtsSql = "INSERT INTO debts (debtor_id, creditor_id, expense_id, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement expenseStmt = conn.prepareStatement(expenseSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement expenseUserStmt = conn.prepareStatement(expenseUserSql);
             PreparedStatement debtsStmt = conn.prepareStatement(debtsSql)) {

            conn.setAutoCommit(false);

            expenseStmt.setString(1, name);
            expenseStmt.setString(2, category);
            expenseStmt.setInt(3, paidById);
            expenseStmt.setDate(4, date);
            expenseStmt.setDouble(5, amount);
            expenseStmt.executeUpdate();

            ResultSet generatedKeys = expenseStmt.getGeneratedKeys();
            if (!generatedKeys.next()) return;
            int expenseId = generatedKeys.getInt(1);

            double share = amount / users.size();
            for (String user : users) {
                int userId = addUser(user);
                expenseUserStmt.setInt(1, expenseId);
                expenseUserStmt.setInt(2, userId);
                expenseUserStmt.executeUpdate();

                if (userId != paidById) {
                    debtsStmt.setInt(1, userId);
                    debtsStmt.setInt(2, paidById);
                    debtsStmt.setInt(3, expenseId);
                    debtsStmt.setDouble(4, share);
                    debtsStmt.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Expense saved to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllData() {
        String sqlDeleteDebts = "DELETE FROM debts";
        String sqlDeleteExpenseUsers = "DELETE FROM expense_users";
        String sqlDeleteExpenses = "DELETE FROM expenses";
        String sqlDeleteUsers = "DELETE FROM users";
        String sqlResetAutoIncrementExpenses = "ALTER TABLE expenses AUTO_INCREMENT = 1";
        String sqlResetAutoIncrementUsers = "ALTER TABLE users AUTO_INCREMENT = 1";
        String sqlResetAutoIncrementDebts = "ALTER TABLE debts AUTO_INCREMENT = 1";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Prvo brišemo dugove
            stmt.executeUpdate(sqlDeleteDebts);
            // Zatim brišemo povezane korisnike za troškove
            stmt.executeUpdate(sqlDeleteExpenseUsers);
            // Brišemo sve troškove
            stmt.executeUpdate(sqlDeleteExpenses);
            // Brišemo sve korisnike
            stmt.executeUpdate(sqlDeleteUsers);

            // Resetiramo auto-increment vrijednosti
            stmt.executeUpdate(sqlResetAutoIncrementExpenses);
            stmt.executeUpdate(sqlResetAutoIncrementUsers);
            stmt.executeUpdate(sqlResetAutoIncrementDebts);

            System.out.println("All data deleted and auto-increment values reset successfully.");
            JOptionPane.showMessageDialog(null, "All data has been deleted from the database and auto-increment values reset.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting data from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Dodaje korisnika u bazu ako ne postoji.
     *
     * @param name Ime korisnika
     * @return ID korisnika
     */
    public static int addUser(String name) {
        String checkSql = "SELECT id FROM users WHERE name = ?";
        String insertSql = "INSERT INTO users (name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

            insertStmt.setString(1, name);
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<String> getUsersForExpense(int expenseId) {
        List<String> users = new ArrayList<>();
        String sql = "SELECT u.name FROM expense_users eu JOIN users u ON eu.user_id = u.id WHERE eu.expense_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expenseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}