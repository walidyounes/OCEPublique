/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Tools.FilterTool;

import AmbientEnvironment.OCPlateforme.OCService;
import OCE.InfrastructureMessages.InfraARSAMessages.AdvertiseInfraMessage;
import OCE.InfrastructureMessages.InfraMessage;
import OCE.OCEMessages.MessageTypes;
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
     * This function is used to filter a list of infraMessages to keep only the advertisement message that matches a service
     * @param infraMessages : the list of infraMessages to filter
     * @return the filtered list containing only the advertisement infraMessages that match to "service"
     */
    @Override
    public ArrayList<InfraMessage> meetCriteria(ArrayList<InfraMessage> infraMessages) {
        //Filter the advertise message to keep Only the matching ones
        ArrayList<InfraMessage> myListAdvertiseFiltered = new ArrayList<>( infraMessages.stream().filter(m -> m.getMyType()== MessageTypes.ADVERTISE)
                                                            .map(m -> (AdvertiseInfraMessage) m)
                                                                .filter(am -> matching.match(service,am.getMyService()))
                                                                    .collect(Collectors.toList())
                                                            );
        //Get the other messages (those which are not an advertise message)
        ArrayList<InfraMessage> myListMessagesFiltered = new ArrayList<>(infraMessages.stream().filter(m -> m.getMyType()!= MessageTypes.ADVERTISE).collect(Collectors.toList()));
        //Add the list of matching advertise messages
        myListMessagesFiltered.addAll(myListAdvertiseFiltered);

        return myListMessagesFiltered;
//        return new ArrayList<InfraMessage> ( infraMessages.stream().filter(m -> m.getMyType()== MessageTypes.ADVERTISE)
//                                                           .map(m -> (AdvertiseInfraMessage) m)
//                                                               .filter(am -> matching.match(service,am.getMyService()))
//                                                                  .collect(Collectors.toList())
//                                                            );
    }
}
