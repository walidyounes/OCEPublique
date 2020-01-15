/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.DeviceBinder;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.OCELogger;
import MOICE.MOICE;
import OCE.Agents.ServiceAgentPack.ServiceAgentConnexionState;
import OCE.ServiceConnection.Connection;
import UI.NotifySetStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PhysicalDeviceBinder {

    private static StringProperty UIBinderServices; // a variable used to trigger the binding of two services in the UI
    private NotifySetStringProperty UIBinderS;
    private List<Connection> listConnections; // A trace of the set of connexions between the services that this device realized




    /**
     * Construct the physical binder object (private cause we implement the singleton pattern)
     */
    private PhysicalDeviceBinder() {
        UIBinderServices = new SimpleStringProperty("");
        UIBinderS = new NotifySetStringProperty("");
        this.listConnections = new ArrayList<>();
    }

    /** Holder */
    private static class PhysicalDeviceBinderHolder
    {
        /** Unique instance not initialized */
        private final static PhysicalDeviceBinder instance = new PhysicalDeviceBinder();
    }

    /**
     * Access point to the unique instance of the PhysicalDeviceBinder
     * @return the unique instance to the physical PhysicalDeviceBinder
     */
     public static PhysicalDeviceBinder getInstance()
    {
        return PhysicalDeviceBinderHolder.instance;
    }

    public void bindServices(OCService service1, OCService service2){
        MockupService MService1 = (MockupService)service1;
        MockupService MService2 = (MockupService)service2;
        // Make the binding between the two services
        OCELogger.log(Level.INFO, "Binding "+ MService1.getName()+"-"+MService1.getOwner()+" to "+MService2.getName()+"-"+MService2.getOwner());
        PhysicalDeviceBinder.UIBinderServices.set(""+MService1.getName()+""+MService1.getMatchingID()+""+MService1.getOwner()+""+ MService1.getWay()+"-"+MService2.getName()+""+MService2.getMatchingID()+""+MService2.getOwner()+""+ MService2.getWay());
        this.UIBinderS.setValue(""+MService1.getName()+""+MService1.getMatchingID()+""+MService1.getOwner()+""+ MService1.getWay()+"-"+MService2.getName()+""+MService2.getMatchingID()+""+MService2.getOwner()+""+ MService2.getWay());

        MOICE.getInstance().collectOCEProposedConfiguration();
     }

    public String getUIBinderServices() {
        return UIBinderServices.get();
    }

    public StringProperty UIBinderServicesProperty() {
        return UIBinderServices;
    }

    public String getUIBinderS() {
        return UIBinderS.get();
    }

    public NotifySetStringProperty UIBinderSProperty() {
        return UIBinderS;
    }

    public void setUIBinderS(String UIBinderS) {
        this.UIBinderS.set(UIBinderS);
    }

    /**
     * Add a connection to the list of connexions
     * @param connection : the reference of the connection
     */
    public void addConnexion(Connection connection){
        this.listConnections.add(connection);
         MOICE.getInstance().registerConnection(connection);
    }

    //Todo : this function is obsolete
    /**
     * Delete the connexion between the two service agents, it's triggered by the user from the UI
     * @param idFirstService  :   the name of the first service
     * @param idSecondService :   the name of the second service
     */
    public void deleteConnexion(String idFirstService, String idSecondService){
        for(Connection connection : listConnections){
            if(connection.isTheSameServices(idFirstService,idSecondService)){
                OCELogger.log(Level.INFO, "Bingo");
                OCELogger.log(Level.INFO,"Connection to delete = " + connection.toString());
                //Disconnect the two service by changing their state
                connection.getFirstServiceAgent().setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
                connection.getSecondServiceAgent().setMyConnexionState(ServiceAgentConnexionState.NOT_CONNECTED);
            }
        }
    }
}
