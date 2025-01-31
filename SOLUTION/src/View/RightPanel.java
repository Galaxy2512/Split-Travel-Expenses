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

public class RightPanel extends JPanel implements ExpencesObserver, RightPanelListener {
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JTextArea totalDebtsArea;
    private Map<String, Double> totalDebts;
    private RightPanelController controller;
    private JButton clearButton;

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

        totalDebtsArea = new JTextArea();
        totalDebtsArea.setEditable(false);
        totalDebtsArea.setPreferredSize(new Dimension(500, 100));

        clearButton = new JButton("Clear");
    }

    private void layoutComponents() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(totalDebtsArea), BorderLayout.CENTER);
        bottomPanel.add(clearButton, BorderLayout.SOUTH);

        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
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

    public void setController(RightPanelController controller) {
        this.controller = controller;
    }

    public void addExpense(String name, String category, String paidBy, Date date, double amount, List<String> users) {
        controller.addExpense(name, category, paidBy, date, amount, users);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTotalDebtsAreaText(String text) {
        totalDebtsArea.setText(text);
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
        // Handle the event (if needed)
    }
}