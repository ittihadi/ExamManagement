package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import exammanagementsystem.ui.result.*;
import exammanagementsystem.ui.exam.AvailableExamPanel;

public class ParticipantDashboard extends JFrame {

    public ParticipantDashboard() {
        setTitle("Participant Dashboard");
        setSize(900,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Available Exams", new AvailableExamPanel());
        tabs.add("My Results", new ResultPerParticipantPanel());

        add(tabs);
    }
}