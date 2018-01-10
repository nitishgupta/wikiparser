package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import edu.illinois.cs.cogcomp.wikiparser.utils.FileUtils;
import edu.illinois.cs.cogcomp.wikiparser.constants.WikiparseConstants;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import edu.illinois.cs.cogcomp.wikiparser.utils.ParserLogger;
import static edu.illinois.cs.cogcomp.wikiparser.wikiparse.WikiExtractParser.getBoundedThreadPool;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class PageIdTitleParser implements Runnable{
    private Logger logger;
    private FileHandler fh;
    private String outputDir;
    private String inputDir;
    private Map<Integer, String> idToTitleMap;
    
    public PageIdTitleParser(String inputDir, String outputDir, Logger logger){
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.logger = logger;
        idToTitleMap = new HashMap();
    }
    
    public static ThreadPoolExecutor getBoundedThreadPool() {
        /*
            Manages a fixed number of WikiParser threads
        */
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
    
    private static String decodeTitle(String title) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(title, "UTF-8");
    } 
    
    public void ParseDoc(String doc){
        /*
         * This function parses the document and and gets the page Id and page Title
        */
        logger.info("Parse doc");
        if (!doc.trim().isEmpty()){
            String [] lines = doc.trim().split("\n");
            if(lines.length == 0){
                logger.severe("Document is empty!");
                return;
            }
            assert (lines[0].startsWith("<doc id=")); // Line 1 should be <doc ... >
            
            // Gets Wikititle
            String title = lines[0].split("title=\"")[1];
            String wikiTitle = title.substring(0,title.length()-2); // Removes extra characters
            try{
                wikiTitle = decodeTitle(wikiTitle);
            } catch(Exception e){
                logger.severe("Input : " + wikiTitle);
                logger.severe("Exception: " + e.toString());
            }
            wikiTitle = wikiTitle.replaceAll(" ", "_");
            if (wikiTitle.startsWith("List_of") || wikiTitle.startsWith("Lists_of")) return;
            
            // Gets curID
            String firstLine = lines[0].split("curid=")[1];
            int index = firstLine.indexOf("\"");
            String id = firstLine.substring(0, index);
            int curId = Integer.parseInt(id);
            
            // Gets page title
            String pageTitle = lines[1];
            pageTitle = pageTitle.replaceAll(" ", "_");
            if(!idToTitleMap.containsKey(curId)){
                idToTitleMap.put(curId, pageTitle);
            }
        }
    }
    
    public void writeToFiles(){
        // Writes id to title map to file
        Path filePath = Paths.get(outputDir, WikiparseConstants.PageIdTitleOutput);
        File file = new File(filePath.toString());
        logger.info("Writing to output file");
            try{
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for(Integer id : idToTitleMap.keySet()){
                    String PageTitle = idToTitleMap.get(id);     
                    bw.write(id.toString() + "\t" + PageTitle + "\n"); // Each page exists on a new line
                }
                bw.close();
        }
        catch (IOException e){
            logger.severe(e.toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public void MapPageIdToTitle(){
        // Dir path to parsed Wikipedia. This dir contains multiple nested dirs with multiple files.
        File inDir = new File(inputDir);
        // This dir will replicate the dir/file structure in 'inDir'.
	Iterator<File> i = org.apache.commons.io.FileUtils.iterateFiles(inDir, null, true);
        
        int totalFiles = 0;
        logger.info("Starting to Parse Wiki Texts");
        // Reads all of the files in the given directory
        while(i.hasNext()){
            totalFiles ++;
            File file = i.next();
            String infilepath = file.toString();
            logger.info("Parsing Wiki Text " + Integer.toString(totalFiles));
            String [] docs = FileParser.breakDocs(infilepath);
            if(docs == null){
                logger.severe("There are no files to be parsed");
                continue;
            }
            for(int idx = 0; idx < docs.length; idx++){
                String doc = docs[idx];
                ParseDoc(doc);
            }
        }
        logger.info("Total Files: " + Integer.toString(totalFiles));
        
        // Writing to file
        writeToFiles();
    }
    
    public void run(){
        logger.info(("Generating Map of Page Ids to Page Titles"));
        try{
            MapPageIdToTitle();
        }catch(Exception e){
            logger.severe("Exception: " + e.toString());
        }
    }
    
    public static void main(String [] args){
        ThreadPoolExecutor parser = getBoundedThreadPool();
        String inputDir = args[0], outputDir = args[1];
        ParserLogger logger = new ParserLogger("PageIdTitleParser");
        parser.execute(new PageIdTitleParser(inputDir, outputDir, logger.log));
        logger.log.info("Finished generating page id to page title map");
    }
}
