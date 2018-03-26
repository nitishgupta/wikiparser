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

/**
 *
 */
public class resolveHyperlinks {
    private String file = "/shared/preprocessed/wikiparser/unifiedParserOutput/RedirectTitle2ResolvedTitle.txt";
    private static String unresolvedOutput = "/shared/preprocessed/wikiparser/unifiedParserOutput/unresolvedHyperlinks.txt";
    private static Set<String> unresolvedTitles = new HashSet<String>();
    private Set<String> resolvedTitles;
    private Map<String, String> redirectToResolved;

    public resolveHyperlinks(){
        parseMap();
    }

    private void parseMap(){
        resolvedTitles = new HashSet<String>();
        redirectToResolved = new HashMap<String, String>();
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String redirectTitle, resolvedTitle;
            while((line = reader.readLine()) != null){
                String [] row = line.split("\t");
                redirectTitle = row[0];
                resolvedTitle = row[1];
                resolvedTitles.add(resolvedTitle);
                if(!redirectToResolved.containsKey(redirectTitle)){
                    redirectToResolved.put(redirectTitle, resolvedTitle);
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void writeUnresolvedTitles(){
      File unresolvedFile = new File(unresolvedOutput);
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
            if(resolvedTitles.contains(title)){
                continue;
            } else if(redirectToResolved.containsKey(title)){
                hyperlinks.replace(i, redirectToResolved.get(title));
            } else{
                // Checks if # is present
                int idx = title.indexOf('#');
                if(idx != -1){
                    String shortened_title = title.substring(0, idx);
                    if(resolvedTitles.contains(shortened_title)){
                        hyperlinks.replace(i, shortened_title);
                    } else if(redirectToResolved.containsKey(shortened_title)){
                        hyperlinks.replace(i, redirectToResolved.get(shortened_title));
                    }
                } else{
                    // Capitalizes first letter
                    String cap_title = title.substring(0, 1).toUpperCase() + title.substring(1);
                    if(resolvedTitles.contains(cap_title)){
                        hyperlinks.replace(i, cap_title);
                        continue;
                    } else if(redirectToResolved.containsKey(title)){
                        hyperlinks.replace(i, redirectToResolved.get(cap_title));
                        continue;
                    }
                    // Capitalizes all letters directly succeeding '_'
                    for(int c = 1; c < cap_title.length(); c++){
                        if(cap_title.charAt(c) == '_'){
                            if(c + 1 < cap_title.length()){
                                cap_title = cap_title.substring(0,c+1) + cap_title.substring(c+1,c+1).toUpperCase();
                                if(c + 2 < cap_title.length()) cap_title += title.substring(c+2);
                            }
                        }
                    }
                    if(resolvedTitles.contains(cap_title)){
                        hyperlinks.replace(i, cap_title);
                    } else if(redirectToResolved.containsKey(title)){
                        hyperlinks.replace(i, redirectToResolved.get(cap_title));
                    } else{
                      unresolvedTitles.add(title);
                    }
                }
            }
        }

        return hyperlinks;
    }
}
