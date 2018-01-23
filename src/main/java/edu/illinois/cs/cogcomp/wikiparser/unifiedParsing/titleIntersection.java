package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author Reuben-PC
 */
public class titleIntersection {
    private String wikiExtractorOutput;
    private String jwplOutput;
    private String outputFile;
    
    public titleIntersection(String wikiExtractorOutput, String jwplOutput, String outputFile){
        this.wikiExtractorOutput = wikiExtractorOutput;
        this.jwplOutput = jwplOutput;
        this.outputFile = outputFile;
    }
    
    public void getIntersection(){
        
    }
    
    public static void main(String[] args){
        String file = "C:\\Users\\Reuben-PC\\Desktop\\testInput1.tsv";
        String line;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
