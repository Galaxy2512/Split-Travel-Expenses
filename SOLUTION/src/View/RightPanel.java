package View;

import Controller.RightPanelController;
import Model.ExpencesObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RightPanel class represents the right panel in the application's user interface.
 * It displays expenses, user amounts, and total debts, and allows clearing of data.
 *
 * @version 1.0
 */
public class RightPanel extends JPanel implements ExpencesObserver, RightPanelListener {
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JTable userAmountTable;
    private DefaultTableModel userAmountTableModel;
    private JTextArea totalDebtsArea;
    private JTextArea userTotalsArea;
    private JButton clearButton;
    private Map<String, Double> totalDebts;
    private RightPanelController controller;
    public UserSummaryPanel userSummaryPanel;

    /**
     * Constructs a new RightPanel and initializes its components.
     */
    public RightPanel() {
        setLayout(new BorderLayout());
        totalDebts = new HashMap<>();
        initializeComponents();
        layoutComponents();
        activateComponents();
    }

    private void initializeComponents() {
        tableModel = new DefaultTableModel(new Object[]{"Title of expense", "Category", "Paid By", "Date", "Amount", "Split Between", "Debts"}, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setPreferredScrollableViewportSize(new Dimension(500, 300));

        userAmountTableModel = new DefaultTableModel(new Object[]{"User", "Amount"}, 0);
        userAmountTable = new JTable(userAmountTableModel);
        userAmountTable.setPreferredScrollableViewportSize(new Dimension(500, 150));

        totalDebtsArea = new JTextArea();
        totalDebtsArea.setEditable(false);
        totalDebtsArea.setPreferredSize(new Dimension(500, 100));

        userTotalsArea = new JTextArea();
        userTotalsArea.setEditable(false);
        userTotalsArea.setPreferredSize(new Dimension(500, 200));

        clearButton = new JButton("Clear");

        // Initialize userSummaryPanel
        userSummaryPanel = new UserSummaryPanel();
    }

    private void layoutComponents() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(totalDebtsArea), BorderLayout.CENTER);
        bottomPanel.add(clearButton, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(userAmountTable), BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(userTotalsArea), BorderLayout.SOUTH);

        add(new JScrollPane(expenseTable), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void activateComponents() {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clearTableData();
            }
        });
    }

    /**
     * Sets the controller for the right panel.
     *
     * @param controller the controller to set
     */
    public void setController(RightPanelController controller) {
        this.controller = controller;
    }

    /**
     * Adds an expense to the panel.
     *
     * @param name the name of the expense
     * @param category the category of the expense
     * @param paidBy the person who paid for the expense
     * @param date the date of the expense
     * @param amount the amount of the expense
     * @param users the users involved in the expense
     * @param debts the debts associated with the expense
     */
    public void addExpense(String name, String category, String paidBy, Date date, double amount, List<String> users, String debts) {
        controller.addExpense(name, category, paidBy, date, amount, users);

        double splitAmount = amount / users.size();

        for (String user : users) {
            userSummaryPanel.addUserExpense(user, splitAmount);
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTotalDebtsAreaText(String text) {
        totalDebtsArea.setText(text);
    }

    public void setUserTotalsAreaText(String text) {
        userTotalsArea.setText(text);
    }

    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }

    @Override
    public void updateExpenses() {
        controller.updateTotalDebtsDisplay();
    }

    @Override
    public void rightPanelEventOccurred(RightPanelEvent event) {
        // Handle right panel events if needed
    }

    public DefaultTableModel getUserAmountTableModel() {
        return userAmountTableModel;
    }
}