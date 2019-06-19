/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools;

import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public interface Criteria {
    /**
     * Function used to filter a list of infraMessages according to a certain criteria
     */
     ArrayList<InfraMessage> meetCriteria(ArrayList<InfraMessage> infraMessages);
}
