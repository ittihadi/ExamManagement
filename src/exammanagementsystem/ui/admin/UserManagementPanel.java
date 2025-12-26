package exammanagementsystem.ui.admin;

/**
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exammanagementsystem.dao.UserDAO;
import exammanagementsystem.dao.UserDAO.User;
import exammanagementsystem.dao.UserDAO.Roles;

import java.awt.*;
import java.sql.SQLException;

//useenamenya dah ku hapus
//dah ku connect juga tapi coba cek dulu, mana tahu ada kesalahan lagi :v

public class UserManagementPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtUserId;
    private JTextField txtPassword;
    private JComboBox<String> cbRole;

    private final UserDAO user_dao;

    public UserManagementPanel() {
        user_dao = new UserDAO();

        setLayout(new BorderLayout(10, 10));
        initTable();
        initForm();
    }

    /* ================= TABLE ================= */

    @SuppressWarnings("unused")
    private void initTable() {
        model = new DefaultTableModel(
                new Object[] { "User ID", "Password", "Role" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedUser());

        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void reloadTable() {
        model.setRowCount(0);
        try {
            for (User u : user_dao.readNonAdmins()) {
                model.addRow(new Object[] {
                        u.getId(),
                        u.getPass(),
                        u.getRole().toString()
                });
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    @SuppressWarnings("unused")
    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtUserId = new JTextField(15);
        txtPassword = new JTextField(15);
        cbRole = new JComboBox<>(new String[] { "SUPERVISOR", "PARTICIPANT" });

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearForm());

        int y = 0;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("User ID"), c);
        c.gridx = 1;
        form.add(txtUserId, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Password"), c);
        c.gridx = 1;
        form.add(txtPassword, c);

        y++;
        c.gridx = 0;
        c.gridy = y;
        form.add(new JLabel("Role"), c);
        c.gridx = 1;
        form.add(cbRole, c);

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

        add(form, BorderLayout.EAST);
    }

    // ini untuk bagian CRUD nya

    private void addUser() {
        String id = txtUserId.getText().trim();
        String pass = txtPassword.getText().trim();

        if (id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID & Password required");
            return;
        }

        try {
            User user = new User(
                    id,
                    pass,
                    Roles.valueOf(cbRole.getSelectedItem().toString()));
            user_dao.create(user);
            reloadTable();
            clearForm();
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            User user = new User(
                    txtUserId.getText().trim(),
                    txtPassword.getText().trim(),
                    Roles.valueOf(cbRole.getSelectedItem().toString()));
            user_dao.update(user);
            reloadTable();
            clearForm();
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        String id = model.getValueAt(row, 0).toString();
        try {
            user_dao.delete(id);
            reloadTable();
            clearForm();
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void loadSelectedUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            txtUserId.setEditable(true);
            return;
        }

        txtUserId.setEditable(false);
        txtUserId.setText(model.getValueAt(row, 0).toString());
        txtPassword.setText(model.getValueAt(row, 1).toString());
        cbRole.setSelectedItem(model.getValueAt(row, 2).toString());
    }

    private void clearForm() {
        txtUserId.setEditable(true);
        txtUserId.setText("");
        txtPassword.setText("");
        cbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
