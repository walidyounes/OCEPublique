/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import MASInfrastructure.Communication.IMessage;

import java.util.ArrayList;

public class WeightedPriorityChoice implements IMessageSelection {
    private float alpha; // Le seuil d'interêt

    @Override
    public IMessage singleSelect(ArrayList<IMessage> perceptions) {
        return null;
    }

    @Override
    public ArrayList<IMessage> multipleSelect(ArrayList<IMessage> perceptions) {
        return null;
    }
}
