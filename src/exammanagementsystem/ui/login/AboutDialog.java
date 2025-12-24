package exammanagementsystem.ui.login;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "About - Exam Management System", true);
        setSize(500, 300);
        setLocationRelativeTo(parent);

        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutText.setBackground(getBackground());

        aboutText.setText(
            "Aplikasi Manajemen Ujian (Exam Management System)\n\n" +
            "Nama Programmer: \n" +
            "1. Ittihadi Ramadhan (24343038)\n" +
            "2. Bakthi Ananda Harisona (24343085)\n\n" +
            "Aplikasi ini digunakan untuk mengelola data ujian secara terpusat, " +
            "meliputi pengelolaan ujian, soal, dan hasil ujian.\n\n" +
            "Fitur utama:\n" +
            "- Manajemen ujian (judul, deskripsi, waktu mulai & selesai)\n" +
            "- Manajemen soal dan jawaban\n" +
            "- CRUD soal dan skor\n" +
            "- Login berdasarkan peran (Admin, Supervisor, Participant)\n" +
            "- Dashboard dengan distribusi nilai dan hasil per peserta\n\n" +
            "Catatan:\n" +
            "Aplikasi ini berfungsi sebagai dashboard manajemen data ujian, " +
            "bukan sebagai platform untuk melaksanakan ujian secara langsung."
        );

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(scrollPane, BorderLayout.CENTER);
    }
}
