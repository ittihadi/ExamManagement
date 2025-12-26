package exammanagementsystem.ui.result;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import exammanagementsystem.dao.ExamDAO;
import exammanagementsystem.dao.ExamDAO.Exam;
import exammanagementsystem.dao.QuestionDAO;
import exammanagementsystem.dao.QuestionDAO.Question;
import exammanagementsystem.dao.ResultDAO;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */
public class ResultPerParticipantPanel extends JPanel {

    private JComboBox<Exam> cbExam;
    private JTable table;
    private DefaultTableModel model;

    private ExamDAO examDAO = new ExamDAO();
    private ResultDAO resultDAO = new ResultDAO();
    private QuestionDAO questionDAO = new QuestionDAO();

    @SuppressWarnings("unused")
    public ResultPerParticipantPanel(String userId) {
        setLayout(new BorderLayout(10, 10));

        cbExam = new JComboBox<>();
        loadExamList(userId);
        cbExam.setSelectedIndex(-1);

        // Custom renderer to show item names intead of internal object names
        cbExam.setRenderer(new DefaultListCellRenderer() {
            @SuppressWarnings("rawtypes")
            @Override
            public Component getListCellRendererComponent(final JList list, Object value, final int index,
                    final boolean isSelected,
                    final boolean cellHasFocus) {

                Exam value_exam = (Exam) value;
                if (value_exam == null) {
                    value = "SELECT EXAM";
                } else {
                    value = value_exam.getTitle();
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        cbExam.addActionListener(e -> loadResult(userId));

        model = new DefaultTableModel(
                new String[] { "No.", "Question", "Answer", "Score" }, 0);
        table = new JTable(model);

        add(cbExam, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadExamList(String userId) {
        try {
            for (ExamDAO.Exam e : examDAO.readByParticipant(userId)) {
                cbExam.addItem(e);
            }
        } catch (Exception ignored) {
        }
    }

    private void loadResult(String userId) {
        model.setRowCount(0);
        ExamDAO.Exam exam = (ExamDAO.Exam) cbExam.getSelectedItem();
        if (exam == null)
            return;

        try {
            for (ResultDAO.Result r : resultDAO.readByExamAndUser(exam.getId(), userId)) {
                int qsNum = r.getNumber();
                Question qs = questionDAO.readByExamNumber(exam.getId(), qsNum);

                model.addRow(new Object[] {
                        r.getNumber(),
                        qs.getContent().replace('\n', ' '),
                        r.getAnswer(),
                        r.getScore()
                });
            }
        } catch (Exception ignored) {
        }
    }
}
