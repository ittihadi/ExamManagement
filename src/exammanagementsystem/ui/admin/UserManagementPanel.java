package exammanagementsystem.ui.admin;

/**
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exammanagementsystem.dao.UserDAO;
import exammanagementsystem.dao.UserDAO.User;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManagementPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JComboBox<String> cbRole;

    private UserDAO user_dao;

    public UserManagementPanel() {
        user_dao = new UserDAO();

        setLayout(new BorderLayout(10, 10));
        initTable();
        initForm();
    }

    // Ini untuk tabelnya, tapi coba mu tengok nanti soalnya aku 100% gak bisa
    // nengok hasilnya

    private void initTable() {
        model = new DefaultTableModel(
                new Object[] { "ID", "Password", "Role" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabelnya read only
            }
        };

        try {
            user_dao.readNonAdmins()
                    .forEach(u -> model.addRow(new Object[] { u.getId(), u.getPass(), u.getRole().toString() }));
        } catch (Exception e) {
            System.out.println("Gagal membaca user list: ");
            e.printStackTrace();
        }

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedUser());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(15);
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
        form.add(new JLabel("Username"), c);
        c.gridx = 1;
        form.add(txtUsername, c);

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

    // untuk Bagian CRUD nya (coba nanti testkan, aku gak bisa test)

    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getText());
        String role = cbRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username & Password required");
            return;
        }

        String id = UUID.randomUUID().toString().substring(0, 8);

        UserRow user = new UserRow(id, username, password, role);
        // users.add(user);

        model.addRow(new Object[] { id, username, role });
        clearForm();
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        // UserRow user = users.get(row);
        // user.username = txtUsername.getText();
        // user.password = new String(txtPassword.getPassword());
        // user.role = cbRole.getSelectedItem().toString();
        //
        // model.setValueAt(user.username, row, 1);
        // model.setValueAt(user.role, row, 2);
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        // users.remove(row);
        // model.removeRow(row);
        // clearForm();
    }

    private void loadSelectedUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        String user_id = model.getValueAt(row, 0).toString();
        try {
            User selected_user = user_dao.readById(user_id);
            txtPassword.setText(selected_user.getPass());
            cbRole.setSelectedItem(selected_user.getRole().toString());
        } catch (SQLException e) {
            System.out.println("Gagal membaca user:");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        cbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    private static class UserRow {
        String id;
        String username;
        String password;
        String role;

        UserRow(String id, String username, String password, String role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
}
