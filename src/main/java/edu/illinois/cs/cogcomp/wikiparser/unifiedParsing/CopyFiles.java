package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;

/**
 * This class reads curIds2Title.tsv and resCurId2Redirects.tsv from 
 * an input directory and copies them to a specified directory
 */
public class CopyFiles {
    private String inputDir;
    private String outputDir;
    private Map<String, String> idMap;
    private Map<String, String> redirectMap;
    
    public CopyFiles(String inputDir, String outputDir){
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        idMap = new HashMap<String, String>();
        redirectMap = new HashMap<String, String>();
    }
    
    private void writeToFiles(){
        try{
            // Writes to curIds2Title.tsv in new directory
            Path filePath = Paths.get(this.outputDir, JWPLConstants.curId2Title);
            File file = new File(filePath.toString());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String id : idMap.keySet()) {
              String PageTitle = idMap.get(id);
              bw.write(id + "\t" + PageTitle + "\n"); // Each page exists on a new line
            }
            
            // Writes to resCurId2Redirects.tsv in new directory
            filePath = Paths.get(this.outputDir, JWPLConstants.resCurId2Redirects);
            file = new File(filePath.toString());
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            for(String id : redirectMap.keySet()){
              String redirect = redirectMap.get(id);
              bw.write(id + "\t" + redirect + "\n"); // Each page exists on a new line
            }
            
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public void copy() {
        // Parses input files
        String line;
        try{
            // Parses curIds2Title.tsv
            System.out.println("Parsing curIds2Title.tsv");
            Path filePath = Paths.get(this.inputDir, JWPLConstants.curId2Title);
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            while((line = reader.readLine()) != null){
                String [] row = line.split("\t");
                idMap.put(row[0], row[1]);
            }

            // Parses resCurId2Redirects.tsv
            System.out.println("Parsing resCurId2Redirects.tsv");
            filePath = Paths.get(this.inputDir, JWPLConstants.resCurId2Redirects);
            reader = new BufferedReader(new FileReader(filePath.toString()));
            while((line = reader.readLine()) != null){
                String [] row = line.split("\t");
                redirectMap.put(row[0], row[1]);
            }
            writeToFiles();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        String inputDir = args[0];
        String outputDir = args[1];
        CopyFiles cf = new CopyFiles(inputDir, outputDir);
        cf.copy();
    }
}
