package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

//coba mu lihat dulu hadi, aku gak bisa nengok outputnya masalahnya
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExamManagementPanel extends JPanel {

    private final List<ExamRow> exams = new ArrayList<>();

    // Bagian UI nya
    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> cbSelectExam;
    private JTextField txtTitle;
    private JTextArea txtDescription;

    private JTabbedPane tabs;

    public ExamManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        initHeader();
        initTable();
        initForm();
        initTabs();
    }

    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbSelectExam = new JComboBox<>();
        cbSelectExam.addItem("ALL EXAMS");
        cbSelectExam.addActionListener(e -> filterTable());

        header.add(new JLabel("Select Exam:"));
        header.add(cbSelectExam);

        add(header, BorderLayout.NORTH);
    }

    // ================= TABLE =================
    private void initTable() {
        model = new DefaultTableModel(
            new Object[]{"ID", "Title"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedExam());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtTitle = new JTextField(15);
        txtDescription = new JTextArea(4, 15);
        txtDescription.setLineWrap(true);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addExam());
        btnUpdate.addActionListener(e -> updateExam());
        btnDelete.addActionListener(e -> deleteExam());
        btnClear.addActionListener(e -> clearForm());

        int y = 0;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Title"), c);
        c.gridx = 1; form.add(txtTitle, c);

        y++;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Description"), c);
        c.gridx = 1; form.add(new JScrollPane(txtDescription), c);

        y++;
        c.gridx = 0; c.gridy = y; form.add(btnAdd, c);
        c.gridx = 1; form.add(btnUpdate, c);

        y++;
        c.gridx = 0; c.gridy = y; form.add(btnDelete, c);
        c.gridx = 1; form.add(btnClear, c);

        add(form, BorderLayout.EAST);
    }

    private void initTabs() {
        tabs = new JTabbedPane();

        tabs.add("Questions", simpleLabel("Questions CRUD here"));
        tabs.add("Participants", simpleLabel("Participants CRUD here"));
        tabs.add("Supervisors", simpleLabel("Supervisors CRUD here"));

        add(tabs, BorderLayout.SOUTH);
    }

    private JPanel simpleLabel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }

    // Logic untuk CRUD nya coba mu lihat
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
        model.addRow(new Object[]{id, title});
        cbSelectExam.addItem(id + " - " + title);

        clearForm();
    }

    private void updateExam() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        ExamRow exam = exams.get(row);
        exam.title = txtTitle.getText();
        exam.description = txtDescription.getText();

        model.setValueAt(exam.title, row, 1);
    }

    private void deleteExam() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        exams.remove(row);
        model.removeRow(row);
        clearForm();
        refreshComboBox();
    }

    private void loadSelectedExam() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        ExamRow exam = exams.get(row);
        txtTitle.setText(exam.title);
        txtDescription.setText(exam.description);
    }

    private void clearForm() {
        txtTitle.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }

    private void filterTable() {
        // mana tahu kepakek
    }

    private void refreshComboBox() {
        cbSelectExam.removeAllItems();
        cbSelectExam.addItem("ALL EXAMS");
        for (ExamRow e : exams) {
            cbSelectExam.addItem(e.id + " - " + e.title);
        }
    }

    //untuk model
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
