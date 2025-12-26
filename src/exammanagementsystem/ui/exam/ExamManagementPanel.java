package exammanagementsystem.ui.exam;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import exammanagementsystem.dao.ExamDAO;
import exammanagementsystem.dao.ExamDAO.Exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */
public class ExamManagementPanel extends JPanel {
    private boolean isEditing;

    private JComboBox<Exam> cbSelectExam;
    private JTextField txtTitle;
    private JTextArea txtDescription;

    private JTextField txtStartYear;
    private JTextField txtStartMonth;
    private JTextField txtStartDay;
    private JTextField txtStartHour;
    private JTextField txtStartMinute;

    private JTextField txtEndYear;
    private JTextField txtEndMonth;
    private JTextField txtEndDay;
    private JTextField txtEndHour;
    private JTextField txtEndMinute;

    private JTabbedPane tabs;

    private ExamDAO examDAO;
    private Exam selectedExam;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public ExamManagementPanel() {
        examDAO = new ExamDAO();
        isEditing = false;
        setLayout(new BorderLayout(10, 10));

        initHeader();
        initFormAndTabs();
        loadExamList();
        updateUIState(false);
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    private void initFormAndTabs() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtTitle = new JTextField(15);
        txtDescription = new JTextArea(4, 15);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        txtStartYear = new JTextField(4);
        txtStartMonth = new JTextField(2);
        txtStartDay = new JTextField(2);
        txtStartHour = new JTextField(2);
        txtStartMinute = new JTextField(2);

        txtEndYear = new JTextField(4);
        txtEndMonth = new JTextField(2);
        txtEndDay = new JTextField(2);
        txtEndHour = new JTextField(2);
        txtEndMinute = new JTextField(2);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

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
        c.gridwidth = 2;
        form.add(new JSeparator(), c);
        c.gridwidth = 1;

        // START TIME WIDGETS -----------------------------
        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Start Year"), c);
        c.gridx = 1;
        form.add(txtStartYear, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Start Month"), c);
        c.gridx = 1;
        form.add(txtStartMonth, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Start Day"), c);
        c.gridx = 1;
        form.add(txtStartDay, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Start Hour"), c);
        c.gridx = 1;
        form.add(txtStartHour, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Start Minute"), c);
        c.gridx = 1;
        form.add(txtStartMinute, c);

        // END TIME WIDGETS -------------------------------
        y++;
        c.gridx = 0;
        c.gridy = y;
        c.gridwidth = 2;
        form.add(new JSeparator(), c);
        c.gridwidth = 1;

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("End Year"), c);
        c.gridx = 1;
        form.add(txtEndYear, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("End Month"), c);
        c.gridx = 1;
        form.add(txtEndMonth, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("End Day"), c);
        c.gridx = 1;
        form.add(txtEndDay, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("End Hour"), c);
        c.gridx = 1;
        form.add(txtEndHour, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("End Minute"), c);
        c.gridx = 1;
        form.add(txtEndMinute, c);

        // BUTTONS ----------------------------------------

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

        tabs = new JTabbedPane();
        tabs.add("Questions", simpleLabel("Select exam first"));
        tabs.add("Participants", simpleLabel("Participants CRUD (per exam)"));
        tabs.add("Supervisors", simpleLabel("Supervisors CRUD (per exam)"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                form,
                tabs);
        split.setResizeWeight(0.3);

        updateButtons();

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
        cbSelectExam.setSelectedItem(null);
        selectedExam = null;
        reloadTabsForSelectedExam();
        clearForm();
    }

    private void reloadTabsForSelectedExam() {
        tabs.removeAll();

        int examId = selectedExam == null ? -1 : selectedExam.getId();

        tabs.add("Questions", new QuestionManagementPanel(examId));
        tabs.add("Participants", new ParticipantAssignPanel(examId));
        tabs.add("Supervisors", new SupervisorAssignPanel(examId));
    }

    private void onExamSelected() {
        selectedExam = (Exam) cbSelectExam.getSelectedItem();

        if (selectedExam == null) {
            updateUIState(false);
            return;
        }

        txtTitle.setText(selectedExam.getTitle());
        txtDescription.setText(selectedExam.getDescription());

        Timestamp start = selectedExam.getStartTime();
        Timestamp end = selectedExam.getEndTime();

        LocalDateTime start_loc = start.toLocalDateTime();
        LocalDateTime end_loc = end.toLocalDateTime();

        txtStartYear.setText(String.format("%04d", start_loc.getYear()));
        txtStartMonth.setText(String.format("%02d", start_loc.getMonthValue()));
        txtStartDay.setText(String.format("%02d", start_loc.getDayOfMonth()));
        txtStartHour.setText(String.format("%02d", start_loc.getHour()));
        txtStartMinute.setText(String.format("%02d", start_loc.getMinute()));

        txtEndYear.setText(String.format("%04d", end_loc.getYear()));
        txtEndMonth.setText(String.format("%02d", end_loc.getMonthValue()));
        txtEndDay.setText(String.format("%02d", end_loc.getDayOfMonth()));
        txtEndHour.setText(String.format("%02d", end_loc.getHour()));
        txtEndMinute.setText(String.format("%02d", end_loc.getMinute()));

        isEditing = true;
        updateButtons();

        reloadTabsForSelectedExam();
        updateUIState(true);
    }

    private void addExam() {
        if (txtTitle.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Title required");
            return;
        }

        try {
            Instant start = Instant.parse(String.format("%s-%s-%sT%s:%s:00Z",
                    txtStartYear.getText(),
                    txtStartMonth.getText(),
                    txtStartDay.getText(),
                    txtStartHour.getText(),
                    txtStartMinute.getText()));

            Instant end = Instant.parse(String.format("%s-%s-%sT%s:%s:00Z",
                    txtEndYear.getText(),
                    txtEndMonth.getText(),
                    txtEndDay.getText(),
                    txtEndHour.getText(),
                    txtEndMinute.getText()));

            Exam exam = new Exam(
                    (int) (System.currentTimeMillis() / 1000),
                    txtTitle.getText(),
                    txtDescription.getText(),
                    Timestamp.from(start),
                    Timestamp.from(end));

            examDAO.create(exam);
            loadExamList();
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed create exam");
        }
    }

    private void updateExam() {
        if (selectedExam == null)
            return;

        try {
            selectedExam.setTitle(txtTitle.getText());
            selectedExam.setDescription(txtDescription.getText());

            Instant start = Instant.parse(String.format("%s-%s-%sT%s:%s:00Z",
                    txtStartYear.getText(),
                    txtStartMonth.getText(),
                    txtStartDay.getText(),
                    txtStartHour.getText(),
                    txtStartMinute.getText()));

            selectedExam.setStartTime(Timestamp.from(start));

            Instant end = Instant.parse(String.format("%s-%s-%sT%s:%s:00Z",
                    txtEndYear.getText(),
                    txtEndMonth.getText(),
                    txtEndDay.getText(),
                    txtEndHour.getText(),
                    txtEndMinute.getText()));

            selectedExam.setEndTime(Timestamp.from(end));

            examDAO.update(selectedExam);
            loadExamList();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed update exam");
        }
    }

    private void deleteExam() {
        if (selectedExam == null)
            return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this exam?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

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
        selectedExam = null;

        txtStartYear.setText("");
        txtStartMonth.setText("");
        txtStartDay.setText("");
        txtStartHour.setText("");
        txtStartMinute.setText("");

        txtEndYear.setText("");
        txtEndMonth.setText("");
        txtEndDay.setText("");
        txtEndHour.setText("");
        txtEndMinute.setText("");

        reloadTabsForSelectedExam();
        isEditing = false;
        updateButtons();
    }

    private void updateUIState(boolean enabled) {
        tabs.setEnabled(enabled);
    }

    private JPanel simpleLabel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
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
