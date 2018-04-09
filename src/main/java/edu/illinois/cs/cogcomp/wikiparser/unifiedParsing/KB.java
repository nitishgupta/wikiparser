package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Path;
import edu.illinois.cs.cogcomp.wikiparser.constants.WikiparseConstants;

public class KB {
    public static Map<String, String> curIds2TitleMap = null;

    public static Map<String, String> resTitle2curId = null;
    public static Map<String, String> resCurId2Title = null;


    public static Map<String, String> nonListMap = null;
    public static Map<String, String> RedirectTitle2ResolvedTitleMap = null;
    // public static Set<String> resolvedTitlesList = null;


    public static void loadRedirectTitle2ResolvedTitleMap() {
        System.out.println("# Loading RedirectTitle2ResolvedTitleMap");
        if (RedirectTitle2ResolvedTitleMap == null) {
            _loadRedirectTitle2ResolvedTitleMap();
        } else {
            System.out.println("# Loaded. RedirectTitle2ResolvedTitleMap: " + RedirectTitle2ResolvedTitleMap.size());
        }
    }

    public static void _loadRedirectTitle2ResolvedTitleMap(){
        // Parses resCurId2Redirects.tsv

        // resolvedTitlesList = new HashSet<String>();
        RedirectTitle2ResolvedTitleMap = new HashMap();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(WikiparseConstants.resCurId2RedirectsPath));
            while((line = reader.readLine()) != null){
                String [] columns = line.split("\t");
                String [] redirects = columns[1].split(" ");
                String resolvedTitle;
                if(curIds2TitleMap.containsKey(columns[0])){
                    resolvedTitle = curIds2TitleMap.get(columns[0]);
                    // resolvedTitlesList.add(resolvedTitle);
                }
                else continue;
                for(int idx = 0; idx < redirects.length; idx++){
                    if(curIds2TitleMap.containsKey(redirects[idx])) {
                        RedirectTitle2ResolvedTitleMap.put(curIds2TitleMap.get(redirects[idx]), resolvedTitle);
                        // Stores lower case version of RedirectTitle
                        /*Set<String> actualTitles = lowerCaseTitles2actualTitles.get(lowerCaseRedirectTitle);
                        if(actualTitles == null){
                          actualTitles = new HashSet<String>();
                        }
                        actualTitles.add(curIds2TitleMap.get(redirects[idx]));
                        lowerCaseTitles2actualTitles.replace(lowerCaseRedirectTitle, actualTitles);

                        // Stores lower case version of resolvedTitle
                        actualTitles = lowerCaseTitles2actualTitles.get(lowerCaseResolvedTitle);
                        if(actualTitles == null){
                          actualTitles = new HashSet<String>();
                        }
                        actualTitles.add(resolvedTitle);
                        lowerCaseTitles2actualTitles.replace(lowerCaseResolvedTitle, actualTitles);*/
                    }
                }
            }
            reader.close();

            System.out.println("# Loaded. RedirectTitle2ResolvedTitleMap: " + RedirectTitle2ResolvedTitleMap.size());

        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void loadResolvedCurIdsMap() {
        System.out.println("# Loading Resolved curIds2TitleMap");
        if (resTitle2curId == null || resCurId2Title == null) {
            _loadResolvedCurIdsMap();
        } else {
            System.out.println("# Loaded. Resolved curIds2TitleMap: " + resCurId2Title.size());
        }
    }

    public static void _loadResolvedCurIdsMap(){
        // Parses curIds2Title.tsv
        resCurId2Title = new HashMap();
        resTitle2curId = new HashMap();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(WikiparseConstants.resCurIdNonDisambig2ResTitlePath));
            while((line = reader.readLine()) != null){
                String [] columns = line.split("\t");
                resCurId2Title.put(columns[0], columns[1]);
                resTitle2curId.put(columns[1], columns[0]);
            }
            reader.close();
            System.out.println("# Loaded. Resolved curIds2TitleMap: " + resCurId2Title.size());
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public static void loadCurIdsMap() {
        System.out.println("# Loading curIds2TitleMap");
        if (curIds2TitleMap == null) {
            _loadCurIdsMap();
        } else {
            System.out.println("# Loaded. curIds2TitleMap: " + curIds2TitleMap.size());
        }
    }


    public static void _loadCurIdsMap(){
        // Parses curIds2Title.tsv
        curIds2TitleMap = new HashMap();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(WikiparseConstants.curIds2TitleMapPath));
            while((line = reader.readLine()) != null){
                String [] columns = line.split("\t");
                curIds2TitleMap.put(columns[0], columns[1]);
            }
            reader.close();
            System.out.println("# Loaded. curIds2TitleMap: " + curIds2TitleMap.size());
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
