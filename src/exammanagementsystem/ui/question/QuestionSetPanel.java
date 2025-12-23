package exammanagementsystem.ui.question;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class QuestionSetPanel extends JPanel {

    public QuestionSetPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TABLE QUESTION
        JTable questionTable = new JTable(
            new Object[][]{
                {"1", "What is Java?", "Objective", "A"},
                {"2", "Explain OOP concept", "Essay", "-"}
            },
            new String[]{
                "No", "Question Content", "Type", "Correct Answer"
            }
        );

        // FORM QUESTION
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Question Form"));

        formPanel.add(new JLabel("Question Number"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Question Content"));
        formPanel.add(new JTextArea(3, 20));

        formPanel.add(new JLabel("Question Type"));
        formPanel.add(new JComboBox<>(new String[]{"Objective", "Essay"}));

        formPanel.add(new JLabel("Correct Answer"));
        formPanel.add(new JTextField());

        // BUTTONS
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Add"));
        buttonPanel.add(new JButton("Update"));
        buttonPanel.add(new JButton("Delete"));

        add(new JScrollPane(questionTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}