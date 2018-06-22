package edu.illinois.cs.cogcomp.wikiparser.unifiedParsing;

import java.util.*;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

import edu.illinois.cs.cogcomp.wikiparser.constants.WikiparseConstants;

/**
 *
 */
public class ResolveHyperlinks {
    private static Set<String> unresolvedTitles = new HashSet<String>();
    // private ArrayList<Integer> possibleIndices = new ArrayList<Integer>();
    // private ArrayList<ArrayList<Integer>> capCombinations = new ArrayList<ArrayList<Integer>>();

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

    private static void combine(int start, ArrayList<Integer> indices, ArrayList<Integer> possibleIndices,
                         ArrayList<ArrayList<Integer>> capCombinations) {
        for(int i = start; i < indices.size(); i++) {
            possibleIndices.add(indices.get(i));
            getIndices(possibleIndices, capCombinations);
            combine(i+1, indices, possibleIndices, capCombinations);
            possibleIndices.remove(possibleIndices.size() - 1);
        }
    }

    private static void getIndices(ArrayList<Integer> possibleIndices, ArrayList<ArrayList<Integer>> capCombinations){
        ArrayList<Integer> allIndices = new ArrayList<Integer>();
        for(int i = 0; i < possibleIndices.size(); i++) {
            allIndices.add(possibleIndices.get(i));
        }
        capCombinations.add(allIndices);
    }

    private static String checkTitle(String title) {
        if(KB.resTitle2curId.containsKey(title)){
            return title;
        } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(title)){
            return KB.RedirectTitle2ResolvedTitleMap.get(title);
        }
        return null;
    }


    private static String resolveTitleString(String title) {
//        ArrayList<Integer> possibleIndices = new ArrayList<Integer>();
//        ArrayList<ArrayList<Integer>> capCombinations = new ArrayList<ArrayList<Integer>>();

        int idx = title.indexOf('#');
        if(idx != -1) {
            title = title.substring(0, idx);
        }
        String resTitle = checkTitle(title);
        if (resTitle != null) {
            return resTitle;
        }

        if (title.length() < 2)
            return null;

        ArrayList<Integer> indices = new ArrayList<Integer>();
        indices.add(0);
        int index = title.indexOf('_');
        while (index >= 0) {
            // Don't want _ if last character
            if (index < title.length() - 1)
                indices.add(index+1);
            index = title.indexOf('_', index + 1);
        }

        String newtitle = title;

        for (int ind : indices) {
            try {
                newtitle = newtitle.substring(0, ind) + newtitle.substring(ind, ind + 1).toUpperCase() + newtitle.substring(ind + 1);
                resTitle = checkTitle(newtitle);
                if (resTitle != null) {
                    return resTitle;
                }
            } catch (Exception e) {
                System.out.println("Title:" + newtitle + " Len: " + title.length());
                System.out.println("Index: " + ind);
                return null;
            }
        }

//        combine(0, indices, possibleIndices, capCombinations);
//        for(int fidx = 0; fidx < capCombinations.size(); fidx++){
//            String str = newTitle;
//            for(int sidx = 0; sidx < capCombinations.get(fidx).size(); sidx++){
//                str = str.substring(0,capCombinations.get(fidx).get(sidx)) + str.substring(capCombinations.get(fidx).get(sidx),capCombinations.get(fidx).get(sidx)+1).toUpperCase() + str.substring(capCombinations.get(fidx).get(sidx)+1);
//            }
//            // Checks if it is a resolved title
//            if (KB.resTitle2curId.containsKey(str)) {
//                return str;
//            } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(str)){
//                return KB.RedirectTitle2ResolvedTitleMap.get(str);
//            }
//        }

        return null;
    }

    public static Map<List<Integer>, String> resolveHyperlinkMap(Map<List<Integer>, String> hyperlinks) {
        Set<List<Integer>> keys = hyperlinks.keySet();
        for(List<Integer> i : keys){
            String title = hyperlinks.get(i);
            String resolvedTitle = resolveTitleString(title);
            if(resolvedTitle != null){
                hyperlinks.replace(i, resolvedTitle);
            }
//            if(resolvedTitle == null){
//              unresolvedTitles.add(title);
//            } else {
//              hyperlinks.replace(i, resolvedTitle);
//            }
        }
        return hyperlinks;
    }

    public static void main(String [] args) {
        String unifiedParserOutputDir = args[0];
        KB.setFilePaths(unifiedParserOutputDir);
        KB.loadCurIdsMap();
        KB.loadResolvedCurIdsMap();
        KB.loadRedirectTitle2ResolvedTitleMap();
        ResolveHyperlinks hyper = new ResolveHyperlinks();

        String title = "lal_bahadur_shastri";
        String resolvedTitle = hyper.resolveTitleString(title);
        System.out.println("In: " + title + "   Output: " + resolvedTitle);
    }
}
