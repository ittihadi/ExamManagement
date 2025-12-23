package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class ScoreDistributionPanel extends JPanel {

    public ScoreDistributionPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Score Distribution", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JTable distributionTable = new JTable(
            new Object[][]{
                {"Alice", 85, "Pass"},
                {"Bob", 60, "Pass"},
                {"Charlie", 40, "Fail"}
            },
            new String[]{
                "Participant", "Final Score", "Status"
            }
        );

        JPanel summaryPanel = new JPanel(new GridLayout(1,3,10,10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

        summaryPanel.add(new JLabel("Average: 61"));
        summaryPanel.add(new JLabel("Highest: 85"));
        summaryPanel.add(new JLabel("Lowest: 40"));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(distributionTable), BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }
}