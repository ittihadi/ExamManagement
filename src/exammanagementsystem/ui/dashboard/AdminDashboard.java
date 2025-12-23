package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import exammanagementsystem.ui.exam.*;
import exammanagementsystem.ui.question.*;
import exammanagementsystem.ui.result.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1100,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Exams", new ExamManagementPanel());
        tabs.add("Questions", new QuestionSetPanel());
        tabs.add("Participants", new ParticipantListPanel());
        tabs.add("Supervisors", new SupervisorListPanel());
        tabs.add("Score Distribution", new ScoreDistributionPanel());

        add(tabs);
    }
}