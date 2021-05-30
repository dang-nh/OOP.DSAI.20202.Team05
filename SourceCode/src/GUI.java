import Constant.Constant;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame {
    private GUI() {
        // Set the title for this frame.
        this.setTitle("O An Quan");

        // Set size of the frame.
        this.setSize(Constant.frameWidth, Constant.frameHeight);
        // Put frame to center of the screen.
        this.setLocationRelativeTo(null);
        // So that frame cannot be resizable by the user.
        this.setResizable(false);

        // Exit the application when user close frame.
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                int resp = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                        "Exit?", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        this.setContentPane(new MainGame());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(GUI::new);
    }
}
