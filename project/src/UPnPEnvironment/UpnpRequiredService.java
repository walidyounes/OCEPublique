/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UPnPEnvironment;

import AmbientEnvironment.MockupCompo.AddLinkException;
import AmbientEnvironment.MockupCompo.RemoveLinkException;
import AmbientEnvironment.MockupCompo.SingleLinkMockupService;
import AmbientEnvironment.MockupCompo.Way;
import AmbientEnvironment.OCPlateforme.OCService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;

import java.util.ArrayList;
import java.util.List;

public class UpnpRequiredService extends SingleLinkMockupService {

    private Action bindingAction;

    private List<String> udnQueue = new ArrayList<>();

    public UpnpRequiredService(String name, String matchingID, String owner, Action bindingAction) {
        super(name, matchingID, owner, Way.REQUIRED);
        this.bindingAction = bindingAction;
    }

    public void addLink(OCService s) throws AddLinkException {
        super.addLink(s);
        UpnpProvidedService provided = (UpnpProvidedService)s;

        if( !udnQueue.isEmpty() ) {
            udnQueue.add(provided.getUdn());
        }

        bindToUDN(provided.getUdn());
    }

    public void removeLink(OCService s) throws RemoveLinkException {
        super.removeLink(s);

        if( !udnQueue.isEmpty() ) {
            udnQueue.add(null);
        }

        bindToUDN(null);
    }

    public void bindToUDN(String udn) {
        ActionInvocation actionInvocation = new ActionInvocation(bindingAction);

        try {
            actionInvocation.setInput("ServiceName", getName());
            actionInvocation.setInput("DeviceNumber", udn == null?"":udn);

            UPnPProbe.getUpnpService().getControlPoint().execute(new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation actionInvocation) {
                    System.out.println(udn == null?"Unbind successful":"Bind successful");

                    if( !udnQueue.isEmpty() ) {
                        bindToUDN(udnQueue.remove(0));
                    }
                }

                @Override
                public void failure(ActionInvocation invocation,
                                    UpnpResponse operation,
                                    String defaultMsg) {
                    System.err.println(defaultMsg);

                    if( !udnQueue.isEmpty() ) {
                        bindToUDN(udnQueue.remove(0));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
