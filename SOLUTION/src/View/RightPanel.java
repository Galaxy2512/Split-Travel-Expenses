package View;

import Controller.RightPanelController;
import Model.ExpencesObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RightPanel class represents the right panel in the application's user interface.
 * It displays expenses, user amounts, and total debts, and allows filtering and clearing of data.
 *
 * @version 1.1
 */
public class RightPanel extends JPanel implements ExpencesObserver, RightPanelListener {
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JTable userAmountTable;
    private DefaultTableModel userAmountTableModel;
    private JTextArea totalDebtsArea;
    private JTextArea userTotalsArea;
    private JButton clearButton;
    private JTextField filterField;
    private JComboBox<String> columnFilterDropdown;
    private TableRowSorter<DefaultTableModel> tableRowSorter;
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
        // Table Model for expenses
        tableModel = new DefaultTableModel(new Object[]{"Title of expense", "Category", "Paid By", "Date", "Amount", "Split Between", "Debts"}, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setPreferredScrollableViewportSize(new Dimension(500, 300));

        // Enable filtering
        filterField = new JTextField(20);
        tableRowSorter = new TableRowSorter<>(tableModel);
        expenseTable.setRowSorter(tableRowSorter);

        // Dropdown for column selection
        columnFilterDropdown = new JComboBox<>(new String[]{"All", "Title of expense", "Category", "Paid By", "Date", "Amount", "Split Between", "Debts"});

        // Table Model for user expenses
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
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(filterField);
        filterPanel.add(new JLabel(" in "));
        filterPanel.add(columnFilterDropdown);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(totalDebtsArea), BorderLayout.CENTER);
        bottomPanel.add(clearButton, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(userAmountTable), BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(userTotalsArea), BorderLayout.SOUTH);

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void activateComponents() {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clearTableData();
            }
        });

        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }
        });
    }

    /**
     * Applies filtering based on the text entered in the filter field and selected column.
     */
    private void applyFilter() {
        String searchText = filterField.getText().trim();
        int selectedColumn = columnFilterDropdown.getSelectedIndex() - 1; // -1 because "All" is at index 0

        if (searchText.isEmpty()) {
            tableRowSorter.setRowFilter(null);
        } else {
            if (selectedColumn == -1) {
                tableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
            } else {
                tableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, selectedColumn));
            }
        }
    }

    public void setController(RightPanelController controller) {
        this.controller = controller;
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

    public void addExpense(String name, String name1, String paidBy, Date date, double amount, List<String> users, String debts) {
        controller.addExpense(name, name1, paidBy, date, amount, users);
        double splitAmount = amount / users.size();
        for (String user : users) {
            userSummaryPanel.addUserExpense(user, splitAmount);
        }
    }
}
