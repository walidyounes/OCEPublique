/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools.FilterTool;

import OCE.Messages.Message;
import OCE.Tools.Criteria;

import java.util.ArrayList;

/**
 * Class that implements the "And" logic operation between two Criteria
 */
public class AndCriteria implements Criteria {

    private Criteria criteria;
    private Criteria otherCriteria;

    /**
     * Constructor of this class
     * @param criteria : the first criteria
     * @param otherCriteria : the second criteria
     */
    public AndCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    /**
     * It runs the logical "and" operation between the two attributes "criteria" and "otherCriteria" specified in the construction of the class
     * @param messages : the list of messages to filter
     * @return the filtered list of messages
     */
    @Override
    public ArrayList<Message> meetCriteria(ArrayList<Message> messages) {
        return null;
    }
}
