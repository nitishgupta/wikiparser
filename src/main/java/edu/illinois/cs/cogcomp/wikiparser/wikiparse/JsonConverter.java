package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import edu.illinois.cs.cogcomp.wikiparser.ds.WikiPage;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class converts the WikiPage objects into JSON format.  With an 
 * estimated 5 million pages, each JSON file will contain 10000 pages.
 */
public class JsonConverter {
    private static final int limit = 10000;
    private static final String fileName = "WikiPages";
    private String outputDir;
    private String filePath, file;
    private static boolean fileOpened = false;
    
    public JsonConverter(String outputDir){
        this.outputDir = outputDir;
        filePath = Paths.get(outputDir, fileName).toString();
        System.out.println((filePath));
    }
    
    public static void ConvertToJson(List<WikiPage> objects, String outputDir){
        ObjectMapper mapper = new ObjectMapper();
        String json;
        Path filePath = Paths.get(outputDir, fileName);
        try{
            int fileNumber = 0;
            FileOutputStream fileOut = null;
            ObjectOutputStream out = null;
            for(int idx = 0; idx < objects.size(); idx++){
                if(idx % limit == 0){
                    fileNumber++;
                    //System.out.println(filePath.toString() + String.valueOf(fileNumber));
                    fileOut = new FileOutputStream(filePath.toString() + String.valueOf(fileNumber));
                    out = new ObjectOutputStream(fileOut);
                }
                WikiPage obj = objects.get(idx);
                json = mapper.writeValueAsString(obj);
                if(out != null) out.writeObject(json);
                //System.out.println(json);
            }
            fileOut.close();
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
