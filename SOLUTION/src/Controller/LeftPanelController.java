package Controller;

import View.LeftPanel;
import View.LeftPanelEvent;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeftPanelController {
    private LeftPanel leftPanel;
    private Controller controller;

    public LeftPanelController(LeftPanel leftPanel, Controller controller) {
        this.leftPanel = leftPanel;
        this.controller = controller;
        this.leftPanel.setController(this);
    }

    public void processLeftPanelEvent() {
        String name = leftPanel.getNameField().getText();
        String category = (String) leftPanel.getCategoryField().getSelectedItem();
        String paidBy = (String) leftPanel.getPaidByField().getSelectedItem();
        Date date = (Date) leftPanel.getDateField().getValue();
        double amount;

        if (name.isEmpty() || category == null || category.trim().isEmpty() || paidBy == null || date == null) {
            JOptionPane.showMessageDialog(leftPanel, "Please fill in all fields before sending data.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (leftPanel.getAmountField().getText().isEmpty()) {
            JOptionPane.showMessageDialog(leftPanel, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            amount = Double.parseDouble(leftPanel.getAmountField().getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(leftPanel, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int rowCount = leftPanel.getTableModel().getRowCount();
        List<String> eventUsers = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            eventUsers.add((String) leftPanel.getTableModel().getValueAt(i, 0));
        }

        LeftPanelEvent event = new LeftPanelEvent(leftPanel, name, category, paidBy, date, amount, eventUsers);
        controller.processLeftPanelEvent(event);
    }
}