/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components.Impl;

import Demo.Components.Annotations.Provided;
import Demo.Components.Annotations.Required;

import java.util.function.Consumer;

public class PositiveNegativeConverter implements @Provided Consumer<Integer> {

    private Consumer<Integer> positiveConsumer;

    private Consumer<Integer> negativeConsumer;
    
    @Override
    public void accept(Integer integer) {
        if(positiveConsumer != null){
            System.out.println(super.toString() + " : " + integer);
            positiveConsumer.accept(integer);
        }
        if(negativeConsumer != null){
            System.out.println(super.toString() + " : " + (-1*integer));
            negativeConsumer.accept(-1*integer);
        }
    }

    @Required
    public void setPositiveConsumer(Consumer<Integer> positiveConsumer) {
        this.positiveConsumer = positiveConsumer;
    }

    @Required
    public void setNegativeConsumer(Consumer<Integer> negativeConsumer) {
        this.negativeConsumer = negativeConsumer;
    }

}
