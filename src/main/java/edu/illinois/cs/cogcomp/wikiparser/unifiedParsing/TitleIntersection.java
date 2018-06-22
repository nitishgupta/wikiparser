package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *  This class computes the intersection between the jwpl output
 *  and the wikiextractor output and writes them to the designated
 *  output file
 */
public class TitleIntersection {
    private String wikiExtractorOutput;
    private String jwplOutput;
    private String outputFile;
    private Map<String, String> jwplTitleMap;
    private Map<String, String> wikiextractorTitleMap;
    private Map<String, String> outputMap;

    public TitleIntersection(String wikiExtractorOutput, String jwplOutput, String outputFile){
        this.wikiExtractorOutput = wikiExtractorOutput;
        this.jwplOutput = jwplOutput;
        this.outputFile = outputFile;
        jwplTitleMap = new HashMap<String, String>();
        wikiextractorTitleMap = new HashMap<String, String>();
        outputMap = new HashMap<String, String>();
    }

    public void writeToFile(){
      File file = new File(this.outputFile);
      try{
          FileWriter fw = new FileWriter(file.getAbsoluteFile());
          BufferedWriter bw = new BufferedWriter(fw);
          for(String id : outputMap.keySet()){
              String PageTitle = outputMap.get(id);
              bw.write(id + "\t" + PageTitle + "\n"); // Each page exists on a new line
          }
          bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void getIntersection(){
      // Parses wikiextractor output
      String line;
      try{
          // Parses wikiextractor output
          System.out.println("Parsing wikiextractor output");
          BufferedReader reader = new BufferedReader(new FileReader(this.wikiExtractorOutput));
          while((line = reader.readLine()) != null){
              String [] row = line.split("\t");
              wikiextractorTitleMap.put(row[0], row[1]);
          }

          // Parses jwpl output
          System.out.println("Parsing jwpl output");
          reader = new BufferedReader(new FileReader(this.jwplOutput));
          while((line = reader.readLine()) != null){
              String [] row = line.split("\t");
              jwplTitleMap.put(row[0], row[1]);
          }

          // Computes intersection between these 2 title maps
          System.out.println("Computing intersection");
          Set<String> id = wikiextractorTitleMap.keySet();
          id.retainAll(jwplTitleMap.keySet());
          for(String key : id){
            String jwplTitle = jwplTitleMap.get(key);
            String wikiTitle = wikiextractorTitleMap.get(key);
            if(jwplTitle.equals(wikiTitle)){
              outputMap.put(key, jwplTitle);
            }
          }
          writeToFile();
      } catch(IOException e){
          e.printStackTrace();
      }
      System.out.println("Done computing intersection");
    }

    public static void main(String[] args) {
        // args
        // 0: WikiExtractor curId2Title - filepath
        // 1: Jwpl resCurIdNonDisambig2ResTitle_nonList.tsv
        // 2: unifiedParserOutput/resCurIdNonDisambig2ResTitle_nonList.tsv

        TitleIntersection intersect = new TitleIntersection(args[0], args[1], args[2]);
        intersect.getIntersection();
    }
}
