package edu.illinois.cs.cogcomp.wikiparser.jwpl;

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

/**
 *  This class receives the PageMapLine.txt file as input.  It will
 *  parse the file and create data structures from it.  The results
 *  will be written to files stored in a folder called page_map_line_output.
 */
public class PageMapLineParser {
    private String outputDir;
    private static Set<Integer> curIds = new HashSet(); // Stores all CurIds, both resolved and unresolved
    public static Set<Integer> resolvedCurIds = new HashSet<Integer>();  // Stores resolved Cur Ids
    private static Set<Integer> listPages = new HashSet<Integer>();  // Stores Cur Ids which are list pages
    public static Map<Integer, Integer> uidToRid = new HashMap(); // Maps unresolved Cur Ids to resolved Cur Ids
    public static Map<Integer, String> curidsToTitles = new HashMap();  // Map from all Cur Ids to page titles
    private static Map<String, String> uptToRpt = new HashMap(); // Maps unresolved page titles to resolved page titles
    
    public PageMapLineParser(String outputDir){
        this.outputDir = outputDir;
    }

    private void mapUnresolvedToResolved(){
        for(Integer id : curIds){
            String unresolvedPageTitle = null;
            String resolvedPageTitle = null;
            Integer resolvedId = null;
            if(uidToRid.containsKey(id)) resolvedId = uidToRid.get(id);
            if(curidsToTitles.containsKey(id)) unresolvedPageTitle = curidsToTitles.get(id);
            if(resolvedId != null && curidsToTitles.containsKey(resolvedId)) resolvedPageTitle = curidsToTitles.get(resolvedId); // Check if resolved page ids exists
            if(unresolvedPageTitle != null && resolvedPageTitle != null){  // Maps unresolved page titles to resolved page titles
                uptToRpt.put(unresolvedPageTitle, resolvedPageTitle);
            }
        }
    }
    
    // Write list of all page ids
    private void writePageIds(){
        Path filePath = Paths.get(outputDir, JWPLConstants.pageIds);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : curIds){
                bw.write(id.toString() + "\n");  // Each id exists on new line
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Writes list of resolved page ids
    private void writeResPageIds(){
        Path filePath = Paths.get(outputDir, JWPLConstants.resPageIds);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resolvedCurIds){
                bw.write(id.toString() + "\n");  // Each id exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Writes map from page ids to page titles
    private void writePageIdsToTitles(){
        Path filePath = Paths.get(outputDir, JWPLConstants.pageIdsToTitles);
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
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Writes map from unresolved page titles (first column) to resolved page titles (second column)
    private void writePageTitlesToResTitles(){
        Path filePath = Paths.get(outputDir, JWPLConstants.unresTitlesToResTitles);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String title : uptToRpt.keySet()){
                bw.write(title + "\t" + uptToRpt.get(title) + "\n");  // Each title exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Writes list of ids which belong to list pages
    private void writeListPageIds(){
        Path filePath = Paths.get(outputDir, JWPLConstants.resListPages);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : listPages){
                bw.write(id.toString() + "\n");  // Each id exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeToFiles(){
        writePageIds();
        writeResPageIds();
        writePageIdsToTitles();
        writePageTitlesToResTitles();
        writeListPageIds();
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
                    if(id.equals(resolvedId)) listPages.add(resolvedId);  // Only add resolved pages
                }
                curIds.add(id);  // Adds unresolved Cur Ids
                resolvedCurIds.add(resolvedId); // Adds resolved Cur Ids to set
                uidToRid.put(id, resolvedId);
                if(!curidsToTitles.containsKey(id)){  // Maps all Cur Ids to Page Titles
                    curidsToTitles.put(id, pageTitle);
                }
            }
            fileReader.close();
            mapUnresolvedToResolved();
            writeToFiles();  // Writes outputs to files
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
