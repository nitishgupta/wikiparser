package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Reuben-PC
 * 
 * This class receives as input the output of the python parser which is a folder of folder 
 * of wiki text files.  It reads each text file in the directory and parses them into
 * serialized lists of WikiPage objects
 */
public class WikiExtractParser {
    private Logger logger = Logger.getLogger("WikiExtractParser");
    public String logfile = System.getProperty("user.dir") + "/logs/ExtractedWiki.log";
    public static String wikiDirectory;
    public static String outputFile;
    private FileHandler fh;
    private ThreadPoolExecutor parser = null;
    
    public WikiExtractParser() {
        parser = getBoundedThreadPool();
        System.out.println(logfile);
        try {
                // This block configure the logger with handler and formatter
                File dir = new File("logs");
                dir.mkdir();
                fh = new FileHandler(logfile);
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                logger.setUseParentHandlers(false);
                // the following statement is used to log any messages
                logger.info("Static Function");
        } catch (SecurityException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    // Manages a fixed number of WikiParser threads
    public static ThreadPoolExecutor getBoundedThreadPool() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        coreCount = Math.max(coreCount, 10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                                        coreCount, // Core count
                                        coreCount, // Pool Max
                                        60, TimeUnit.SECONDS, // Thread keep alive time
                                        new ArrayBlockingQueue<Runnable>(coreCount),// Queue
                                        new ThreadPoolExecutor.CallerRunsPolicy()// Blocking mechanism
        );
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
    
    public void extractWiki(){
        // Dir path to parsed Wikipedia. This dir contains multiple nested dirs with multiple files.
        File inDir = new File(wikiDirectory);
        // This dir will replicate the dir/file structure in 'inDir'.
	Iterator<File> i = org.apache.commons.io.FileUtils.iterateFiles(inDir, null, true);
        
        int totalFiles = 0;
        
        // Reads all of the files in the given directory
        while(i.hasNext()){
            totalFiles ++;
            File file = i.next();
            String infilepath = file.toString();
            String outfilepath = outputFile + "/tmp" + Integer.toString(totalFiles) + ".ser";
            
            // Give this to thread runner
            parser.execute(new WikiParser(infilepath, outfilepath, logger));
        }
        
        System.out.println("[#] Total Files: " + totalFiles);
    }
    
    public static void main(String [] args){
        wikiDirectory = args[0];
        outputFile = args[1];
        WikiExtractParser wikiparser = new WikiExtractParser();
        wikiparser.logger.info("Starting to Parse Wiki Texts");
        wikiparser.extractWiki();
    }
}
