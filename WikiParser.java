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

/**
 *
 * @author Reuben-PC
 */
public class WikiParser {
    
    private static Logger logger;
    
    private static String fileToString (String filename) throws IOException{
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
       String urlCharCharSurface = string.substring(9, len - 4);			// Returns : url">surface
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
        StringBuilder doc = new StringBuilder();
        for(int i = 2; i < lines.length; i++){ // First line is url and second is title
            if (!lines[i].trim().isEmpty()) {
                doc.append(lines[i]);
                doc.append("\n");
            }
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
                //System.out.println(urlSurface[0]);
                //System.out.println(urlSurface[1]);
                System.out.println("Start is " + cleanText.length() + " and end is " + (cleanText.length()+urlSurface[1].length()));
                offsets2Title.put(new Pair<Integer, Integer>(cleanText.length(), cleanText.length()+urlSurface[1].length()),
                                  urlSurface[0]);
            }
            cleanText.append(urlSurface[1]);
            oldStart = matcher.end();      
        }
        if (oldStart < len) cleanText.append(markedupText.substring(oldStart, len));
        //System.out.println(cleanText);
        return new Pair<StringBuilder,Map<Pair<Integer, Integer>, String>>(cleanText, offsets2Title);
    }
    
    public static List<wikidata> breakDocs(String filename){ // Have to change return type
        String text = null;
        try{
            text = fileToString(filename);
            //System.out.println(text.length());
            //System.out.println(text);
        }catch(IOException e){
            System.out.println("Invalid file path");
        }
        if(text == null) return null;
        
        String [] docs = text.split("</doc>");
        //System.out.println(docs.length);
        //System.out.println(docs[1]);
        //for(int i = 0; i < docs.length; i++) System.out.println(docs[i]);
        List<wikidata> data = new ArrayList<>();
        for(int i = 0; i < docs.length - 1; i++){ // Last element is an empty string
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
                //System.out.println(wikiTitle);
                
                
                
                // Gets curID
                String firstLine = lines[0].split("curid=")[1];
                int index = firstLine.indexOf("\"");
                String id = firstLine.substring(0, index);
                int curId = Integer.parseInt(id);
                //System.out.println(curId);
                
                // Gets page title
                String pageTitle = lines[1];
                //System.out.println(pageTitle);
                
                // Gets Text and internal hyperlinks
                String doctext = getText(lines);
                //System.out.println(doctext);
                Pair<StringBuilder, Map<Pair<Integer, Integer>, String>> cleanText2Offset = cleanDocText(doctext);
                doctext = cleanText2Offset.getFirst().toString();
                Map<Pair<Integer, Integer>, String> hyperlinks = cleanText2Offset.getSecond();
                //System.out.println(doctext);
                
                data.add(new wikidata(wikiTitle, pageTitle, text, curId, hyperlinks));
            }
        }
        
        return data;
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        //String filename = "C:\\Users\\Reuben-PC\\Desktop\\test.txt";
        String filename = "C:\\Users\\Reuben-PC\\Desktop\\test2.txt";
        breakDocs(filename);
    }
    
}
