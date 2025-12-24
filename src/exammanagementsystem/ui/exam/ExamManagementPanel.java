package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

import exammanagementsystem.ui.question.QuestionSetPanel;
import exammanagementsystem.ui.result.ScoringPanel;
import exammanagementsystem.ui.result.ScoreDistributionPanel;
import exammanagementsystem.ui.exam.VisibilityPanel;

public class ExamManagementPanel extends JPanel {

    private String selectedExamId;
    private ViewMode viewMode = ViewMode.ALL;

    private JComboBox<String> examComboBox;
    private JToggleButton modeToggle;

    private QuestionSetPanel questionPanel;
    private JPanel participantPanel;
    private JPanel supervisorPanel;
    private ScoringPanel scoringPanel;
    private ScoreDistributionPanel distributionPanel;
    private VisibilityPanel visibilityPanel;

    public ExamManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        initHeader();
        initTabs();
    }

    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        examComboBox = new JComboBox<>(new String[]{
            "ALL EXAMS", "E001 - Mid Exam", "E002 - Final Exam"
        });

        modeToggle = new JToggleButton("ALL");
        modeToggle.addActionListener(e -> toggleMode());
        examComboBox.addActionListener(e -> onExamChanged());

        header.add(new JLabel("Exam:"));
        header.add(examComboBox);
        header.add(Box.createHorizontalStrut(20));
        header.add(new JLabel("Mode:"));
        header.add(modeToggle);

        add(header, BorderLayout.NORTH);
    }

    private void initTabs() {
        JTabbedPane tabs = new JTabbedPane();

        questionPanel = new QuestionSetPanel();

        participantPanel = simpleLabelPanel("Participants");
        supervisorPanel = simpleLabelPanel("Supervisors");
        visibilityPanel = new VisibilityPanel();

        scoringPanel = new ScoringPanel();
        distributionPanel = new ScoreDistributionPanel();

        tabs.add("Questions", questionPanel);
        tabs.add("Participants", participantPanel);
        tabs.add("Supervisors", supervisorPanel);
        tabs.add("Scoring", scoringPanel);
        tabs.add("Score Distribution", distributionPanel);
        tabs.add("Visibility", visibilityPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void toggleMode() {
        viewMode = modeToggle.isSelected() ? ViewMode.BY_EXAM : ViewMode.ALL;
        modeToggle.setText(viewMode == ViewMode.ALL ? "ALL" : "BY_EXAM");
        refreshContext();
    }

    private void onExamChanged() {
        if (examComboBox.getSelectedIndex() == 0) {
            selectedExamId = null;
            viewMode = ViewMode.ALL;
            modeToggle.setSelected(false);
            modeToggle.setText("ALL");
        } else {
            selectedExamId = examComboBox.getSelectedItem().toString().split(" ")[0];
            viewMode = ViewMode.BY_EXAM;
            modeToggle.setSelected(true);
            modeToggle.setText("BY_EXAM");
        }
        refreshContext();
    }

    private void refreshContext() {
        questionPanel.setViewMode(viewMode);
        questionPanel.setExamContext(selectedExamId);
        visibilityPanel.setExamContext(selectedExamId);

        updateSimplePanel(participantPanel, "Participants");
        updateSimplePanel(supervisorPanel, "Supervisors");

        scoringPanel.setExamContext(selectedExamId);
        distributionPanel.setExamContext(selectedExamId);
    }

    private JPanel simpleLabelPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void updateSimplePanel(JPanel panel, String baseText) {
        JLabel label = (JLabel) panel.getComponent(0);
        label.setText(
            viewMode == ViewMode.ALL
                ? "All " + baseText
                : baseText + " for Exam: " + selectedExamId
        );
    }

    public enum ViewMode {
        ALL, BY_EXAM
    }
}