/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Logger;

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

    public static void init() {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssSSS");
            String sDate = dateFormat.format(date);
            String fileName = "MyLog_" + sDate + ".log";
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
    }

    public static void close() {
        try {
            fh.close();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
