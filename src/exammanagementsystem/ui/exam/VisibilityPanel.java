package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class VisibilityPanel extends JPanel {

    public VisibilityPanel() {
        setLayout(new GridLayout(3,1,10,10));
        setBorder(BorderFactory.createTitledBorder("Result Visibility Settings"));

        JCheckBox correctStatus = new JCheckBox("Show Correct / Incorrect");
        JCheckBox realAnswer = new JCheckBox("Show Real Answer");
        JCheckBox score = new JCheckBox("Show Score");

        add(correctStatus);
        add(realAnswer);
        add(score);
    }
}