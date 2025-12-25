package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;
import exammanagementsystem.dao.ExamDAO;
import javax.swing.table.DefaultTableModel;

public class AvailableExamPanel extends JPanel {

    private ExamDAO examDAO = new ExamDAO();
    private JTable table;
    private DefaultTableModel model;

    public AvailableExamPanel(String userId) {
        setLayout(new BorderLayout(10,10));

        model = new DefaultTableModel(
            new String[]{"Exam ID", "Title", "Start", "End"}, 0
        );
        table = new JTable(model);

        loadData(userId);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData(String userId) {
        try {
            for (ExamDAO.Exam e : examDAO.readByParticipant(userId)) {
                model.addRow(new Object[]{
                    e.getId(),
                    e.getTitle(),
                    e.getStart_time(),
                    e.getEnd_time()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed load exams");
        }
    }
}
