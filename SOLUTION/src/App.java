import View.MainFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog(null, "Enter your username:", "Login", JOptionPane.QUESTION_MESSAGE);

            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must enter a username!", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            new MainFrame(username.trim()); // ✅ Sada prosleđujemo korisničko ime
        });
    }
}
