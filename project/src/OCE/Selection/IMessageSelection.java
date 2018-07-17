/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import MASInfrastructure.Communication.IMessage;

import java.util.ArrayList;

public interface IMessageSelection {

    IMessage singleSelect(ArrayList<IMessage> perceptions);

    ArrayList<IMessage> multipleSelect(ArrayList<IMessage> perceptions);
}
