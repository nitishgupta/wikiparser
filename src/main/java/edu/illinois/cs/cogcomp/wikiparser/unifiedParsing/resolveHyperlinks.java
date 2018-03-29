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

    public Map<List<Integer>, String> resolve(Map<List<Integer>, String> hyperlinks){
        Set<List<Integer>> keys = hyperlinks.keySet();
        for(List<Integer> i : keys){
            String title = hyperlinks.get(i);
            if(KB.resolvedTitlesList.contains(title)){
                continue;
                //lowerCaseTitles2actualTitles = new HashMap();
            } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(title)){
                hyperlinks.replace(i, KB.RedirectTitle2ResolvedTitleMap.get(title));
            } else{
                // Checks if # is present
                int idx = title.indexOf('#');
                String newTitle = title;
                if(idx != -1){
                    newTitle = title.substring(0, idx);
                    if(KB.resolvedTitlesList.contains(newTitle)){
                        hyperlinks.replace(i, newTitle);
                    } else if(KB.RedirectTitle2ResolvedTitleMap.containsKey(newTitle)){
                        hyperlinks.replace(i, KB.RedirectTitle2ResolvedTitleMap.get(newTitle));
                    }
                }
                // Checks all possible capitalizations of characters succeeding any '_'
                String lowerCaseShortenedTitle = newTitle.toLowerCase();
                if(KB.lowerCaseTitles2actualTitles.containsKey(lowerCaseShortenedTitle)){
                  Set<String> actualTitles = KB.lowerCaseTitles2actualTitles.get(lowerCaseShortenedTitle);
                  if(!actualTitles.contains(newTitle)){
                    unresolvedTitles.add(title);
                  } else{
                    // check if shortened title is a resolved title
                    if(KB.resolvedTitlesList.contains(newTitle)){
                      hyperlinks.replace(i, newTitle);
                    } else{
                      // checks if shortened title is a redirect title and resolve it
                      boolean resolved = false;
                      for(String redirectShortTitle : actualTitles){
                        if(KB.RedirectTitle2ResolvedTitleMap.containsKey(redirectShortTitle)){
                          resolved = true;
                          hyperlinks.replace(i, KB.RedirectTitle2ResolvedTitleMap.get(redirectShortTitle));
                          break;
                        }
                        if(!resolved){
                          unresolvedTitles.add(title);
                        }
                      }
                    }
                  }
                }
            }
        }

        return hyperlinks;
    }
}
