import View.MainFrame;

import javax.swing.*;

import static Model.DatabaseConnection.connect;

public class App {

    public static void main(String[] args) {

        connect();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
