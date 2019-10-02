/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.MockupCompo.MockupComponent;
import OCE.ServiceConnection.Connection;

import java.util.List;

public interface IFileFormatter {

    public void convertFormat(List<MockupComponent> listComponents, List<Connection> listConnections);
}
