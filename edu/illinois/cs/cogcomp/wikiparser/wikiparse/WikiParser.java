/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wiki.parser;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.FileUtils;
import utils.Pair;

/**
 *
 * @author Reuben-PC
 */
public class WikiParser {
    
    private static Logger logger;
    
    private static String fileToString (String filename) throws IOException{
        /**
         * Converts an text file of wikipedia pages into a single string
         */
        BufferedReader reader = new BufferedReader(new FileReader (filename));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } 
        finally {
            reader.close();
        }
    }
    
    private static String decodeURL(String url) throws UnsupportedEncodingException {
            return java.net.URLDecoder.decode(url, "UTF-8");
    }
    
    private static String[] _cleanHyperLink(String string) {
        /**
        * Input : <a href="Grand%20Slam%20%28tennis%29">Grand Slam</a>
        * Output : String[] = [decode("Grand%20Slam%20%28tennis%29"), "Grand Slam"]
        *
        * If Url cannot be parsed, then output String[0] = null
        */

       int len = string.length();
       String urlCharCharSurface = string.substring(9, len - 4);  // Returns : url">surface
       String[] urlSurface = urlCharCharSurface.split("\">");
       assert (urlSurface.length == 2);
       String decodedUrl = new String();
       try {
               decodedUrl = decodeURL(urlSurface[0]);
       } catch (Exception e) {
               logger.severe("URL Parsing failed : " + urlSurface[0]);
       }
       urlSurface[0] = decodedUrl;
       return urlSurface;
    }
    
    private static String getText(String [] lines){
        /**
         * Helper function to convert all of the lines
         * in a single article to a single string
         */
        StringBuilder doc = new StringBuilder();
        for(int i = 2; i < lines.length; i++){ // First line is url and second is title - Skip them
            if (!lines[i].trim().isEmpty()) {
                doc.append(lines[i]);
            } 
            doc.append("\n");
        }
        
        String docText = doc.toString().trim();
        return docText;
    }
    
    private static Pair<StringBuilder, Map<Pair<Integer, Integer>, String>> cleanDocText(String markedupText) {
        /**
        * Takes text marked with <a href="url">surface</a> and returns
        * Returns:
        * 	StringBuilder : cleanText with no markups.
        * 	Map<<start, end>, Title> : Start (inc) end(exc) char offsets for surface and their resp. marked titles
        * 
        * Important Note: Text files in the wikipedia dump do not indicate between sections.
        *                 The text field is stored as in the same format as the parsed text where
        *                 the paragraphs are kept as they are and are separated by an empty line.  
        */
        StringBuilder cleanText = new StringBuilder();
	Map<Pair<Integer, Integer>, String> offsets2Title = new HashMap<>();
        Pattern linkPattern = Pattern.compile("(<a[^>]+>.+?</a>)",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	Matcher matcher = linkPattern.matcher(markedupText);
        int len = markedupText.length();
        int oldStart = 0;
        int start = 0;
        while(matcher.find()){
            start = matcher.start();
            cleanText.append(markedupText.substring(oldStart, start));
            String[] urlSurface = _cleanHyperLink(matcher.group());

            if (urlSurface[0] != null) {
                offsets2Title.put(new Pair<Integer, Integer>(cleanText.length(), cleanText.length()+urlSurface[1].length()),
                                  urlSurface[0]);
            }
            cleanText.append(urlSurface[1]);
            oldStart = matcher.end();   
        }
        
        if (oldStart < len) cleanText.append(markedupText.substring(oldStart, len));
        return new Pair<StringBuilder,Map<Pair<Integer, Integer>, String>>(cleanText, offsets2Title);
    }
    
    public static List<WikiPage> breakDocs(String filename){
        /**
         * Takes the name of the file to parse as input
         * Returns: a list of wikidata objects with the required data fields
         */
        String text = null;
        text = FileUtils.readFileToString(filename);
        if(text == null) return null;
        
        String [] docs = text.split("</doc>");
        List<WikiPage> data = new ArrayList<>();
        for(int i = 0; i < docs.length; i++){ // Last element is an empty string - ignore it
            String doc = docs[i];
            if (!doc.trim().isEmpty()){
                String [] lines = doc.trim().split("\n");
                assert (lines[0].startsWith("<doc id=")); // Line 1 should be <doc ... >
                
                // Gets Wikititle
                String title = lines[0].split("title=\"")[1];
		String wikiTitle = title.substring(0,title.length()-3); // Removes extra characters
                if (wikiTitle.startsWith("List of") || wikiTitle.startsWith("Lists of")) continue;
                try {
			wikiTitle = decodeURL(wikiTitle);
		} catch (Exception e) {
			logger.severe("URL Parsing failed : " + wikiTitle);
		}                              

                // Gets curID
                String firstLine = lines[0].split("curid=")[1];
                int index = firstLine.indexOf("\"");
                String id = firstLine.substring(0, index);
                int curId = Integer.parseInt(id);
                
                // Gets page title
                String pageTitle = lines[1];
                
                // Gets Text and internal hyperlinks
                String doctext = getText(lines);
                Pair<StringBuilder, Map<Pair<Integer, Integer>, String>> cleanText2Offset = cleanDocText(doctext);
                doctext = cleanText2Offset.getFirst().toString();

                Map<Pair<Integer, Integer>, String> hyperlinks = cleanText2Offset.getSecond();
                
                data.add(new WikiPage(wikiTitle, pageTitle, doctext, curId, hyperlinks));
            }
        }
        
        return data;
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        String filename = "C:\\Users\\Reuben-PC\\Desktop\\test.txt";
        //String filename = "C:\\Users\\Reuben-PC\\Desktop\\test2.txt";
        List<WikiPage> res = breakDocs(filename);
    }
    
}
