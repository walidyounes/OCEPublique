
package UPnPEnvironment;

import UI.UIMockupController;
import javafx.application.Platform;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ProbeRegistryListener extends DefaultRegistryListener {

    Map<String,UpnpMockupComponent> knownComponents = new HashMap<>();

    private UIMockupController uiMockupController;

    public ProbeRegistryListener(UIMockupController uiMockupController){
        this.uiMockupController = uiMockupController;
    }

    private void deviceAdded(Device device){
        if (knownComponents.keySet().contains(device.getIdentity().getUdn().toString())) {
            System.out.println(
                    "Device re-added: " + displayStringOfDevice(device)
            );
            UpnpMockupComponent component = knownComponents.get(device.getIdentity().getUdn().toString());

            Platform.runLater(() -> uiMockupController.addComponentToEnv(component));
        } else {
            System.out.println(
                    "Device added: " + displayStringOfDevice(device)
            );

            UpnpComponentFactory.makeUpnpComponent(device, component -> {
                knownComponents.put(device.getIdentity().getUdn().toString(), component);
                Platform.runLater(() -> uiMockupController.addComponentToEnv(component));
            });

        }
    }

    private void deviceRemoved(Device device){
        if (knownComponents.keySet().contains(device.getIdentity().getUdn().toString())) {
            System.out.println(
                    "Device removed: " + device.getDisplayString()
            );

            Platform.runLater(() -> uiMockupController.removeComponentFromEnv(knownComponents.get(device.getIdentity().getUdn().toString())));
        }
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    private static String displayStringOfDevice(Device device){
        String display = device.getDisplayString();
        if(device instanceof RemoteDevice){
            display += ", " + ((RemoteDevice)device).getIdentity().getDescriptorURL();
        }

        return display;
    }

    public Map<String, UpnpMockupComponent> getKnownComponents() {
        return knownComponents;
    }
}
