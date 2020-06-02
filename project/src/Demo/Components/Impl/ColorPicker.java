/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components.Impl;

import Demo.Components.Annotations.Required;
import Demo.Components.Interfaces.ColorConsumer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorPicker {
    private ColorConsumer colorConsumer = null;

    private JColorChooser jColorChooser;

    public ColorPicker(){
        jColorChooser = new JColorChooser();
        jColorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if(colorConsumer != null){
                    colorConsumer.acceptColor(jColorChooser.getColor());
                }
            }
        });

        JFrame frame = new JFrame("ColorPicker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(jColorChooser);
        frame.pack();
        frame.setVisible(true);
    }

    @Required
    public void setColorConsumer(ColorConsumer colorConsumer){
        this.colorConsumer = colorConsumer;
    }
}
