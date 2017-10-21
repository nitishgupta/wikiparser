package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 *  This class receives the Category.txt file as input.
 *  It will parse the file and creates data structures
 *  that will be stored in a folder called category_output
 */
public class CategoryParser {
    private String outputDir;
    public static Map<Integer, String> idToDisambCat = new HashMap(); // Maps category id to disambiguation categories
    public static Map<Integer, String> idToCat = new HashMap();  // Maps category id to category titles
    private static Map<String, Integer> catToId = new HashMap();  // Maps category titles to category id
    
    public CategoryParser(String outputDir){
        this.outputDir = outputDir;
    }

    private void writeToFiles(){
        // Writes map from category id (first column) to category title (second column)
        Path filePath = Paths.get(outputDir, JWPLConstants.idsToTitles);
        File file = new File(filePath.toString());
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : idToCat.keySet()){
                bw.write(id.toString() + "\t" + idToCat.get(id) + "\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void parseCategory(String CategoryFile){
        try {
            File file = new File(CategoryFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String [] args = line.split("\t");
                Integer catId = Integer.parseInt(args[0]);
                String title = args[2];
                title = title.replace("\\", ""); // Removes escape character '\'
                title = title.trim();
                if(title.isEmpty()) continue;
                if(title.toLowerCase().contains("disambig")){ // Stores ids and titles of disambiguation categories
                    idToDisambCat.put(catId, title);
                }
                idToCat.put(catId, title);  // Maps id to title
                catToId.put(title, catId);  // Maps title to id
            }
            fileReader.close();
            writeToFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
