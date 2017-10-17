package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 *  This class receives the Category.txt file as input.
 *  It will parse the file and creates data structures
 *  that will be stored in a folder called category_output
 */
public class CategoryParser {
    public static Map<Integer, String> idToDisambCat = new HashMap(); // Maps category id to disambiguation categories
    public static Map<Integer, String> idToCat = new HashMap();  // Maps category id to category titles
    private static Map<String, Integer> catToId = new HashMap();  // Maps category titles to category id
    
    private void writeToFiles(){
        String projectFolder = System.getProperty("user.dir");
        String dataFolder = projectFolder + "\\category_output";
        File directory = new File(dataFolder);
        if(! directory.exists()){
            directory.mkdir();
        }
        int count = 0;
        
        // Writes map from category id (first column) to category title (second column)
        File file = new File(dataFolder + "\\" + "id2Title.txt");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Integer id : idToCat.keySet()){
                count++;
                bw.write(id.toString() + "\t" + idToCat.get(id));
                if(count != idToCat.keySet().size()) bw.write("\n");
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Write map from category title (first column) to category id (second column)
        count = 0;
        file = new File(dataFolder + "\\" + "title2Id.txt"); 
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String title : catToId.keySet()){
                count++;
                bw.write(title + "\t" + catToId.get(title));
                if(count != catToId.keySet().size()) bw.write("\n");
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
