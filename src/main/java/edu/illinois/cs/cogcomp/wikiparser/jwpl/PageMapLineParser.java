package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 *  This class receives the PageMapLine.txt file as input.  It will
 *  parse the file and create data structures from it.  The results 
 *  will be written to files stored in a folder called page_map_line_output.
 */
public class PageMapLineParser {
    private static List<Integer> curIds = new ArrayList(); // Stores all CurIds, both resolved and unresolved
    public static Set<Integer> resolvedCurIds = new HashSet<Integer>();  // Stores resolved Cur Ids
    private static Set<Integer> listPages = new HashSet<Integer>();  // Stores Cur Ids which are list pages
    public static Map<Integer, Integer> uidToRid = new HashMap(); // Maps unresolved Cur Ids to resolved Cur Ids
    public static Map<Integer, String> curidsToTitles = new HashMap();  // Map from all Cur Ids to page titles 
    private static Map<String, String> uptToRpt = new HashMap(); // Maps unresolved page titles to resolved page titles
    
    private void mapUnresolvedToResolved(){
        for(Integer id : curIds){
            String unresolvedPageTitle = null;
            String resolvedPageTitle = null;
            Integer resolvedId = null;
            if(uidToRid.containsKey(id)) resolvedId = uidToRid.get(id);
            if(curidsToTitles.containsKey(id)) unresolvedPageTitle = curidsToTitles.get(id); 
            if(resolvedId != null && curidsToTitles.containsKey(resolvedId)) resolvedPageTitle = curidsToTitles.get(resolvedId);
            if(unresolvedPageTitle != null && resolvedPageTitle != null){
                uptToRpt.put(unresolvedPageTitle, resolvedPageTitle);
            }
        }
    }
    
    private void writeToFiles(){
        String projectFolder = System.getProperty("user.dir");
        String dataFolder = projectFolder + "\\page_map_line_output";
        File directory = new File(dataFolder);
        if(! directory.exists()){
            directory.mkdir();
        }
        int count = 0;
        // Writes list of all Cur Ids
        File file = new File(dataFolder + "\\" + "pageIds.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : curIds){
                count++;
                bw.write(id.toString());
                if(count != curIds.size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Writes list of resolved Cur Ids
        count = 0;
        file = new File(dataFolder + "\\" + "resolvedPageIds.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resolvedCurIds){
                count++;
                bw.write(id.toString());
                if(count != resolvedCurIds.size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Writes map from Cur Ids (first column) to page titles (second column)
        count = 0;
        file = new File(dataFolder + "\\" + "curIds2Titles.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : curidsToTitles.keySet()){
                count++;
                bw.write(id.toString() + "\t" + curidsToTitles.get(id));
                if(count != curidsToTitles.keySet().size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Writes map from page titles (first column) to resolved page titles (second column)
        count = 0;
        file = new File(dataFolder + "\\" + "unresTitles2resTitles.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String title : uptToRpt.keySet()){
                count++;
                bw.write(title + "\t" + uptToRpt.get(title));
                if(count != uptToRpt.keySet().size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Writes list of Cur Ids which belong to list pages
        count = 0;
        file = new File(dataFolder + "\\" + "resListPages.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : listPages){
                count++;
                bw.write(id.toString());
                if(count != listPages.size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
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
            writeToFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
