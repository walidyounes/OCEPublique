/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;
import java.util.Random;

public class RandomSelection implements IMessageSelection {

    private int maxItem; // Le nombre maximum de perceptions séléctionnées

    public RandomSelection(int maxItem) {
        this.maxItem = maxItem;
    }

    /*
    * Sélectionner aléatoirement un messsage (Perception)
     */
    @Override
    public InfraMessage singleSelect(ArrayList<InfraMessage> perceptions) {
        Random r = new Random();
        int index = r.nextInt(perceptions.size());
        //System.out.println(" Index généré = "+index);
        return perceptions.get(index);
    }

    @Override
    public ArrayList<InfraMessage> multipleSelect(ArrayList<InfraMessage> perceptions) {
        return new ArrayList<>();
    }
}
