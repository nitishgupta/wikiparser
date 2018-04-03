package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Path;
import edu.illinois.cs.cogcomp.wikiparser.constants.WikiparseConstants;
import edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.KB;

/**
 *
 */
public class resolveHyperlinks {
    private static Set<String> unresolvedTitles = new HashSet<String>();
    private ArrayList<Integer> possibleIndices = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> capCombinations = new ArrayList<ArrayList<Integer>>();

    public static void writeUnresolvedTitles(){
      File unresolvedFile = new File(WikiparseConstants.unresolvedOutput);
      try{
          FileWriter fw = new FileWriter(unresolvedFile.getAbsoluteFile());
          BufferedWriter bw = new BufferedWriter(fw);
          for(String title : unresolvedTitles){
              bw.write(title + "\n");
          }
          bw.close();
      }
      catch (IOException e){
          e.printStackTrace();
          System.exit(-1);
      }
    }

    private void combine(int start, ArrayList<Integer> indices){
        for(int i = start; i < indices.size(); i++){
           possibleIndices.add(indices.get(i));
           getIndices();
           combine(i+1, indices);
           possibleIndices.remove(possibleIndices.size() - 1);
        }
    }

    private void getIndices(){
        ArrayList<Integer> allIndices = new ArrayList<Integer>();
        for(int i = 0; i < possibleIndices.size(); i++){
            allIndices.add(possibleIndices.get(i));
        }
        capCombinations.add(allIndices);
    }

    private void clearIndices(){
      possibleIndices.clear();
      capCombinations.clear();
    }

    private String resolveCurrentString(String title){
      if(KB.resolvedTitlesList.contains(title)){
          return title;
      } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(title)){
          return KB.RedirectTitle2ResolvedTitleMap.get(title);
      } else {
        // Checks if # is present
        int idx = title.indexOf('#');
        String newTitle = title;
        if(idx != -1){
            newTitle = title.substring(0, idx);
            if(KB.resolvedTitlesList.contains(newTitle)){
                return newTitle;
            } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(newTitle)){
                return KB.RedirectTitle2ResolvedTitleMap.get(newTitle);
            }
        }

        ArrayList<Integer> indices = new ArrayList<Integer>();
        int index = newTitle.indexOf('_');
        indices.add(0);
        while (index >= 0) {
            indices.add(index+1);
            index = newTitle.indexOf('_', index + 1);
        }

        combine(0, indices);
        for(int fidx = 0; fidx < capCombinations.size(); fidx++){
            String str = newTitle;
            for(int sidx = 0; sidx < capCombinations.get(fidx).size(); sidx++){
                str = str.substring(0,capCombinations.get(fidx).get(sidx)) + str.substring(capCombinations.get(fidx).get(sidx),capCombinations.get(fidx).get(sidx)+1).toUpperCase() + str.substring(capCombinations.get(fidx).get(sidx)+1);
            }
            // Checks if it is a resolved title
            if(KB.resolvedTitlesList.contains(str)){
                return str;
            } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(str)){
                return KB.RedirectTitle2ResolvedTitleMap.get(str);
            }
        }
      }
      return null;
    }

    public Map<List<Integer>, String> resolve(Map<List<Integer>, String> hyperlinks){
        Set<List<Integer>> keys = hyperlinks.keySet();
        for(List<Integer> i : keys){
            String title = hyperlinks.get(i);
            clearIndices();
            String resolvedTitle =resolveCurrentString(title);
            if(resolvedTitle == null){
              unresolvedTitles.add(title);
            } else {
              hyperlinks.replace(i, resolvedTitle);
            }
        }

        return hyperlinks;
    }
}
