/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exammanagementsystem.ui.exam;

/**
 *
 * @author bakth
 */

import exammanagementsystem.dao.QuestionDAO;
import exammanagementsystem.dao.QuestionDAO.Question;
import exammanagementsystem.dao.QuestionDAO.QuestionType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class QuestionManagementPanel extends JPanel {

    private final int examId;
    private final QuestionDAO questionDAO;
    private boolean isEditing;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtNumber;
    private JTextArea txtContent;
    private JTextField txtCorrectAnswer;
    private JComboBox<QuestionType> cbType;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public QuestionManagementPanel(int examId) {
        this.isEditing = false;
        this.examId = examId;
        this.questionDAO = new QuestionDAO();

        setLayout(new BorderLayout(10, 10));

        initTable();
        initForm();
        loadQuestions();
    }

    @SuppressWarnings("unused")
    private void initTable() {
        model = new DefaultTableModel(
                new Object[] { "Number", "Type", "Question", "Correct Answer" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> loadSelected());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    @SuppressWarnings("unused")
    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtNumber = new JTextField(5);
        txtContent = new JTextArea(4, 20);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);

        txtCorrectAnswer = new JTextField(15);
        cbType = new JComboBox<>(QuestionType.values());

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addQuestion());
        btnUpdate.addActionListener(e -> updateQuestion());
        btnDelete.addActionListener(e -> deleteQuestion());
        btnClear.addActionListener(e -> clearForm());

        int y = 0;

        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Number"), c);
        c.gridx = 1;
        form.add(txtNumber, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Type"), c);
        c.gridx = 1;
        form.add(cbType, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Content"), c);
        c.gridx = 1;
        form.add(new JScrollPane(txtContent), c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Correct Answer"), c);
        c.gridx = 1;
        form.add(txtCorrectAnswer, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(btnAdd, c);
        c.gridx = 1;
        form.add(btnUpdate, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(btnDelete, c);
        c.gridx = 1;
        form.add(btnClear, c);

        updateButtons();
        if (examId == -1) {
            txtNumber.setEnabled(false);
            txtContent.setEnabled(false);
            cbType.setEnabled(false);
            txtCorrectAnswer.setEnabled(false);

            btnAdd.setEnabled(false);
        }

        add(form, BorderLayout.EAST);
    }

    private void loadQuestions() {
        model.setRowCount(0);
        if (examId == -1) {
            return;
        }

        try {
            List<Question> list = questionDAO.readByExam(examId);
            for (Question q : list) {
                model.addRow(new Object[] {
                        q.getNumber(),
                        q.getType(),
                        q.getContent().replace("\n", " "),
                        q.getCorrectAnswer()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed load questions");
            e.printStackTrace();
        }
    }

    private void addQuestion() {
        try {
            if (questionDAO.readByExamNumber(examId, Integer.parseInt(txtNumber.getText())) != null) {
                JOptionPane.showMessageDialog(this, "Number already exists");
                return;
            }

            Question q = new Question(
                    examId,
                    Integer.parseInt(txtNumber.getText()),
                    txtContent.getText(),
                    txtCorrectAnswer.getText(),
                    (QuestionType) cbType.getSelectedItem());

            questionDAO.create(q);
            loadQuestions();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    private void updateQuestion() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            Question q = new Question(
                    examId,
                    Integer.parseInt(txtNumber.getText()),
                    txtContent.getText(),
                    txtCorrectAnswer.getText(),
                    (QuestionType) cbType.getSelectedItem());

            questionDAO.update(q);
            loadQuestions();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
    }

    private void deleteQuestion() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        int number = (int) model.getValueAt(row, 0);

        try {
            questionDAO.delete(examId, number);
            loadQuestions();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Delete failed");
        }
    }

    private void loadSelected() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        txtNumber.setText(model.getValueAt(row, 0).toString());
        cbType.setSelectedItem(model.getValueAt(row, 1));
        txtContent.setText(model.getValueAt(row, 2).toString());
        txtCorrectAnswer.setText(model.getValueAt(row, 3).toString());

        isEditing = true;
        updateButtons();
    }

    private void clearForm() {
        txtNumber.setText("");
        txtContent.setText("");
        txtCorrectAnswer.setText("");
        table.clearSelection();

        isEditing = false;
        updateButtons();
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
