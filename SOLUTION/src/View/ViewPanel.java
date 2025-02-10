package View;

import Controller.LeftPanelController;
import Controller.RightPanelController;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa ViewPanel predstavlja panel koji sadrzi sve panele.
 * Panel se sastoji od LeftPanel i RightPanel.
 * Metoda setController postavlja kontroler.
 * Metode getLeftPanel i getRightPanel vracaju LeftPanel i RightPanel.
 *
 * @see LeftPanel
 * @see RightPanel
 *
 * @version 1.0
 *
 */

public class ViewPanel extends JPanel {
    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    public ViewPanel() {
        setLayout(new BorderLayout());
        rightPanel = new RightPanel();
        leftPanel = new LeftPanel((RightPanelListener) rightPanel);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public void setController(LeftPanelController leftPanelController, RightPanelController rightPanelController) {
        leftPanel.setController(leftPanelController);
        rightPanel.setController(rightPanelController);
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }
}