package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The UserSummaryPanel class represents a panel that displays a summary of user expenses.
 * It shows the total expenses for each user in a table.
 *
 * @version 1.0
 */
public class UserSummaryPanel extends JPanel {
    private JTable userSummaryTable;
    private DefaultTableModel tableModel;
    private Map<String, Double> userExpenses;

    /**
     * Constructs a new UserSummaryPanel and initializes its components.
     */
    public UserSummaryPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"User", "Total Expenses"}, 0);
        userSummaryTable = new JTable(tableModel);
        add(new JScrollPane(userSummaryTable), BorderLayout.CENTER);
        userExpenses = new HashMap<>();
    }

    /**
     * Adds an expense for a user and updates the table.
     *
     * @param user the user to add the expense for
     * @param amount the amount of the expense
     */
    public void addUserExpense(String user, double amount) {
        userExpenses.put(user, userExpenses.getOrDefault(user, 0.0) + amount);
        refreshTable();
    }

    /**
     * Refreshes the table to display the current user expenses.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Double> entry : userExpenses.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    /**
     * Clears all user expenses and updates the table.
     */
    public void clearUserExpenses() {
        userExpenses.clear();
        refreshTable();
    }
}