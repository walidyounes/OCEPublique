/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components.Impl;

import Demo.Components.Annotations.Provided;
import Demo.Components.Interfaces.ColorConsumer;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class IntDisplay implements @Provided Consumer<Integer>, @Provided ColorConsumer {
    private JLabel label = new JLabel("");

    public IntDisplay(){
        JFrame frame = new JFrame("IntDisplay");
        label.setFont(new Font("Arial", Font.PLAIN, 25));
        frame.setMinimumSize(new Dimension(300,100));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void accept(Integer integer) {
        label.setText(String.format("%d",integer));
    }

    @Override
    public void acceptColor(Color color){
        label.setForeground(color);
    }
}
