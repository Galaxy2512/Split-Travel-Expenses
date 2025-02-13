package View;

import Controller.Controller;
import Controller.DataHandle;
import Model.DatabaseConnection;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    private Controller controller;
    private DataHandle dataHandle;
    private String loggedInUser; // Čuvamo trenutno ulogovanog korisnika

    public MenuBar() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadDataItem = new JMenuItem("Load Data from database");
        JMenuItem exportItem = new JMenuItem("Export Data...");
        JMenuItem importItem = new JMenuItem("Import Data...");
        JMenuItem deleteAllItem = new JMenuItem("Delete ALL Data from Database");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(loadDataItem);
        fileMenu.add(exportItem);
        fileMenu.add(importItem);
        fileMenu.addSeparator();
        fileMenu.add(deleteAllItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        add(fileMenu);

        dataHandle = new DataHandle();

        loadDataItem.addActionListener(e -> controller.loadDataFromDatabase());
        exportItem.addActionListener(e -> dataHandle.exportData());
        importItem.addActionListener(e -> dataHandle.handleImport());





        // 🔹 Da li je korisnik admin prije nego što dozvolimo brisanje
        deleteAllItem.addActionListener(e -> {
            if (loggedInUser == null) {
                JOptionPane.showMessageDialog(null, "Error: No user logged in.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //provjera jel korisnik administrator

            boolean isAdmin = DatabaseConnection.isAdmin(loggedInUser);
            if (isAdmin) {
                controller.deleteAllData();
                JOptionPane.showMessageDialog(null, "All data has been deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "You do not have permission to delete all data.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        exitItem.addActionListener(e -> System.exit(0));
    }

    /**
     * Postavlja trenutno ulogiranogkorisnika.
     *
     * @param username korisničko ime
     */
    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        dataHandle.setController(controller);
    }
}
