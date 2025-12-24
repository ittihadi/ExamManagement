package exammanagementsystem.ui.login;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;
import exammanagementsystem.ui.dashboard.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Exam Management System - Login");
        setSize(400,260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3,2,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        formPanel.add(new JLabel("Username"));
        formPanel.add(username);
        formPanel.add(new JLabel("Password"));
        formPanel.add(password);
        formPanel.add(new JLabel());
        formPanel.add(loginBtn);

        JPanel aboutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        JLabel aboutLabel = new JLabel("<HTML><U>About</U></HTML>");
        aboutLabel.setForeground(Color.BLUE);
        aboutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        aboutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AboutDialog(LoginFrame.this).setVisible(true);
            }
        });

        aboutPanel.add(aboutLabel);

        loginBtn.addActionListener(e -> {
            dispose();
            // nanti diganti hasil auth DB
            new AdminDashboard().setVisible(true);
        });

        add(formPanel, BorderLayout.CENTER);
        add(aboutPanel, BorderLayout.SOUTH);
    }
}