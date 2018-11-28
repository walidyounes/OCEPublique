/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import OCE.Messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrioritySelection implements IMessageSelection {

    private float alpha; // threshold

    /** The priority is fixed as follow (0 higher)
     *  Others = 4 (Bind message and EmptyMessage)
     *  Advertise = 3
     *  Reply = 2
     *  Select = 1
     *  Agree = 0
     */
    /**
     * Select a single message to treat taking into account a fixed priority between types of messages
     * @param perceptions : the list of received messages
     * @return the selected priority message
     */
    @Override
    public Message singleSelect(ArrayList<Message> perceptions) {
    /*    //Check fo agree messages
        List<Message> messagesList = perceptions.stream();//.filter().collect(Collectors.toList());
        if (messagesList.size()>0){ // We have at least an agree message

        }else{
            //Check fo select messages
            messagesList = perceptions.stream();//.filter().collect(Collectors.toList());
            if(messagesList.size()>0){ // we have at least a select message

            }else{
                //Check fo reply messages
                messagesList = perceptions.stream().filter().collect(Collectors.toList());
                if(messagesList.size()>0){ // we have at least a response message

                }else{
                    //Check fo advertise messages
                    messagesList = perceptions.stream().filter().collect(Collectors.toList());
                    if(messagesList.size()>0) { // we have at least an advertisement message

                    }else{

                    }
                }
            }
        }*/
    return null;
    }

    @Override
    public ArrayList<Message> multipleSelect(ArrayList<Message> perceptions) {
        return null;
    }
}
