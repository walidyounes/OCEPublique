/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.DeviceBinder;

import AmbientEnvironment.MockupCompo.MockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Logger.MyLogger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.logging.Level;

public class PhysicalDeviceBinder {

    private static StringProperty UIBinderServices; // a variable used to trigger the binding of two services in the UI

    public PhysicalDeviceBinder() {
        UIBinderServices = new SimpleStringProperty("");
    }

    public static void bindServices(OCService service1, OCService service2){
        MockupService MService1 = (MockupService)service1;
        MockupService MService2 = (MockupService)service2;
        // Make the binding between the two services
        MyLogger.log(Level.INFO, "Binding "+ MService1.getName()+"-"+MService1.getOwner()+" to "+MService2.getName()+"-"+MService2.getOwner());
        PhysicalDeviceBinder.UIBinderServices.set(""+MService1.getName()+""+MService1.getOwner()+""+ MService1.getWay()+"-"+MService2.getName()+""+MService2.getOwner()+""+ MService2.getWay());
    }

    public String getUIBinderServices() {
        return UIBinderServices.get();
    }

    public StringProperty UIBinderServicesProperty() {
        return UIBinderServices;
    }

}
