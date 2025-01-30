package View;

import Controller.Controller;
import Controller.DataHandle;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    private Controller controller;
    private DataHandle dataHandle;

    public MenuBar() {
        this.dataHandle = new DataHandle();

        // File menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem exportItem = new JMenuItem("Export Data...");
        JMenuItem importItem = new JMenuItem("Import Data...");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(exportItem);
        fileMenu.add(importItem);

        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Adding menus to the menu bar
        add(fileMenu);

        // Delete Expense Action
        JMenuItem deleteAllItem = new JMenuItem("Delete ALL Data");
        fileMenu.add(deleteAllItem);

        deleteAllItem.addActionListener(e -> {
            if (controller != null) {
                dataHandle.handleDeleteAllData();
            }
        });

        // File menu actions
        exitItem.addActionListener(e -> {
            if (controller != null) {
                controller.handleExit();
            }
        });

        exportItem.addActionListener(e -> {
            if (controller != null) {
                dataHandle.exportData();
            }
        });

        importItem.addActionListener(e -> {
            if (controller != null) {
                dataHandle.handleImport();
            }
        });
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.dataHandle.setController(controller);
    }

    public void setMenuBarListener(MenuBarListener listener) {
        this.dataHandle.setMenuBarListener(listener);
    }
}