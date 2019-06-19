/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public class WeightedPriorityChoice implements IMessageSelection {
    private float alpha; // Le seuil d'interêt

    @Override
    public InfraMessage singleSelect(ArrayList<InfraMessage> perceptions) {
        return null;
    }

    @Override
    public ArrayList<InfraMessage> multipleSelect(ArrayList<InfraMessage> perceptions) {
        return null;
    }
}
