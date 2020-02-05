/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package MOICE;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;

public class MOICEProbe {
    private long  periodicity;                     //Periodicity of the task of probing
    private boolean fileFound;                      //Boolean value to indicate whether if the file send by ICe was found
    private PropertyChangeSupport fileReceived;    // Variable used to inform the listener of the changes in the variable "fileFound"

    /**
     * Constructor of the class
     */
    public MOICEProbe() {
        this.periodicity = 3000;
        this.fileFound = false;
        this.fileReceived = new PropertyChangeSupport(this);
    }

    class ScheduledProbing extends TimerTask {
        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
                File fileICE = new File("ICEConfiguration\\ICEConfiguration.ice_editor");
                //Mark the file as found and notify the observer
                if(fileICE.exists()){
                    System.out.println("File Found");
                    if(!fileFound) {
                        fileReceived.firePropertyChange("FileICEFound", fileFound, true);
                        fileFound = true;
                    }

                }else{
                    System.out.println("File Not Found");
                    //Mark the file as not found
                    fileFound = false;
                }
        }
    }

    /**
     * This thread call the ScheduledProbing task periodically to probe the storage device in MOICE for the existence of the file supposed to be send by ICE
     */
    private Thread t = new Thread() {
        @Override
        public void run() {
            Timer time = new Timer(); // Instantiate Timer Object
            // Instantiate ScheduledTask class
            ScheduledProbing st = new ScheduledProbing();
            // Create Repetitively task for every "Periodicity == 30s" secs with a delay 0 secs between two executions
            time.schedule(st, 0, periodicity);
           // OCELogger.log(Level.INFO, "*************************** Probing *******************");
        }
    };

    /**
     * Change the value of the periodicity of the sounding
     * @param periodicity : the new periodicity value
     */
    public void setPeriodicity(long periodicity) {
        this.periodicity = periodicity;
    }

    /**
     * Run the thread responsible of probing the device storage in MOICE
     */
    public void run() {
        t.start();
    }

    /**
     * Add a listener for the property change
     * @param listener  : the listener for the change property
     */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.fileReceived.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener for the property change
     * @param listener  : the listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        //we don't check if the listener exists because the function does it internally
        this.fileReceived.removePropertyChangeListener(listener);
    }
}
