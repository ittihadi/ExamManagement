package exammanagementsystem.ui.login;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;
import exammanagementsystem.ui.dashboard.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Exam Management System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JComboBox<String> role = new JComboBox<>(
                new String[]{"Admin", "Exam Supervisor", "Participant"}
        );

        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            dispose();
            switch (role.getSelectedIndex()) {
                case 0 -> new AdminDashboard().setVisible(true);
                case 1 -> new SupervisorDashboard().setVisible(true);
                case 2 -> new ParticipantDashboard().setVisible(true);
            }
        });

        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Password"));
        panel.add(password);
        panel.add(new JLabel("Role"));
        panel.add(role);
        panel.add(new JLabel());
        panel.add(loginBtn);

        add(panel);
    }
}