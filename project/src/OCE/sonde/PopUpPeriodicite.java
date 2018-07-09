/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.sonde;

import javax.swing.*;
import java.awt.*;

public class PopUpPeriodicite extends JFrame {

    private JFrame frame;
    private JPanel panel;
    private JLabel labelPeriodicite;
    private JTextField fieldPeriodicite;
    private JButton btnOK;
    private int periodicite = 2000; // 2000ms par default

    public PopUpPeriodicite() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();

        labelPeriodicite = new JLabel("Periodicite (ms)");

        fieldPeriodicite = new JTextField(10);

        btnOK = new JButton("OK");

        btnOK.addActionListener(e -> {
            acquerirPeriodicite();
        });

        panel.add(labelPeriodicite);
        panel.add(fieldPeriodicite);
        panel.add(btnOK);

        Dimension dimension = panel.getPreferredSize();
        dimension.width = 300;
        dimension.height = 150;
        panel.setPreferredSize(dimension);

        frame.add(panel);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2, dimension.height / 2 - frame.getHeight() / 2);

        frame.pack();
        frame.setVisible(true);
    }

    private void acquerirPeriodicite() {
        boolean parsable = true;
        int value = 0;
        try {
            value = Integer.parseInt(fieldPeriodicite.getText());
        } catch (NumberFormatException e) {
            parsable = false;
        }
        if (!parsable || value <= 0) {
            JOptionPane.showMessageDialog(frame, "Invalid input!");
        } else {
            frame.dispose();
            this.periodicite = value;
        }
    }

    public int getPeriodicite(int periodicite) {
        return this.periodicite;
    }
}
