package exammanagementsystem.ui.dashboard;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import exammanagementsystem.ui.result.*;

public class ParticipantDashboard extends JFrame {

    public ParticipantDashboard() {
        setTitle("Participant Dashboard");
        setSize(900,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Available Exams", new JPanel());
        tabs.add("My Results", new ResultPerParticipantPanel());

        add(tabs);
    }
}