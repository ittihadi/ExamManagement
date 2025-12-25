package exammanagementsystem.ui.login;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

import exammanagementsystem.DatabaseConnection;
import exammanagementsystem.ui.dashboard.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Exam Management System - Login");
        setSize(400, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField username = new JTextField();
        username.setEditable(true);

        JPasswordField password = new JPasswordField();
        password.setEditable(true);

        JButton loginBtn = new JButton("Login");

        formPanel.add(new JLabel("Username"));
        formPanel.add(username);
        formPanel.add(new JLabel("Password"));
        formPanel.add(password);
        formPanel.add(new JLabel());
        formPanel.add(loginBtn);

        JPanel aboutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

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

        loginBtn.addActionListener(event -> {
            // Login with username and password, get role name and switch accoringly
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement user_fetch = conn
                        .prepareStatement("SELECT users.id AS user_id, roles.name AS role_name "
                                + "FROM users INNER JOIN roles ON users.role_id=roles.id "
                                + "WHERE users.id=? AND users.password=?    ");
                user_fetch.setString(1, username.getText());
                user_fetch.setString(2, new String(password.getPassword()));
                System.out.println(user_fetch.toString());
                ResultSet fetch_result = user_fetch.executeQuery();

                if (fetch_result.next()) {
                    dispose();
                    String role = fetch_result.getString("role_name");

                    switch (role) {
                        case "Admin":
                            new AdminDashboard().setVisible(true);
                            break;
                        case "Supervisor":
                            new SupervisorDashboard().setVisible(true);
                            break;
                        case "Participant":
                            new ParticipantDashboard().setVisible(true);
                            break;
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Gagal login");
                }
            } catch (SQLException err) {
                err.printStackTrace();
                return;
            }
        });

        add(formPanel, BorderLayout.CENTER);
        add(aboutPanel, BorderLayout.SOUTH);
    }
}
