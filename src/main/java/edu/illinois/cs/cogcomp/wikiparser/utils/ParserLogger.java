/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.illinois.cs.cogcomp.wikiparser.utils;

import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;

/**
 *
 * @author Reuben-PC
 */
public class ParserLogger {
    public Logger log;
    private FileHandler fh;
    
    public ParserLogger(String className){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        log = Logger.getLogger(className);
        String filename = "/logs/" + className + "_" + timeStamp + ".log";
        
        // Checks if logs directory exists.  If not, create it
        Path filePath = Paths.get(System.getProperty("user.dir"), "logs");
        File logDir = new File(filePath.toString());
        if(!logDir.exists()){
            logDir.mkdir();
        }
        
        filePath = Paths.get(System.getProperty("user.dir"), filename);
        File f = new File(filePath.toString());
        try {
            fh = new FileHandler(filePath.toString());
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            log.setUseParentHandlers(false);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
