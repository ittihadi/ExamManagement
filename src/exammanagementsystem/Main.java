package exammanagementsystem;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.SwingUtilities;
import exammanagementsystem.ui.login.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}   
//NOTE: coba mu lihat dulu kek mana bentuknya aku gak bisa nengok