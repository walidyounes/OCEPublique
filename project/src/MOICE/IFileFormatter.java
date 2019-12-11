/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import AmbientEnvironment.MockupCompo.MockupComponent;
import OCE.ServiceConnection.Connection;

import java.util.List;

public interface IFileFormatter {

    /**
     * Create the file used by ICE for visualition with the right formatting
     * @param listComponents    : the list of available components in the environment
     * @param listConnections   : the list of connections proposed by OCE
     */
     void convertFormat(List<MockupComponent> listComponents, List<Connection> listConnections);

    /**
     * Clear the content of the file used to visualisation in ICE
     */
    void clearICEFileContent();
}
