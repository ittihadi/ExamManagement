/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exammanagementsystem.ui.exam;

/**
 *
 * @author bakth
 */
import exammanagementsystem.dao.UserDAO;
import exammanagementsystem.dao.UserDAO.User;
import exammanagementsystem.dao.UserDAO.Roles;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SupervisorAssignPanel extends JPanel {

    private final int examId;
    private final UserDAO userDAO = new UserDAO();

    private JComboBox<User> cbUsers;
    private DefaultListModel<User> listModel;
    private JList<User> listAssigned;

    public SupervisorAssignPanel(int examId) {
        this.examId = examId;
        setLayout(new BorderLayout(8, 8));

        initUI();
        loadUsers();
        loadAssigned();
    }

    @SuppressWarnings("unused")
    private void initUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbUsers = new JComboBox<>();
        cbUsers.setRenderer(userRenderer());

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> addSupervisor());

        top.add(new JLabel("User:"));
        top.add(cbUsers);
        top.add(btnAdd);

        listModel = new DefaultListModel<>();
        listAssigned = new JList<>(listModel);
        listAssigned.setCellRenderer(userRenderer());

        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(e -> removeSupervisor());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(listAssigned), BorderLayout.CENTER);
        add(btnRemove, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        cbUsers.removeAllItems();
        try {
            for (User u : userDAO.readNonAdmins()) {
                if (u.getRole() == Roles.SUPERVISOR) {
                    cbUsers.addItem(u);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed load users");
        }
    }

    private void loadAssigned() {
        listModel.clear();
        try {
            List<User> users = userDAO.readSupervisorsByExamId(examId);
            users.forEach(listModel::addElement);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed load supervisors");
        }
    }

    private void addSupervisor() {
        User user = (User) cbUsers.getSelectedItem();
        if (user == null)
            return;

        try {
            List<User> supervisors = userDAO.readSupervisorsByExamId(examId);
            for (User supervisor : supervisors) {
                if (supervisor.getId().equals(user.getId()))
                    return;
            }

            userDAO.addToExam(user, examId);
            loadAssigned();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed add supervisor");
        }
    }

    private void removeSupervisor() {
        User user = listAssigned.getSelectedValue();
        if (user == null)
            return;

        try {
            userDAO.removeFromExam(user, examId);
            loadAssigned();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed remove supervisor");
        }
    }

    private DefaultListCellRenderer userRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                if (value instanceof User u) {
                    value = u.getId();
                }
                return super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
            }
        };
    }
}
