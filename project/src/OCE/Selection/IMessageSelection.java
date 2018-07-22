/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import OCE.Messages.Message;

import java.util.ArrayList;

public interface IMessageSelection {

    Message singleSelect(ArrayList<Message> perceptions);

    ArrayList<Message> multipleSelect(ArrayList<Message> perceptions);
}
