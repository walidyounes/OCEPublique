/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components.Impl;

import Demo.Components.Annotations.Required;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * Generates a random integer between 0 / 99 every second
 */
public class RandomIntGenerator {

    private Consumer<Integer> consumer = null;

    public RandomIntGenerator() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int a = (int) (100 * Math.random());
                if(consumer != null){
                    System.out.println(super.toString() + " : " + a);
                    consumer.accept(a);
                }
            }
        });
        timer.start();
    }

    public Consumer<Integer> getConsumer() {
        return consumer;
    }

    @Required
    public void setIntegerConsumer(Consumer<Integer> consumer) {
        this.consumer = consumer;
    }
}
