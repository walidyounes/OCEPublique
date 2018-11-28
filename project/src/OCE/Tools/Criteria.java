/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools;

import OCE.Messages.Message;

import java.util.ArrayList;

public interface Criteria {
    /**
     * Function used to filter a list of messages according to a certain criteria
     */
     ArrayList<Message> meetCriteria(ArrayList<Message> messages);
}
