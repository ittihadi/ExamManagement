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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExamManagementPanel extends JPanel {

    private final List<ExamRow> exams = new ArrayList<>();

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<Exam> cbSelectExam;
    private JTextField txtTitle;
    private JTextArea txtDescription;

    private JTabbedPane tabs;

    private ExamDAO exam_dao;

    public ExamManagementPanel() {
        exam_dao = new ExamDAO();

        setLayout(new BorderLayout(10, 10));
        // TODO: Ganti layounya
        // tabel exam ditengah ganti aja jadi Questions/participants/supervisors

        initHeader();
        initTable();
        initForm();
        initTabs();
        updateUIState(false);
    }

    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbSelectExam = new JComboBox<>();

        try {
            exam_dao.readAll().forEach(ex -> cbSelectExam.addItem(ex));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // cbSelectExam.addItem("-- Select Exam --");
        cbSelectExam.addActionListener(e -> onExamSelected());

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

        header.add(new JLabel("Select Exam:"));
        header.add(cbSelectExam);

        add(header, BorderLayout.NORTH);
    }

    private void initTable() {
        model = new DefaultTableModel(
                new Object[] { "ID", "Title" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedExam());
    }

    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtTitle = new JTextField(15);
        txtDescription = new JTextArea(4, 15);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addExam());
        btnUpdate.addActionListener(e -> updateExam());
        btnDelete.addActionListener(e -> deleteExam());
        btnClear.addActionListener(e -> clearForm());

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Title"), c);
        c.gridx = 1;
        form.add(txtTitle, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Description"), c);
        c.gridx = 1;
        form.add(new JScrollPane(txtDescription), c);

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

        JPanel right = new JPanel(new BorderLayout());
        right.add(form, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(table),
                right);
        split.setResizeWeight(0.65);

        add(split, BorderLayout.CENTER);
    }

    private void initTabs() {
        tabs = new JTabbedPane();

        tabs.add("Questions", simpleLabel("Questions CRUD (select exam first)"));
        tabs.add("Participants", simpleLabel("Participants CRUD (per exam)"));
        tabs.add("Supervisors", simpleLabel("Supervisors CRUD (per exam)"));

        add(tabs, BorderLayout.SOUTH);
    }

    private JPanel simpleLabel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }

    // Bagian Logic
    private void addExam() {
        String title = txtTitle.getText().trim();
        String desc = txtDescription.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title required");
            return;
        }

        String id = UUID.randomUUID().toString().substring(0, 8);
        ExamRow exam = new ExamRow(id, title, desc);

        exams.add(exam);
        model.addRow(new Object[] { id, title });
        // cbSelectExam.addItem(id + " - " + title);

        clearForm();
    }

    private void updateExam() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        ExamRow exam = exams.get(row);
        exam.title = txtTitle.getText();
        exam.description = txtDescription.getText();

        model.setValueAt(exam.title, row, 1);
        refreshComboBox();
    }

    private void deleteExam() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        exams.remove(row);
        model.removeRow(row);
        clearForm();
        refreshComboBox();
        updateUIState(false);
    }

    private void loadSelectedExam() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        ExamRow exam = exams.get(row);
        txtTitle.setText(exam.title);
        txtDescription.setText(exam.description);
        updateUIState(true);
    }

    private void clearForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }

    private void onExamSelected() {
        if (cbSelectExam.getSelectedIndex() <= 0) {
            updateUIState(false);
        } else {
            updateUIState(true);
        }
    }

    private void updateUIState(boolean enabled) {
        txtTitle.setEnabled(enabled);
        txtDescription.setEnabled(enabled);
        tabs.setEnabled(enabled);
    }

    private void refreshComboBox() {
        cbSelectExam.removeAllItems();
        // cbSelectExam.addItem("-- Select Exam --");
        // for (ExamRow e : exams) {
        // cbSelectExam.addItem(e.id + " - " + e.title);
        // }
    }

    private static class ExamRow {
        String id;
        String title;
        String description;

        ExamRow(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }
    }
}
