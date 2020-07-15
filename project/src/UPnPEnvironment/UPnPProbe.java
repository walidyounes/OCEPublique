
package UPnPEnvironment;

import UI.UPnPMockupController;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.RemoteDevice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URLConnection;

public class UPnPProbe implements Runnable {

    private static UpnpService upnpService;

    private ProbeRegistryListener probeRegistryListener;

    public UPnPProbe(UPnPMockupController upnpMockupController){
        this.probeRegistryListener = new ProbeRegistryListener(upnpMockupController);
    }

    public void run() {
        try {
            upnpService = new UpnpServiceImpl();

            upnpService.getRegistry().addListener(probeRegistryListener);

            Timer t = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    upnpService.getControlPoint().search(3);
                    for(RemoteDevice remoteDevice : upnpService.getRegistry().getRemoteDevices()) {
                        try {
                            URLConnection connection = remoteDevice.getIdentity().getDescriptorURL().openConnection();
                            connection.getContent();
                        } catch (IOException e) {
                            upnpService.getRegistry().removeDevice(remoteDevice);
                        }
                    }
                }
            });
            t.start();

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
        }
    }

    public static UpnpService getUpnpService(){
        return upnpService;
    }
}
