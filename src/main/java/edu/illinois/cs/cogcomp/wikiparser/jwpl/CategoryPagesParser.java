package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Reuben-PC
 */
public class CategoryPagesParser {
    private static Map<Integer, Set<String>> resIdToTitle = new HashMap(); // Maps from resolved Cur Ids to page titles
    private static Set<Integer> disambPageIds = new HashSet(); // Stores resolved Ids of disambiguation pages
    private static Set<Integer> nondisambPageIds = new HashSet(); // Stores resolved Ids of non-disambiguation pages
    private static Set<String> pageTitlesDisamb = new HashSet(); // Stores unresolved Page Titles of disambiguation pages
    private static Set<String> pageTitlesNondisamb = new HashSet(); // Stores unresolved Page Titles of non-disambiguation pages

    private void writeToFiles(){
        String projectFolder = System.getProperty("user.dir");
        String dataFolder = projectFolder + "/category_pages_output";
        File directory = new File(dataFolder);
        if(! directory.exists()){
            directory.mkdir();
        }
        int count = 0;

        // Writes map from resolved cur ids to set of category titles
        File file = new File(dataFolder + "/" + "resId2CatTitles.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resIdToTitle.keySet()){
                count++;
                bw.write(id.toString());
                for(String title : resIdToTitle.get(id)){
                    bw.write("\t" + title);
                }
                if(count != resIdToTitle.keySet().size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        // Writes list of resolved cur ids of disambiguation pages
        count = 0;
        file = new File(dataFolder + "/" + "resIdDisamb.txt");
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : disambPageIds){
                count++;
                bw.write(id.toString());
                if(count != disambPageIds.size()) bw.write("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        // Writes list of resolved cur ids of non-disambiguation pages
        count = 0;
        file = new File(dataFolder + "/" + "resIdNonDisamb.txt");
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : nondisambPageIds){
                count++;
                bw.write(id.toString());
                if(count != nondisambPageIds.size()) bw.write("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        // Writes list of unresolved titles of disambiguation pages
        count = 0;
        file = new File(dataFolder + "/" + "unresTitleDisamb.txt");
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String title : pageTitlesDisamb){
                count++;
                bw.write(title);
                if(count != pageTitlesDisamb.size()) bw.write("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        // Writes list of unresolved titles of non-disambiguation pages
        count = 0;
        file = new File(dataFolder + "/" + "unresTitleNonDisamb.txt");
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String title : pageTitlesNondisamb){
                count++;
                bw.write(title);
                if(count != pageTitlesNondisamb.size()) bw.write("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
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

            fileReader.close();
            writeToFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
