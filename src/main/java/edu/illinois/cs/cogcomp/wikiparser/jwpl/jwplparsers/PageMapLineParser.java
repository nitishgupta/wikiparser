package edu.illinois.cs.cogcomp.wikiparser.jwpl.jwplparsers;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;
import edu.illinois.cs.cogcomp.wikiparser.utils.ParserLogger;

/**
 *  This class receives the PageMapLine.txt file as input.  It will
 *  parse the file and create data structures from it.  The results
 *  will be written to files stored in a folder called page_map_line_output.
 */
public class PageMapLineParser {
    public String outputDir;
    public static Set<Integer> curIds; // Stores all CurIds, both resolved and unresolved
    public static Set<Integer> resolvedCurIds;  // Stores resolved Cur Ids
    public static Set<Integer> listPages;  // Stores Cur Ids which are list pages
    public static Map<Integer, Integer> uidToRid; // Maps unresolved Cur Ids to resolved Cur Ids
    public static Map<Integer, String> curidsToTitles;  // Map from all Cur Ids to page titles
    public static Map<Integer, String> resCurIdsToTitles;  // Map from all Cur Ids to page titles
    public static Map<Integer, String> resCurIdsToTitles_nonList; // Map from all Cur Ids to page titles
    public static Map<Integer, List<Integer>> resCurId2redirects;
    private ParserLogger logger; 
    
    public PageMapLineParser(String outputDir, ParserLogger logger){
        this.outputDir = outputDir;
        this.logger = logger;
        System.out.println("PageMapLine Parser");
        System.out.println("Parses PageMapLine.txt to produce:");
        System.out.println("[1] CurIds: " + JWPLConstants.pageIds);
        System.out.println("[2] Resolved CurIds: " + JWPLConstants.resPageIds);
        System.out.println("[3] CurIds -> Tiles: "+ JWPLConstants.pageIdsToTitles);
        System.out.println("[4] Unresolved Titles -> Resolved Tiles: "+ JWPLConstants.unresTitlesToResTitles);
        System.out.println("[5] Resolved List Page Ids: "+ JWPLConstants.resListPages);

        System.out.println("[#] Output Folder: " + outputDir);
        logger.log.info("Parsing PageMapLine.txt");
        curIds = new HashSet(); // Stores all CurIds, both resolved and unresolved
        resolvedCurIds = new HashSet<Integer>();  // Stores resolved Cur Ids
        listPages = new HashSet<Integer>();  // Stores Cur Ids which are list pages
        uidToRid = new HashMap(); // Maps unresolved Cur Ids to resolved Cur Ids
        curidsToTitles = new HashMap();  // Map from all Cur Ids to page titles
        resCurIdsToTitles = new HashMap();  // Map from all Cur Ids to page titles
        resCurIdsToTitles_nonList = new HashMap();  // Map from all Cur Ids to page titles
        resCurId2redirects = new HashMap();
    }

    private void writeCurId2Title(){
        // Writes map from page ids to page titles
        logger.log.info("Writes map from page ids to page titles");
        Path filePath = Paths.get(outputDir, JWPLConstants.curId2Title);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : curidsToTitles.keySet()){
                bw.write(id.toString() + "\t" + curidsToTitles.get(id) + "\n"); // Each page exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            logger.log.severe(e.toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeResCurId2ResTitle(){
        // Writes map from page ids to page titles
        logger.log.info("Writes map from page ids to page titles");
        Path filePath = Paths.get(outputDir, JWPLConstants.resCurId2ResTitle);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resCurIdsToTitles.keySet()){
                bw.write(id.toString() + "\t" + resCurIdsToTitles.get(id) + "\n"); // Each page exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            logger.log.severe(e.toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeResCurId2ResTitle_nonList() {
        // Writes map from page ids to page titles
        logger.log.info("Writes map from page ids to page titles");
        Path filePath = Paths.get(outputDir, JWPLConstants.resCurId2ResTitle_nonList);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resCurIdsToTitles_nonList.keySet()){
                bw.write(id.toString() + "\t" + resCurIdsToTitles.get(id) + "\n"); // Each page exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            logger.log.severe(e.toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeListPageIdsTitle(){
        // Writes list of ids which belong to list pages
        logger.log.info("Writes list of ids which belong to list pages");
        Path filePath = Paths.get(outputDir, JWPLConstants.resListPageCurId2ResTitle);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : listPages){
                String title = resCurIdsToTitles.get(id);
                bw.write(id + "\t" + title + "\n");
            }
            bw.close();
        }
        catch (IOException e){
            logger.log.severe(e.toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeResCurId2Redirects() {
        logger.log.info("Writes Resolved CurID to Redirect CurIDs map.");
        Path filePath = Paths.get(outputDir, JWPLConstants.resCurId2Redirects);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resCurId2redirects.keySet()){
                List<Integer> redirectIds = resCurId2redirects.get(id);
                StringBuffer redirects = new StringBuffer();
                for (Integer redid : redirectIds) {
                    redirects.append(redid).append(" ");
                }
                String redirectString = redirects.toString().trim();


                bw.write(id + "\t" + redirectString + "\n");
            }
            bw.close();
        }
        catch (IOException e){
            logger.log.severe(e.toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeToFiles(){
        writeCurId2Title();
        writeResCurId2ResTitle();
        writeResCurId2ResTitle_nonList();
        writeListPageIdsTitle();
        writeResCurId2Redirects();
    }

    private void mapUnresolvedToResolved(){
        for(Integer id : curIds){
            assert (uidToRid.containsKey(id));
            Integer resolvedId = uidToRid.get(id);

            String resolvedPageTitle = curidsToTitles.get(resolvedId);   // Check if resolved page ids exists
            resCurIdsToTitles.put(resolvedId, resolvedPageTitle);

            if (!listPages.contains(resolvedId)) {
                resCurIdsToTitles_nonList.put(resolvedId, resolvedPageTitle);
            }

            if (uidToRid.get(id) != id) {
                Integer rid = uidToRid.get(id);
                Integer redirect = id;
                if (!resCurId2redirects.containsKey(rid)) {
                    resCurId2redirects.put(rid, new ArrayList<Integer>());
                }
                resCurId2redirects.get(rid).add(redirect);
            }
        }
    }

    public void parsePageMap(String pageMapFile){
        try {
            File file = new File(pageMapFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String [] args = line.split("\t");
                Integer id = Integer.parseInt(args[0]);
                Integer resolvedId = Integer.parseInt(args[2]);
                String pageTitle = args[1];
                pageTitle = pageTitle.replace("\\", "");  // Removes escape character '\'
                pageTitle = pageTitle.trim(); // Removes trailing and leading space
                if(pageTitle.isEmpty()) continue;
                if (pageTitle.startsWith("List_of") || pageTitle.startsWith("Lists_of")){
                    listPages.add(resolvedId);  // Only add resolved pages
                }
                curIds.add(id);  // Adds unresolved Cur Ids
                resolvedCurIds.add(resolvedId); // Adds resolved Cur Ids to set
                uidToRid.put(id, resolvedId);
                if(!curidsToTitles.containsKey(id)){  // Maps all Cur Ids to Page Titles
                    curidsToTitles.put(id, pageTitle);
                } else {
                    System.out.println("curidsToTitles already contains duplicate curid: " + id);
                    System.exit(0);
                }
            }
            fileReader.close();
            mapUnresolvedToResolved();
            writeToFiles();  // Writes outputs to files
            logger.log.info("Parsing of PageMapLine.txt done");
        } catch (IOException e) {
            logger.log.severe(e.toString());
            e.printStackTrace();
        }
    }
}
