/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import OCE.Messages.Message;

import java.util.ArrayList;

public class PrioritySelection implements IMessageSelection {

    private float alpha; // Le seuil d'intérêt

    @Override
    public Message singleSelect(ArrayList<Message> perceptions) {
        return null;
    }

    @Override
    public ArrayList<Message> multipleSelect(ArrayList<Message> perceptions) {
        return null;
    }
}
