/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import OCE.OCEMessages.MessageTypes;

public class TestEnum {
    public static void main(String[] args){
        MessageTypes typeAdvertise = MessageTypes.ADVERTISE;
        MessageTypes typeReply = MessageTypes.REPLY;

        System.out.println(""+ (typeAdvertise.ordinal() + typeReply.ordinal()));
    }
}
