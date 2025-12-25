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

    public ExamSupervisionPanel(String user_id) {
        supervisor_id = user_id;
        exam_dao = new ExamDAO();

        setLayout(new BorderLayout(10, 10));
        initHeader(); // Select exam (siap ngab)
        initTable(); // All response, group by qs. number / user ID (siap ngab)
        initForm(); // Update answer, score (siap ngab)
        // TODO: Tambahin answer baru juga
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

        if (cbSelectExam.getItemCount() == 0) {
            cbSelectExam.setEnabled(false);
            cbSelectExam.addItem(null);
        }

        // Custom renderer to show item names intead of internal object names
        cbSelectExam.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(final JList list, Object value, final int index,
                    final boolean isSelected,
                    final boolean cellHasFocus) {

                Exam value_exam = (Exam) value;
                value = value_exam.getTitle();

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        cbSelectExam.addActionListener(e -> loadResponses());

        header.add(new JLabel("Select Exam:"));
        header.add(cbSelectExam);

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
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedResponse());

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
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Question No"), c);
        c.gridx = 1;
        form.add(txtQuestionNo, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Participant"), c);
        c.gridx = 1;
        form.add(txtParticipant, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Score"), c);
        c.gridx = 1;
        form.add(txtScore, c);

        y++;
        c.gridx = 1;
        c.gridy = y;
        form.add(btnUpdate, c);

        add(form, BorderLayout.EAST);
    }

    private JPanel simpleLabel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }

    // bagian logic

    // DAO nya belum kepanggil tapi rasanya udah siap untuk logicnya
    private void loadResponses() {
        model.setRowCount(0);

        Exam selected = (Exam) cbSelectExam.getSelectedItem();
        if (selected == null)
            return;

        // TODO:
        try {
            exam_dao.readById(selected.getId());
            // for each result:
            // model.addRow(...)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedResponse() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        txtQuestionNo.setText(model.getValueAt(row, 0).toString());
        txtParticipant.setText(model.getValueAt(row, 1).toString());
        txtScore.setText(model.getValueAt(row, 3).toString());
    }

    private void updateScore() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        String score = txtScore.getText().trim();

        if (score.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Score required");
            return;
        }

        // TODO:
        // ResultDAO.updateScore(...)
        model.setValueAt(score, row, 3);
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
