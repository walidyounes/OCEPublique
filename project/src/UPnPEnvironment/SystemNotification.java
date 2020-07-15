package UPnPEnvironment;

import java.awt.*;

public class SystemNotification {

    private SystemTray systemTray;

    private TrayIcon trayIcon;

    public SystemNotification() throws AWTException {
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

            trayIcon = new TrayIcon(image, "OCE Info");
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);

            trayIcon.setToolTip("OCE Info");
            systemTray.add(trayIcon);

        } else {
            System.out.println("Notification not supported");
        }
    }

    public void displayMessage(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }

    public void shutdown() {
        systemTray.remove(trayIcon);
    }
}
