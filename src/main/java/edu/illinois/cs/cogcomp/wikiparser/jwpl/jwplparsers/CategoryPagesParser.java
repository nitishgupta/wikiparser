package edu.illinois.cs.cogcomp.wikiparser.jwpl.jwplparsers;

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
    private static Map<Integer, Set<String>> resCurIdToCatTitles; // Maps from resolved Cur Ids to page titles
    private static Set<Integer> disambPageResCurIds; // Stores resolved Ids of disambiguation pages
    private static Set<Integer> nondisambPageResCurIds; // Stores resolved Ids of non-disambiguation pages
    private static Set<Integer> nondisambPageResCurIds_noList; // Stores resolved Ids of non-disambiguation pages


    public CategoryPagesParser(String outputDir) {
        this.outputDir = outputDir;

        System.out.println("CategoryPages  Parser");
        System.out.println("Parses category_pages.txt to produce:");
        System.out.println("[1] redCurIds -> Category Tiles: " + JWPLConstants.resCurIdToCatTitles);
        System.out.println("[2] redCurIds (disambig) -> Page Title: " + JWPLConstants.resCurIdDisambig2ResTitle);
        System.out.println("[2] redCurIds (non-disambig) -> Page Title: " + JWPLConstants.resCurIdNonDisambig2ResTitle);
        System.out.println("[2] redCurIds (non-disambig, no-list) -> Page Title: " + JWPLConstants.resCurIdNonDisambig2ResTitle_nonList);

        System.out.println("[#] Output Folder: " + outputDir);

        resCurIdToCatTitles = new HashMap(); // Maps from resolved Cur Ids to page titles
        disambPageResCurIds = new HashSet(); // Stores resolved Ids of disambiguation pages
        nondisambPageResCurIds = new HashSet(); // Stores resolved Ids of non-disambiguation pages
        nondisambPageResCurIds_noList = new HashSet(); // Stores resolved Ids of non-disambiguation pages
    }
    
    private void writeResIdsToCatTitles(){
        // Writes map from resolved cur ids to set of category titles
        Path filePath = Paths.get(outputDir, JWPLConstants.resCurIdToCatTitles);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : resCurIdToCatTitles.keySet()){
                StringBuffer writeline = new StringBuffer();
                writeline.append(id.toString()).append("\t");
                for(String cattitle : resCurIdToCatTitles.get(id)){
                    writeline.append(cattitle).append(" ");
                }
                String wrline = writeline.toString().trim();
                bw.write(wrline);
                bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void writeCurId2Title(Set<Integer> curIds, Path filePath) {
        // Writes map from page ids to page titles
        // Path filePath = Paths.get(outputDir, JWPLConstants.resCurId2ResTitle);
        System.out.println("Number of titles in resCurIdsToTitles: " + PageMapLineParser.resCurIdsToTitles.keySet().size());

        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : curIds){
                if (!PageMapLineParser.resCurIdsToTitles.containsKey(id)) {
                    System.out.println(id + " not in resCurIds2Titles");
                    System.exit(0);
                }

                String resPageTitle = PageMapLineParser.resCurIdsToTitles.get(id);
                bw.write(id.toString() + "\t" + resPageTitle + "\n"); // Each page exists on a new line
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
//    private void writeResIdsDisambPages(){
//        // Writes list of resolved cur ids of disambiguation pages
//        Path filePath = Paths.get(outputDir, JWPLConstants.resDisambPageIds);
//        File file = new File(filePath.toString());
//        try {
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            for(Integer id : disambPageResCurIds){
//                bw.write(id.toString() + "\n");
//            }
//        } catch (IOException e){
//            e.printStackTrace();
//            System.exit(-1);
//        }
//    }
//
//    private void writeResIdsNonDisambPage(){
//        // Writes list of resolved cur ids of non-disambiguation pages
//        Path filePath = Paths.get(outputDir, JWPLConstants.resNonDisambPageIds);
//        File file = new File(filePath.toString());
//        try {
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            for(Integer id : nondisambPageIds){
//                bw.write(id.toString() + "\n");
//            }
//        } catch (IOException e){
//            e.printStackTrace();
//            System.exit(-1);
//        }
//    }
    
    private void writeToFiles() {
        writeResIdsToCatTitles();

        Path filePath = null;

        filePath = Paths.get(outputDir, JWPLConstants.resCurIdNonDisambig2ResTitle);
        writeCurId2Title(nondisambPageResCurIds, filePath);

        filePath = Paths.get(outputDir, JWPLConstants.resCurIdNonDisambig2ResTitle_nonList);
        writeCurId2Title(nondisambPageResCurIds_noList, filePath);

        filePath = Paths.get(outputDir, JWPLConstants.resCurIdDisambig2ResTitle);
        writeCurId2Title(disambPageResCurIds, filePath);
    }

    public void parseCategoryPages(String CategoryPages){
        try {
            File file = new File(CategoryPages);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String [] args = line.split("\t");
                Integer catId = Integer.parseInt(args[0]);
                Integer curId = Integer.parseInt(args[1]);
                String catName = CategoryParser.idToCat.get(catId);

                Integer resCurId = PageMapLineParser.uidToRid.get(curId);

                // Maps resolved cur id to set of category titles
                if(!resCurIdToCatTitles.containsKey(resCurId)) {
                    resCurIdToCatTitles.put(resCurId, new HashSet<String>());
                }
                resCurIdToCatTitles.get(resCurId).add(catName);

                if (CategoryParser.disambigCatIdToDisambCatTitle.containsKey(catId)) {
                    disambPageResCurIds.add(resCurId);
                }
            }

            fileReader.close();

            nondisambPageResCurIds = new HashSet<Integer>(PageMapLineParser.resCurIdsToTitles.keySet());
            nondisambPageResCurIds.removeAll(disambPageResCurIds);

            nondisambPageResCurIds_noList = new HashSet<Integer>(PageMapLineParser.resCurIdsToTitles.keySet());
            nondisambPageResCurIds_noList.removeAll(disambPageResCurIds);

            writeToFiles();  // Writes outputs to files
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
