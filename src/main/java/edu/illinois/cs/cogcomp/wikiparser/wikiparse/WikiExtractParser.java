package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

import edu.illinois.cs.cogcomp.wikiparser.utils.ParserLogger;
import edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.ResolveHyperlinks;
import edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.KB;

/**
 * This class receives as input the output of the python parser which is a folder of folder
 * of wiki text files.  It reads each text file in the directory and parses them into
 * serialized lists of WikiPage objects
 */
public class WikiExtractParser {
    private ParserLogger logger = new ParserLogger("WikiExtractParser");
    public String logfile = System.getProperty("user.dir") + "/logs/ExtractedWiki.log";
    public String wikiDirectory;
    public String outputDir;
    private FileHandler fh;
    private ThreadPoolExecutor parser = null;

    public WikiExtractParser() {
        parser = getBoundedThreadPool();
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
                                        new ThreadPoolExecutor.CallerRunsPolicy()// Blocking mechanismlogger
        );
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public void extractWiki() {
        // Dir path to parsed Wikipedia. This dir contains multiple nested dirs with multiple files.
        File inDir = new File(wikiDirectory);
        // This dir will replicate the dir/file structure in 'inDir'.
	    Iterator<File> i = org.apache.commons.io.FileUtils.iterateFiles(inDir, null, true);
        JsonConverter.setOutputDir(this.outputDir);
        int totalFiles = 0;
        logger.log.info("Starting to Parse Wiki Texts");
        // Reads all of the files in the given directory
        while(i.hasNext()){
            totalFiles ++;
            File file = i.next();
            String infilepath = file.toString();
            // String outfilepath = outputDir + "/tmp" + Integer.toString(totalFiles) + ".ser";
            logger.log.info("Parsing Wiki Text " + Integer.toString(totalFiles));
            // Give this to thread runner
            parser.execute(new FileParser(infilepath, logger.log, new ResolveHyperlinks()));
        }

        logger.log.info("Total Files: " + Integer.toString(totalFiles));
        System.out.println("[#] Total Files: " + totalFiles);
        while (parser.getActiveCount() > 0) {
          // wait
        }
        JsonConverter.closeFile();
        // ResolveHyperlinks.writeUnresolvedTitles();
    }

    public static void main(String [] args) {

        String unifiedParserOutputDir = args[0];
        KB.setFilePaths(unifiedParserOutputDir);
        KB.loadCurIdsMap();
        KB.loadResolvedCurIdsMap();
        KB.loadRedirectTitle2ResolvedTitleMap();
        // System.exit(0);

        WikiExtractParser wikiparser = new WikiExtractParser();
        wikiparser.wikiDirectory = args[1];   // Dir contain wikiextrator output
        wikiparser.outputDir = args[2];   // Output Dir for json output
        wikiparser.extractWiki();

    }
}
