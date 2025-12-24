package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import exammanagementsystem.ui.exam.ExamManagementPanel;
import exammanagementsystem.ui.question.QuestionSetPanel;
import exammanagementsystem.ui.result.ScoreDistributionPanel;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Exams", new ExamManagementPanel());
        tabs.add("Questions", new QuestionSetPanel());
        add(tabs);
    }
}