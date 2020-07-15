/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package UPnPEnvironment;

import AmbientEnvironment.OCPlateforme.OCService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UpnpComponentFactory {

    private static Map<String, Integer> componentCount = new HashMap<>();

    public static void makeUpnpComponent(Device device, Consumer<UpnpMockupComponent> callback) {
        if(!componentCount.containsKey(device.getDetails().getFriendlyName())){
            componentCount.put(device.getDetails().getFriendlyName(), 0);
        }
        String componentName = device.getDetails().getFriendlyName() + "_" + componentCount.get(device.getDetails().getFriendlyName());
        componentCount.put(device.getDetails().getFriendlyName(), componentCount.get(device.getDetails().getFriendlyName()) + 1);

        ArrayList<OCService> providedServices = new ArrayList<>();
        ArrayList<OCService> requiredServices = new ArrayList<>();


        Service dependencyInjectionService = null;
        for(Service service : device.findServices()){
            if("urn:upnp-org:serviceId:DependencyInjectionService".equals(service.getServiceId().toString())) {
                dependencyInjectionService = service;
            }else{
                providedServices.add(new UpnpProvidedService(
                        service.getServiceId().getId(),
                        service.getServiceId().toString(),
                        componentName,
                        service.getDevice().getIdentity().getUdn().toString()
                ));
            }
        }

        if(dependencyInjectionService != null){
            ActionInvocation actionInvocation = new ActionInvocation(dependencyInjectionService.getAction("GetRequiredServicesDescription"));

            Service finalDependencyInjectionService = dependencyInjectionService;
            UPnPProbe.getUpnpService().getControlPoint().execute(new ActionCallback(actionInvocation) {
                @Override
                public void success(ActionInvocation actionInvocation) {
                    System.out.println(Arrays.toString(actionInvocation.getOutput()));
                    for(String part : ((String)actionInvocation.getOutput("RequiredServiceDescription").getValue()).split(",") ){
                        String serviceName = part.split(" ")[0];
                        String serviceId = part.split(" ")[1];

                        requiredServices.add(new UpnpRequiredService(
                                serviceName,
                                serviceId,
                                componentName,
                                finalDependencyInjectionService.getAction("BindRequiredService")
                        ));
                    }

                    callback.accept(new UpnpMockupComponent(
                            componentName,
                            providedServices,
                            requiredServices,
                            device.getIdentity().getUdn().toString()
                    ));
                }

                @Override
                public void failure(ActionInvocation invocation,
                                    UpnpResponse operation,
                                    String defaultMsg) {
                    System.err.println(defaultMsg);
                }
            });

        } else {
            callback.accept(new UpnpMockupComponent(
                    componentName,
                    providedServices,
                    requiredServices,
                    device.getIdentity().getUdn().toString()
            ));
        }
    }
}
