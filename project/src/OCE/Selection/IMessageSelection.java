/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import OCE.InfrastructureMessages.InfraMessage;

import java.util.ArrayList;

public interface IMessageSelection {

    InfraMessage singleSelect(ArrayList<InfraMessage> perceptions);

    ArrayList<InfraMessage> multipleSelect(ArrayList<InfraMessage> perceptions);
}
