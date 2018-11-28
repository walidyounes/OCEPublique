/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools.FilterTool;

import OCE.Messages.Message;
import OCE.Messages.MessageTypes;
import OCE.Tools.Criteria;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BindCriteria implements Criteria {
    /**
     * This function is used to filter a list of messages to keep only the Bind messages
     * @param messages
     * @return
     */
    @Override
    public ArrayList<Message> meetCriteria(ArrayList<Message> messages) {
        return new ArrayList<Message>( messages.stream().filter(m -> m.getMyType()== MessageTypes.BIND)
                .collect(Collectors.toList())
        );
    }
}
