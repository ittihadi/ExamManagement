package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class ExamManagementPanel extends JPanel {

    public ExamManagementPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTable table = new JTable(
            new Object[][]{
                {"E001", "Mid Exam", "2025-06-01 09:00", "2025-06-01 11:00", "Visible"},
                {"E002", "Final Exam", "2025-07-01 09:00", "2025-07-01 12:00", "Hidden"}
            },
            new String[]{
                "Exam ID", "Title", "Start Time", "End Time", "Visibility"
            }
        );

        JPanel form = new JPanel(new GridLayout(7,2,10,10));
        form.setBorder(BorderFactory.createTitledBorder("Exam Details"));

        form.add(new JLabel("Title"));
        form.add(new JTextField());

        form.add(new JLabel("Description"));
        form.add(new JTextArea(3,20));

        form.add(new JLabel("Start Time"));
        form.add(new JTextField("YYYY-MM-DD HH:MM"));

        form.add(new JLabel("End Time"));
        form.add(new JTextField("YYYY-MM-DD HH:MM"));

        form.add(new JLabel("Visibility"));
        form.add(new JComboBox<>(new String[]{"Visible", "Hidden"}));

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Add"));
        buttons.add(new JButton("Update"));
        buttons.add(new JButton("Delete"));

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
        add(buttons, BorderLayout.SOUTH);
    }
}