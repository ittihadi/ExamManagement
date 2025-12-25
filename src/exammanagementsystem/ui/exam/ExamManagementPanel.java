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
import java.sql.Timestamp;
import java.time.Instant;

public class ExamManagementPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<Exam> cbSelectExam;
    private JTextField txtTitle;
    private JTextArea txtDescription;

    private JTabbedPane tabs;

    private ExamDAO examDAO;
    private Exam selectedExam;

    public ExamManagementPanel() {
        examDAO = new ExamDAO();
        setLayout(new BorderLayout(10, 10));

        initHeader();
        initFormAndTabs();
        loadExamList();
        updateUIState(false);
    }


    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbSelectExam = new JComboBox<>();
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

        cbSelectExam.addActionListener(e -> onExamSelected());

        header.add(new JLabel("Select Exam:"));
        header.add(cbSelectExam);

        add(header, BorderLayout.NORTH);
    }


    private void initFormAndTabs() {
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
        c.gridx = 0; c.gridy = y;
        form.add(new JLabel("Title"), c);
        c.gridx = 1;
        form.add(txtTitle, c);

        y++;
        c.gridx = 0; c.gridy = y;
        form.add(new JLabel("Description"), c);
        c.gridx = 1;
        form.add(new JScrollPane(txtDescription), c);

        y++;
        c.gridx = 0; c.gridy = y;
        form.add(btnAdd, c);
        c.gridx = 1;
        form.add(btnUpdate, c);

        y++;
        c.gridx = 0; c.gridy = y;
        form.add(btnDelete, c);
        c.gridx = 1;
        form.add(btnClear, c);

        tabs = new JTabbedPane();
        tabs.add("Questions", simpleLabel("Select exam first"));
        tabs.add("Participants", simpleLabel("Participants CRUD (per exam)"));
        tabs.add("Supervisors", simpleLabel("Supervisors CRUD (per exam)"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                form,
                tabs
        );
        split.setResizeWeight(0.3);

        add(split, BorderLayout.CENTER);
    }

    private void loadExamList() {
        cbSelectExam.removeAllItems();
        try {
            for (Exam exam : examDAO.readAll()) {
                cbSelectExam.addItem(exam);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed load exams");
        }
    }
    
    private void reloadTabsForSelectedExam() {
        tabs.removeAll();

        tabs.add("Questions", new QuestionManagementPanel(selectedExam.getId()));
        tabs.add("Participants", new ParticipantAssignPanel(selectedExam.getId()));
        tabs.add("Supervisors", new SupervisorAssignPanel(selectedExam.getId()));
    }


    private void onExamSelected() {
        selectedExam = (Exam) cbSelectExam.getSelectedItem();

        if (selectedExam == null) {
            updateUIState(false);
            return;
        }

        txtTitle.setText(selectedExam.getTitle());
        txtDescription.setText(selectedExam.getDescription());

        reloadTabsForSelectedExam();
        updateUIState(true);
    }

    private void addExam() {
        if (txtTitle.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Title required");
            return;
        }

        try {
            Exam exam = new Exam(
                    (int) (System.currentTimeMillis() / 1000),
                    txtTitle.getText(),
                    txtDescription.getText(),
                    Timestamp.from(Instant.now()),
                    null
            );

            examDAO.create(exam);
            loadExamList();
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed create exam");
        }
    }

    private void updateExam() {
        if (selectedExam == null) return;

        try {
            selectedExam.setTitle(txtTitle.getText());
            selectedExam.setDescription(txtDescription.getText());
            examDAO.update(selectedExam);
            loadExamList();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed update exam");
        }
    }

    private void deleteExam() {
        if (selectedExam == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this exam?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            examDAO.delete(selectedExam.getId());
            selectedExam = null;
            loadExamList();
            clearForm();
            updateUIState(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed delete exam");
        }
    }

    private void clearForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        cbSelectExam.setSelectedItem(null);
    }

    private void updateUIState(boolean enabled) {
        tabs.setEnabled(enabled);
    }

    private JPanel simpleLabel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }
}

