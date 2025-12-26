package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exammanagementsystem.dao.ExamDAO;
import exammanagementsystem.dao.QuestionDAO;
import exammanagementsystem.dao.QuestionDAO.Question;
import exammanagementsystem.dao.ExamDAO.Exam;
import exammanagementsystem.dao.ResultDAO;
import exammanagementsystem.dao.UserDAO;
import exammanagementsystem.dao.UserDAO.User;
import exammanagementsystem.ui.result.ScoringPanel;
import exammanagementsystem.dao.ResultDAO.Result;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ExamSupervisionPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<Exam> cbSelectExam;

    private JComboBox<Integer> cbSelectNumber;
    private JComboBox<String> cbSelectParticipant;
    private JTextArea txtQuestionContent;
    private JTextField txtQuestionCorrect;

    private JTextField txtAnswer;
    private JTextField txtScore;

    private boolean isEditing;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    private final String supervisor_id;
    private final ExamDAO exam_dao;
    private final QuestionDAO question_dao;
    private final ResultDAO result_dao;
    private final UserDAO user_dao;

    public ExamSupervisionPanel(String user_id) {
        supervisor_id = user_id;
        exam_dao = new ExamDAO();
        question_dao = new QuestionDAO();
        result_dao = new ResultDAO();
        user_dao = new UserDAO();

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
        cbSelectExam.setSelectedIndex(-1);

        cbSelectExam.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                if (value instanceof Exam exam && value != null) {
                    value = exam.getTitle();
                } else {
                    value = "- SELECT EXAM -";
                }
                return super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
            }
        });

        cbSelectExam.addActionListener(e -> loadResponses());

        JButton btnScoring = new JButton("Scoring Overview");
        btnScoring.addActionListener(e -> openScoring());

        header.add(new JLabel("Select Exam:"));
        header.add(cbSelectExam);
        header.add(btnScoring);

        add(header, BorderLayout.NORTH);
    }

    private void initTable() {
        model = new DefaultTableModel(
                new Object[] {
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

        cbSelectNumber = new JComboBox<>();
        cbSelectParticipant = new JComboBox<>();

        cbSelectNumber.addActionListener(e -> updateSelected());
        cbSelectParticipant.addActionListener(e -> updateSelected());

        txtQuestionContent = new JTextArea(6, 20);
        txtQuestionContent.setEditable(false);
        txtQuestionCorrect = new JTextField(15);
        txtQuestionCorrect.setEditable(false);

        txtAnswer = new JTextField(20);
        txtScore = new JTextField(5);

        btnAdd = new JButton("Add Answer");
        btnAdd.addActionListener(e -> addScore());

        btnUpdate = new JButton("Update Answer");
        btnUpdate.addActionListener(e -> updateScore());

        btnDelete = new JButton("Delete Answer");
        btnDelete.addActionListener(e -> deleteScore());

        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearForm());

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Question No"), c);
        c.gridx = 1;
        form.add(cbSelectNumber, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Participant"), c);
        c.gridx = 1;
        form.add(cbSelectParticipant, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Question"), c);
        c.gridx = 1;
        form.add(txtQuestionContent, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Correct Answer"), c);
        c.gridx = 1;
        form.add(txtQuestionCorrect, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JSeparator(), c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Answer"), c);
        c.gridx = 1;
        form.add(txtAnswer, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Score (0-10)"), c);
        c.gridx = 1;
        form.add(txtScore, c);

        y++;
        c.gridx = 1;
        c.gridy = y;
        form.add(btnAdd, c);

        y++;
        c.gridx = 1;
        c.gridy = y;
        form.add(btnUpdate, c);

        y++;
        c.gridx = 1;
        c.gridy = y;
        form.add(btnDelete, c);

        y++;
        c.gridx = 1;
        c.gridy = y;
        form.add(btnClear, c);

        updateButtons();

        add(form, BorderLayout.EAST);
    }

    private void loadResponses() {
        model.setRowCount(0);

        Exam selected = (Exam) cbSelectExam.getSelectedItem();
        if (selected == null)
            return;

        try {
            List<Result> results = result_dao.readByExam(selected.getId());
            List<Question> questions = question_dao.readByExam(selected.getId());
            List<User> participants = user_dao.readParticipantsByExamId(selected.getId());

            for (Result r : results) {
                model.addRow(new Object[] {
                        r.getNumber(),
                        r.getUser_id(),
                        r.getAnswer(),
                        r.getScore()
                });
            }

            cbSelectNumber.removeAll();
            cbSelectParticipant.removeAll();

            for (Question qs : questions) {
                cbSelectNumber.addItem(qs.getNumber());
            }

            for (User us : participants) {
                cbSelectParticipant.addItem(us.getId());
            }

            clearForm();

        } catch (SQLException e) {
            showError(e);
        }
    }

    private void openScoring() {
        Exam exam = (Exam) cbSelectExam.getSelectedItem();
        if (exam == null) {
            JOptionPane.showMessageDialog(this, "Please select an exam");
            return;
        }

        JFrame frame = new JFrame("Scoring Overview - " + exam.getTitle());
        ScoringPanel scoringPanel = new ScoringPanel(exam.getId());

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scoringPanel);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    private void loadSelectedResponse() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        int questionNum = Integer.parseInt(model.getValueAt(row, 0).toString());
        String participantId = model.getValueAt(row, 1).toString();

        clearForm();
        loadResponse(questionNum, participantId);
    }

    private void loadResponse(int qsNum, String participantId) {
        if (qsNum == -1) {
            clearForm();
            return;
        }

        Exam exam = (Exam) cbSelectExam.getSelectedItem();

        Question qs = null;
        Result participant_res = null;
        try {
            qs = question_dao.readByExamNumber(exam.getId(), qsNum);
            if (qs == null) {
                return;
            }

            for (Result res : result_dao.readByExamAndUser(exam.getId(), participantId)) {
                if (res.getNumber() == qsNum) {
                    participant_res = res;
                    break;
                }
            }
        } catch (Exception e) {
            showError(e);
            return;
        }

        cbSelectNumber.setSelectedItem(qsNum);
        cbSelectParticipant.setSelectedItem(participantId);
        txtQuestionContent.setText(qs.getContent());
        txtQuestionCorrect.setText(qs.getCorrectAnswer());

        if (participant_res != null) {
            txtAnswer.setText(participant_res.getAnswer());
            txtScore.setText(String.format("%.2f", participant_res.getScore()));
            isEditing = true;
        } else {
            isEditing = false;
        }

        updateButtons();
    }

    private void addScore() {
        Exam exam = (Exam) cbSelectExam.getSelectedItem();
        if (exam == null)
            return;

        try {
            int qsNum = cbSelectNumber.getSelectedItem() == null ? -1 : (Integer) cbSelectNumber.getSelectedItem();
            String qsParticipant = (String) cbSelectParticipant.getSelectedItem();

            if (qsNum == -1)
                return;

            if (qsParticipant == null)
                return;

            if (txtAnswer.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide an answer");
                return;
            }

            if (txtScore.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide a score value");
                return;
            }

            float score = Float.parseFloat(txtScore.getText());
            if (score < 0 || score > 10) {
                JOptionPane.showMessageDialog(this, "Score must be between 0-10");
                return;
            }

            Result new_res = new Result(
                    exam.getId(),
                    qsParticipant,
                    qsNum,
                    txtAnswer.getText(),
                    score);

            result_dao.create(new_res);
            loadResponses();

            JOptionPane.showMessageDialog(this, "Answer Added");

        } catch (Exception e) {
            showError(e);
        }

    }

    private void updateScore() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        Exam exam = (Exam) cbSelectExam.getSelectedItem();
        if (exam == null)
            return;

        try {
            int qsNum = cbSelectNumber.getSelectedItem() == null ? -1 : (Integer) cbSelectNumber.getSelectedItem();
            String qsParticipant = (String) cbSelectParticipant.getSelectedItem();

            if (qsNum == -1)
                return;

            if (qsParticipant == null)
                return;

            Result res = result_dao.readByExamUserNumber(exam.getId(), qsParticipant, qsNum);
            if (res == null)
                return;

            res.setAnswer(txtAnswer.getText());
            res.setScore(Float.parseFloat(txtScore.getText()));

            result_dao.update(res);
            loadResponses();

            JOptionPane.showMessageDialog(this, "Answer updated");

        } catch (Exception e) {
            showError(e);
        }
        // try {
        // int questionNo = Integer.parseInt(txtQuestionNo.getText());
        // String userId = txtParticipant.getText();
        // float score = Float.parseFloat(txtScore.getText());
        //
        // Result updated = new Result(
        // exam.getId(),
        // userId,
        // questionNo,
        // model.getValueAt(row, 2).toString(),
        // score);
        //
        // result_dao.update(updated);
        //
        // model.setValueAt(score, row, 3);
        // JOptionPane.showMessageDialog(this, "Score updated");

        // } catch (NumberFormatException e) {
        // JOptionPane.showMessageDialog(this, "Invalid score");
        // } catch (SQLException e) {
        // showError(e);
        // }
    }

    private void deleteScore() {
        Exam exam = (Exam) cbSelectExam.getSelectedItem();
        if (exam == null)
            return;

        int qsNum = cbSelectNumber.getSelectedItem() == null ? -1 : (Integer) cbSelectNumber.getSelectedItem();
        String qsParticipant = (String) cbSelectParticipant.getSelectedItem();

        if (qsNum == -1)
            return;

        if (qsParticipant == null)
            return;

        try {
            result_dao.delete(exam.getId(), qsParticipant, qsNum);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    private void clearForm() {
        cbSelectNumber.setSelectedIndex(-1);
        cbSelectParticipant.setSelectedIndex(-1);

        txtQuestionContent.setText("");
        txtQuestionCorrect.setText("");
        txtAnswer.setText("");
        txtScore.setText("");

        isEditing = false;
        updateButtons();
    }

    private void updateSelected() {
        int qsNum = cbSelectNumber.getSelectedItem() == null ? -1 : (Integer) cbSelectNumber.getSelectedItem();
        String qsParticipant = (String) cbSelectParticipant.getSelectedItem();

        // int tableRows = table.getRowCount();
        // for (int i = 0; i < tableRows; i++) {
        // if (Integer.parseInt(table.getValueAt(i, 0).toString()) == qsNum &&
        // table.getValueAt(i, 1).toString().equals(qsParticipant)) {
        // break;
        // }
        // }

        clearForm();
        loadResponse(qsNum, qsParticipant);
    }

    private void updateButtons() {
        if (isEditing) {
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
            btnClear.setEnabled(true);
        } else {
            btnAdd.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnClear.setEnabled(false);
        }
    }
}
