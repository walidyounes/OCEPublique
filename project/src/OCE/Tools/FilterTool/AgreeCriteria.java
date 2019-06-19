/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools.FilterTool;

import OCE.InfrastructureMessages.InfraMessage;
import OCE.OCEMessages.MessageTypes;
import OCE.Tools.Criteria;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AgreeCriteria implements Criteria {

    /**
     * This function is used to filter a list of infraMessages to keep only the agree message
     * @param infraMessages : the list of infraMessages to filter
     * @return the filtered list containing only the agree infraMessages
     */
    @Override
    public ArrayList<InfraMessage> meetCriteria(ArrayList<InfraMessage> infraMessages) {
        return new ArrayList<InfraMessage>( infraMessages.stream().filter(m -> m.getMyType()== MessageTypes.SELECT)
                .collect(Collectors.toList())
        );
    }
}
