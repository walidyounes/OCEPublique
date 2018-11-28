/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools.FilterTool;

import AmbientEnvironment.OCPlateforme.OCService;
import OCE.Messages.AdvertiseMessage;
import OCE.Messages.Message;
import OCE.Messages.MessageTypes;
import OCE.Tools.Criteria;
import OCE.Unifieur.IMatching;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MatchingAdvertiseCriteria implements Criteria {

    private OCService service;
    private IMatching matching;

    /**
     * Create the advertisement matching criteria
     * @param service : the first service to match to
     * @param matching : the component that provide the matching method
     */
    public MatchingAdvertiseCriteria(OCService service, IMatching matching) {
        this.service = service;
        this.matching = matching;
    }

    /**
     * This function is used to filter a list of messages to keep only the advertisement message that matches a service
     * @param messages : the list of messages to filter
     * @return the filtered list containing only the advertisement messages that match to "service"
     */
    @Override
    public ArrayList<Message> meetCriteria(ArrayList<Message> messages) {
       return new ArrayList<Message>( messages.stream().filter(m -> m.getMyType()== MessageTypes.ADVERTISE)
                                                            .map(m -> (AdvertiseMessage) m)
                                                                .filter(am -> matching.match(service,am.getMyService()))
                                                                    .collect(Collectors.toList())
                                    );
    }
}
