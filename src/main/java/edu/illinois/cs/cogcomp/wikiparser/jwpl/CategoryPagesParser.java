package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Reuben-PC
 */
public class CategoryPagesParser {
    private String outputDir;
    private static Map<Integer, Set<String>> resIdToTitle = new HashMap(); // Maps from resolved Cur Ids to page titles
    private static Set<Integer> disambPageIds = new HashSet(); // Stores resolved Ids of disambiguation pages
    private static Set<Integer> nondisambPageIds = new HashSet(); // Stores resolved Ids of non-disambiguation pages
    private static Set<String> pageTitlesDisamb = new HashSet(); // Stores unresolved Page Titles of disambiguation pages
    private static Set<String> pageTitlesNondisamb = new HashSet(); // Stores unresolved Page Titles of non-disambiguation pages

    public CategoryPagesParser(String outputDir){
        this.outputDir = outputDir;
    }
    
    // Writes map from resolved cur ids to set of category titles
    private void writeResIdsToCatTitles(){
        Path filePath = Paths.get(outputDir, JWPLConstants.resIdsToCatTitles);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resIdToTitle.keySet()){
                bw.write(id.toString());
                for(String title : resIdToTitle.get(id)){
                    bw.write("\t" + title);
                }
                bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Writes list of resolved cur ids of disambiguation pages
    private void writeResIdsDisambPages(){
        Path filePath = Paths.get(outputDir, JWPLConstants.resDisambPageIds);
        File file = new File(filePath.toString());
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : disambPageIds){
                bw.write(id.toString() + "\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    // Writes list of resolved cur ids of non-disambiguation pages
    private void writeResIdsNonDisambPage(){
        Path filePath = Paths.get(outputDir, JWPLConstants.resNonDisambPageIds);
        File file = new File(filePath.toString());
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : nondisambPageIds){
                bw.write(id.toString() + "\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private void writeToFiles(){
      writeResIdsToCatTitles();
      writeResIdsDisambPages();
      writeResIdsNonDisambPage();
    }

    public void parseCategoryPages(String CategoryPages){
        try {
            File file = new File(CategoryPages);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String [] args = line.split("\t");
                Integer curId = Integer.parseInt(args[1]);
                Integer catId = Integer.parseInt(args[0]);
                Integer resCurId = PageMapLineParser.uidToRid.get(curId);

                // Maps resolved cur id to set of category titles
                if(!resIdToTitle.containsKey(resCurId)){
                    Set<String> categories = new HashSet();
                    categories.add(CategoryParser.idToCat.get(catId));
                    resIdToTitle.put(resCurId, categories);
                }
                else{
                    resIdToTitle.get(resCurId).add(CategoryParser.idToCat.get(catId));
                }

                if(CategoryParser.idToDisambCat.containsKey(catId)){
                    disambPageIds.add(resCurId); // Creates list of resolved Cur Ids of disambiguation pages
                    pageTitlesDisamb.add(PageMapLineParser.curidsToTitles.get(curId)); // Creates list of unresolved page titles of disambiguation pages
                } else{
                    // Creates list of unresolved page titles of non-disambiguation pages
                    pageTitlesNondisamb.add(PageMapLineParser.curidsToTitles.get(curId));
                }
            }
            // Creates list of resolved Cur Ids of non-disambiguation pages
            nondisambPageIds.addAll(PageMapLineParser.resolvedCurIds);
            nondisambPageIds.removeAll(disambPageIds);
            
            // Check intersection of resDisambPageIds and resNonDisambPageIds
            Set<Integer> intersection = new HashSet<Integer>(disambPageIds);
            intersection.retainAll(nondisambPageIds);
            assert(intersection.isEmpty());  // Check that the intersection of both is null
            
            // Check union of resDisambPageIds and resNonDisambPageIds;
            Set<Integer> union = new HashSet<Integer>(disambPageIds);
            union.addAll(nondisambPageIds);
            assert(union.equals(PageMapLineParser.resolvedCurIds)); // Check that the union of both is equal to the set of all resolved page ids

            fileReader.close();
            writeToFiles();  // Writes outputs to files
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
