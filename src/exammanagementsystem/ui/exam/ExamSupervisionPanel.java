package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exammanagementsystem.dao.ExamDAO;
import exammanagementsystem.dao.ExamDAO.Exam;
import exammanagementsystem.dao.ResultDAO;
import exammanagementsystem.dao.ResultDAO.Result;
import exammanagementsystem.ui.result.ScoringPanel;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ExamSupervisionPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<Exam> cbSelectExam;
    private JTextField txtQuestionNo;
    private JTextField txtParticipant;
    private JTextField txtScore;

    private final String supervisor_id;
    private final ExamDAO exam_dao;
    private final ResultDAO result_dao;

    public ExamSupervisionPanel(String user_id) {
        supervisor_id = user_id;
        exam_dao = new ExamDAO();
        result_dao = new ResultDAO();

        setLayout(new BorderLayout(10, 10));
        initHeader();
        initTable();
        initForm();
    }


    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbSelectExam = new JComboBox<>();

        try {
            List<Exam> exams = exam_dao.readBySupervisor(supervisor_id);
            for (Exam e : exams) {
                cbSelectExam.addItem(e);
            }
        } catch (Exception e) {
            showError(e);
        }

        cbSelectExam.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                if (value instanceof Exam exam) {
                    value = exam.getTitle();
                }
                return super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            }
        });

        cbSelectExam.addActionListener(e -> loadResponses());

 
        JButton btnScoring = new JButton("Open Scoring");
        btnScoring.addActionListener(e -> openScoring());

        header.add(new JLabel("Select Exam:"));
        header.add(cbSelectExam);
        header.add(btnScoring); // 🔥

        add(header, BorderLayout.NORTH);
    }



    private void initTable() {
        model = new DefaultTableModel(
                new Object[]{
                        "Question No",
                        "Participant ID",
                        "Answer",
                        "Score"
                }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel()
             .addListSelectionListener(e -> loadSelectedResponse());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtQuestionNo = new JTextField(10);
        txtQuestionNo.setEditable(false);

        txtParticipant = new JTextField(10);
        txtParticipant.setEditable(false);

        txtScore = new JTextField(5);

        JButton btnUpdate = new JButton("Update Score");
        btnUpdate.addActionListener(e -> updateScore());

        int y = 0;
        c.gridx = 0; c.gridy = y;
        form.add(new JLabel("Question No"), c);
        c.gridx = 1;
        form.add(txtQuestionNo, c);

        y++;
        c.gridx = 0; c.gridy = y;
        form.add(new JLabel("Participant"), c);
        c.gridx = 1;
        form.add(txtParticipant, c);

        y++;
        c.gridx = 0; c.gridy = y;
        form.add(new JLabel("Score"), c);
        c.gridx = 1;
        form.add(txtScore, c);

        y++;
        c.gridx = 1; c.gridy = y;
        form.add(btnUpdate, c);

        add(form, BorderLayout.EAST);
    }

    private void loadResponses() {
        model.setRowCount(0);

        Exam selected = (Exam) cbSelectExam.getSelectedItem();
        if (selected == null) return;

        try {
            List<Result> results = result_dao.readByExam(selected.getId());

            for (Result r : results) {
                model.addRow(new Object[]{
                        r.getNumber(),
                        r.getUser_id(),
                        r.getAnswer(),
                        r.getScore()
                });
            }

        } catch (SQLException e) {
            showError(e);
        }
    }
    
    private void openScoring() {
        Exam exam = (Exam) cbSelectExam.getSelectedItem();
        if (exam == null) {
            JOptionPane.showMessageDialog(this, "Please select exam first");
            return;
        }

        JFrame frame = new JFrame("Scoring - " + exam.getTitle());
        ScoringPanel scoringPanel = new ScoringPanel();
        scoringPanel.setExamContext(exam.getId());

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scoringPanel);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }


    private void loadSelectedResponse() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        txtQuestionNo.setText(model.getValueAt(row, 0).toString());
        txtParticipant.setText(model.getValueAt(row, 1).toString());
        txtScore.setText(model.getValueAt(row, 3).toString());
    }

    private void updateScore() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        Exam exam = (Exam) cbSelectExam.getSelectedItem();
        if (exam == null) return;

        try {
            int questionNo = Integer.parseInt(txtQuestionNo.getText());
            String userId = txtParticipant.getText();
            float score = Float.parseFloat(txtScore.getText());

            Result updated = new Result(
                    exam.getId(),
                    userId,
                    questionNo,
                    model.getValueAt(row, 2).toString(),
                    score
            );

            result_dao.update(updated);

            model.setValueAt(score, row, 3);
            JOptionPane.showMessageDialog(this, "Score updated");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid score");
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
    }
}

