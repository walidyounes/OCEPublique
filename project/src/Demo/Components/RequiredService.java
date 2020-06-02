/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.OCPlateforme.OCService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequiredService extends SingleLinkMockupService {

    private Object implementation;

    private Method bindMethod;

    public RequiredService(String name, String matchingID, String owner, Object implementation, Method bindMethod) {
        super(name, matchingID, owner, Way.REQUIRED);
        this.implementation = implementation;
        this.bindMethod = bindMethod;
    }

    public void addLink(OCService s) throws AddLinkException {
        super.addLink(s);
        ProvidedService ps = (ProvidedService)s;
        try {
            bindMethod.invoke(implementation, ps.getImplementation());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void removeLink(OCService s) throws RemoveLinkException {
        super.removeLink(s);
        try {
            bindMethod.invoke(implementation, (Object) null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object getImplementation() {
        return implementation;
    }

    public void setImplementation(Object implementation) {
        this.implementation = implementation;
    }
}
