package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import exammanagementsystem.ui.exam.ExamSupervisionPanel;

public class SupervisorDashboard extends JFrame {

    public SupervisorDashboard(String user_id) {
        setTitle("Supervisor Dashboard");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new ExamSupervisionPanel(user_id));
    }
}
