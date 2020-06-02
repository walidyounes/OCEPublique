/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components.Impl;

import Demo.Components.Annotations.Provided;

import javax.swing.*;
import java.util.function.Consumer;

public class IntDisplay implements @Provided Consumer<Integer> {
    private JLabel label = new JLabel("");

    public IntDisplay(){
        JFrame frame = new JFrame("IntDisplay");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void accept(Integer integer) {
        label.setText(String.format("%d",integer));
    }
}
