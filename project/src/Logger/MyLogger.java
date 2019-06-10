/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Logger;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static FileHandler fh = null;
    private static StringProperty uiLog;

    public MyLogger(){
        this.uiLog = new SimpleStringProperty("");
    }

    public static void init() {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssSSS");
            String sDate = dateFormat.format(date);
            String fileName = "MyLogFiles/MyLog_" + sDate + ".log";
            fh = new FileHandler(fileName, false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger l = Logger.getLogger("");
        fh.setFormatter(new SimpleFormatter());
        l.addHandler(fh);
        l.setLevel(Level.CONFIG);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
        //uiLog.set(getStrLog()+" "+level.toString()+" "+message+"\n"); //ToDo walid : quand je met boucle d'ordonnancement infinie -> outofmemoryProblem
    }

    public static String getStrLog(){
        return uiLog.getValue();
    }

    public StringProperty uiLogProperty() {
        return uiLog;
    }

    public static void close() {
        try {
            fh.close();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
