/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Selection;

import AmbientEnvironment.OCPlateforme.OCService;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.Tools.Criteria;
import OCE.Tools.FilterTool.AgreeCriteria;
import OCE.Tools.FilterTool.MatchingAdvertiseCriteria;
import OCE.Tools.FilterTool.ReplyCriteria;
import OCE.Tools.FilterTool.SelectCriteria;
import OCE.Unifieur.IMatching;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrioritySelection implements IMessageSelection {

    private float alpha; // threshold
    private OCService myservice; // The service that we handle (used when searching for
    private IMatching matching; // The matching method between the service used when selecting advertisement

    public PrioritySelection(OCService myservice, IMatching matching) {
        this.myservice = myservice;
        this.matching = matching;
    }

    /** The priority is fixed as follow (0 higher)
     *  Others = 4 (Bind message and EmptyInfraMessage)
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
    public InfraMessage singleSelect(ArrayList<InfraMessage> perceptions) {
        //Todo For now when we select a message from a sublist we do it randomly -> after we have to choose the right one (witch learning)
        Criteria myCriteria;
        //Check fo agree messages
        myCriteria = new AgreeCriteria();
        List<InfraMessage> messagesList = myCriteria.meetCriteria(perceptions);
        if (messagesList.size()>0){ // We have at least an agree message
            Random r = new Random();
            int index = r.nextInt(messagesList.size());
            return messagesList.get(index);
        }else{
            //Check fo select messages
            myCriteria = new SelectCriteria();
            messagesList = myCriteria.meetCriteria(perceptions);//.filter().collect(Collectors.toList());
            if(messagesList.size()>0){ // we have at least a select message
                Random r = new Random();
                int index = r.nextInt(messagesList.size());
                return messagesList.get(index);
            }else{
                //Check fo reply messages
                myCriteria = new ReplyCriteria();
                messagesList = myCriteria.meetCriteria(perceptions);
                if(messagesList.size()>0){ // we have at least a response message
                    Random r = new Random();
                    int index = r.nextInt(messagesList.size());
                    return messagesList.get(index);
                }else{
                    //Check fo advertise messages
                    myCriteria = new MatchingAdvertiseCriteria(myservice, matching);
                    messagesList = myCriteria.meetCriteria(perceptions);
                    if(messagesList.size()>0) { // we have at least an advertisement message
                        Random r = new Random();
                        int index = r.nextInt(messagesList.size());
                        return messagesList.get(index);
                    }else{ // others messages
                        Random r = new Random();
                        int index = r.nextInt(perceptions.size());
                        return perceptions.get(index);
                    }
                }
            }
        }

    }

    @Override
    public ArrayList<InfraMessage> multipleSelect(ArrayList<InfraMessage> perceptions) {
        return null;
    }
}
