package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class KB {
    public static Map<String, String> curIds2TitleMap = null;
    public static Map<String, String> nonListMap = null;
    public static Map<String, String> RedirectTitle2ResolvedTitleMap = null;

    public static void loadRedirectTitle2ResolvedTitleMap(String filename){
        // Parses resCurId2Redirects.tsv
        RedirectTitle2ResolvedTitleMap = new HashMap();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while((line = reader.readLine()) != null){
                String [] row = line.split("\t");
                String [] redirects = row[1].split(" ");
                String resolvedTitle;
                if(curIds2TitleMap.containsKey(row[0])){
                    resolvedTitle = curIds2TitleMap.get(row[0]);
                }
                else continue;
                for(int idx = 0; idx < redirects.length; idx++){
                    if(curIds2TitleMap.containsKey(redirects[idx])){
                        RedirectTitle2ResolvedTitleMap.put(curIds2TitleMap.get(redirects[idx]), resolvedTitle);
                    }
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void loadCurIdsMap(String filename){
        // Parses curIds2Title.tsv
        curIds2TitleMap = new HashMap();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while((line = reader.readLine()) != null){
                String [] row = line.split("\t");
                curIds2TitleMap.put(row[0], row[1]);
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void loadNonListMap(String filename){
        // Parses resCurIdNonDisambig2ResTitle_nonList.tsv
        nonListMap = new HashMap();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while((line = reader.readLine()) != null){
                String [] row = line.split("\t");
                nonListMap.put(row[0], row[1]);
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String [] args){
      loadCurIdsMap(args[0]);
      System.out.println("Size of curIds2TitleMap is " + curIds2TitleMap.size());
      loadNonListMap(args[1]);
      System.out.println("Size of nonListMap is " + nonListMap.size());
      loadRedirectTitle2ResolvedTitleMap(args[2]);
      System.out.println("Size of RedirectTitle2ResolvedTitleMap is " + RedirectTitle2ResolvedTitleMap.size());
    }
}
