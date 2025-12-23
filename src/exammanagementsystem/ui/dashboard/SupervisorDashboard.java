package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import exammanagementsystem.ui.exam.*;
import exammanagementsystem.ui.result.*;

public class SupervisorDashboard extends JFrame {

    public SupervisorDashboard() {
        setTitle("Supervisor Dashboard");
        setSize(1100,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("My Exams", new ExamManagementPanel());
        tabs.add("Scoring", new ScoringPanel());
        tabs.add("Score Distribution", new ScoreDistributionPanel());

        add(tabs);
    }
}