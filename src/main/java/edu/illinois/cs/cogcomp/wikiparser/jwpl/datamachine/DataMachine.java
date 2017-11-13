/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.illinois.cs.cogcomp.wikiparser.jwpl.datamachine;

import de.tudarmstadt.ukp.wikipedia.datamachine.domain.DataMachineFiles;
import de.tudarmstadt.ukp.wikipedia.datamachine.domain.JWPLDataMachine;
import de.tudarmstadt.ukp.wikipedia.wikimachine.debug.ILogger;
import de.tudarmstadt.ukp.wikipedia.wikimachine.domain.Configuration;
import de.tudarmstadt.ukp.wikipedia.wikimachine.domain.ISnapshotGenerator;
import de.tudarmstadt.ukp.wikipedia.wikimachine.factory.IEnvironmentFactory;
import de.tudarmstadt.ukp.wikipedia.wikimachine.factory.SpringFactory;
import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;
import java.io.File;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import edu.illinois.cs.cogcomp.wikiparser.utils.ParserLogger;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
 
/**
 * This class provides the runDM function that is used to call the JWPL DataMachine function.
 */
public class DataMachine {
    private static final long startTime = System.currentTimeMillis();
    private static final IEnvironmentFactory environmentFactory = SpringFactory.getInstance();

    public static void main(String [] args){
        System.out.println("Running JWPL DataMachine parser to generate 11 output files");
        String jwplInputDir = args[0];
        String jwplOutputDir = Paths.get(jwplInputDir, "output").toString();
        File f = new File(jwplOutputDir);
        ParserLogger logger = new ParserLogger("DataMachine");

        System.out.println("Wiki Dump Files Dir: " + jwplInputDir);
        System.out.println("JWPL Output Dir: " + jwplOutputDir);

        if(!f.exists()){  // Runs DataMachine if output folder does not exist
           try{
               logger.log.info("Running JWPL DataMachine");
               runDM(jwplInputDir);
           } catch(Exception e){
               logger.log.severe("Exception: " + e.toString());
           }
        } else {
            System.out.println("Output folder already exists.");
        }
        logger.log.info("DataMachine done");
    }
    
    public static void runDM(String jwplInputDir) throws Exception {
        System.setProperty("jdk.xml.totalEntitySizeLimit", "500000000");
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
        // Path where wiki dump is stored.
        String[] args = {"english", "Contents", "Disambiguation_pages", jwplInputDir};
        System.out.println("Running JWPL Datamachine");
        JWPLDataMachine.main(args);
    }

}
