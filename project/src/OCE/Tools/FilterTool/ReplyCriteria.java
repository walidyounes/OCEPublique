/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools.FilterTool;

import OCE.Messages.Message;
import OCE.Messages.MessageTypes;
import OCE.Tools.Criteria;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReplyCriteria implements Criteria {
    /**
     * This function is used to filter a list of messages to keep only the reply message
     * @param messages : the list of messages to filter
     * @return the filtered list containing only the reply messages
     */
    @Override
    public ArrayList<Message> meetCriteria(ArrayList<Message> messages) {
        return new ArrayList<Message>( messages.stream().filter(m -> m.getMyType()== MessageTypes.REPLY)
                .collect(Collectors.toList())
        );
    }
}
