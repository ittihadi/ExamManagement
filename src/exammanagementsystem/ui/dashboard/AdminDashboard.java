package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;

import exammanagementsystem.ui.admin.UserManagementPanel;
import exammanagementsystem.ui.exam.ExamManagementPanel;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabs:
        // Exams, Users
        // - | -, - L___________________________________. -
        // Select Exam : [____]
        // --------------------
        // Title :
        // Description :
        // Questions, Participants, Supervisors

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Exams", new ExamManagementPanel());
        tabs.add("Users", new UserManagementPanel());
        add(tabs);
    }
}
