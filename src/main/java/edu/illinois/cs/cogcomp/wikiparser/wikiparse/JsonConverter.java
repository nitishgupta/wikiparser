package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import edu.illinois.cs.cogcomp.wikiparser.ds.WikiPage;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * This class converts the WikiPage objects into JSON format.  With an 
 * estimated 5 million pages, each JSON file will contain 10000 pages.
 */
public class JsonConverter {
    private static final int limit = 10000;
    private static final String filePath = "WikiPages";
    private static String outputDir = null;
    private static String fileName = null;
    private static String currentFileName = null;
    private static int count = 0;
    private static int fileNumber = 0;
    private static OutputStreamWriter fileOut = null;
    
    public static void setOutputDir(String directory){
        outputDir = directory;
        Path file = Paths.get(outputDir, filePath);
        fileName = file.toString();
    }
    
    public static synchronized void ConvertToJson(List<WikiPage> objects) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        for(int idx = 0; idx < objects.size(); idx++){
            try{
                if (count % limit == 0 || currentFileName == null) {
                    fileNumber++;
                    currentFileName = fileName + String.valueOf(fileNumber) + ".json";
                    System.out.println("Writing to " + currentFileName);
                    if(fileOut != null) {
                        fileOut.close(); // Closes current file stream
                    }
                    fileOut = new OutputStreamWriter(new FileOutputStream(currentFileName), 
                        StandardCharsets.UTF_8);
                }
                WikiPage obj = objects.get(idx);
                json = mapper.writeValueAsString(obj);
                JSONObject jsonobj = new JSONObject(json); // Convert text to object
                String jsonStr = jsonobj.toString();
                fileOut.write(jsonStr + "\n"); // Writes each object to a new line
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeFile() {
        try {
            if (fileOut != null) {
                fileOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized void closeFiles(){
        try{
            if(fileOut != null) fileOut.close(); // Closes last file
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
