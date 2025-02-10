// Java
package View;

import Controller.Controller;
import Controller.DataHandle;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    private Controller controller;
    private DataHandle dataHandle;

    public MenuBar() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadDataItem = new JMenuItem("Load Data");
        JMenuItem exportItem = new JMenuItem("Export Data...");
        JMenuItem importItem = new JMenuItem("Import Data...");
        JMenuItem deleteAllItem = new JMenuItem("Delete ALL Data");
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
        deleteAllItem.addActionListener(e -> controller.deleteAllData());

        exportItem.addActionListener(e -> dataHandle.exportData());
        importItem.addActionListener(e -> dataHandle.handleImport());

        exitItem.addActionListener(e -> System.exit(0));
    }

    public void setController(Controller controller) {
        this.controller = controller;
        dataHandle.setController(controller);
    }
}