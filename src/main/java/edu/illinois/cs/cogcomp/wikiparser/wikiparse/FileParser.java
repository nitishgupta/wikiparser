package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import edu.illinois.cs.cogcomp.wikiparser.ds.WikiPage;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.illinois.cs.cogcomp.wikiparser.utils.FileUtils;
import edu.illinois.cs.cogcomp.wikiparser.utils.Pair;
import edu.illinois.cs.cogcomp.wikiparser.utils.ParserLogger;
import edu.illinois.cs.cogcomp.wikiparser.wikiparse.JsonConverter;

/**
 *
 * This class implements functions to parse a single wiki text file into the required
 * fields and create a list of WikiPage objects depending on the number of articles
 * in the text file
 */
public class FileParser implements Runnable {

    /*
        This class is used to temporarily store data from the the parsing of
        a single document.  The text variable will then be additionally
        processed further to get the document text and hyperlinks.
    */
    public class DataFields {
        private String wikiTitle;  // This is the title in the hyperlink with underscores and decoded url
        private String pageTitle;  // Title of the page visible in the Wikipedia webpage.  This includes spaces.
        private Integer curId;  // Integer ID that is unique to every wikipedia page
        private String text; // This contains the text and hyperlinks of a document

        public DataFields(){
            this.wikiTitle = null;
            this.pageTitle = null;
            this.text = null;
            this.curId = null;
        }

        public DataFields(String wikiTitle, String pageTitle, Integer curId, String text){
            this.wikiTitle = wikiTitle;
            this.pageTitle = pageTitle;
            this.curId = curId;
            this.text = text;
        }

        public String getWikiTitle(){
            return this.wikiTitle;
        }

        public String getPageTitle(){
            return this.pageTitle;
        }

        public int getId(){
            return this.curId;
        }

        public String getText(){
            return this.text;
        }
    }

    private static Logger logger;
    public String infile;
    public String outfile;

    public FileParser(String infile, String outfile, Logger logger){
        this.logger = logger;
        this.infile = infile;
        this.outfile = outfile;
    }

    private static String decodeURL(String url) throws UnsupportedEncodingException {
        if(url.contains("%s") || url.contains("90% ")) return url;
        return java.net.URLDecoder.decode(url, "UTF-8");
    }

    private static String removeSpecialCharacters(String text){
        text = text.replaceAll(" ", "_");
        while(text.contains("&amp;") || text.contains("&nbsp;") || text.contains("&quot;") || text.contains("&lt;") || text.contains("&gt;")){
          text = text.replaceAll("&amp;", "&"); // Replaces entity name for the character '&'
          text = text.replaceAll("&nbsp;", "_");  // Replaces entity name for the character '-'
          text = text.replaceAll("&quot;", "\""); // Replaces entity name for the character '"'
          text = text.replaceAll("&lt;", "<");
          text = text.replaceAll("&gt;", ">");
        }
        return text;
    }

    private String[] _cleanHyperLink(String string) {
        /**
        * Input : <a href="Grand%20Slam%20%28tennis%29">Grand Slam</a>
        * Output : String[] = [decode("Grand%20Slam%20%28tennis%29"), "Grand Slam"]
        *
        * If Url cannot be parsed, then output String[0] = null
        */
       if(string == null || string.isEmpty() || string.length() < 7) return null;
       else if(!string.substring(0,7).contains("<a href")) return null;
       int len = string.length();
       String urlCharCharSurface = string.substring(9, len - 4);  // Returns : url">surface
       //urlCharCharSurface = removeSpecialCharacters(urlCharCharSurface);
       String[] urlSurface = urlCharCharSurface.split("\">");
       assert (urlSurface.length == 2);
       String decodedUrl = "";
       try {
               decodedUrl = decodeURL(urlSurface[0]);
               decodedUrl = removeSpecialCharacters(decodedUrl);
               urlSurface[0] = null;
               urlSurface[0] = decodedUrl;
       } catch (Exception e) {
               logger.severe("File : " + infile);
               logger.severe("Input : " + urlCharCharSurface);
               logger.severe("Exception: " + e.toString());
               logger.severe("URL Parsing failed : " + urlSurface[0]);
       }

       return urlSurface;
    }

    private String getDocText(String [] lines){
        /**
         * Helper function to convert all of the lines
         * in a single article to a single string
         */
        if(lines.length < 3) return null;
        StringBuilder doc = new StringBuilder();
        for(int i = 2; i < lines.length; i++){ // First line is url and second is title - Skip them
            if (!lines[i].trim().isEmpty()) {
                doc.append(lines[i]);
                doc.append("\n");
            }
        }

        String docText = doc.toString().trim();
        return docText;
    }

    private Pair<StringBuilder, Map<List<Integer>, String>> cleanDocText(String markedupText) {
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
	Map<List<Integer>, String> offsets2Title = new HashMap();
        Pattern linkPattern = Pattern.compile("(<a[^>]+>.+?</a>)",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
        if(markedupText == null){  // if text is null, do not attempt to find patterns.  It will lead to a null pointer exception
            return new Pair<StringBuilder,Map<List<Integer>, String>>(cleanText, offsets2Title);
        }
	Matcher matcher = linkPattern.matcher(markedupText);
        int len = markedupText.length();
        int oldStart = 0;
        int start = 0;
        while(matcher.find()){
            start = matcher.start();
            cleanText.append(markedupText.substring(oldStart, start));
            String[] urlSurface = _cleanHyperLink(matcher.group());
            if(urlSurface == null || urlSurface.length < 2) continue;
            if (urlSurface[0] != null) {
                List<Integer> offsets = new ArrayList();
                offsets.add(cleanText.length());
                offsets.add(cleanText.length()+urlSurface[1].length());
                offsets2Title.put(offsets, urlSurface[0]);
            }
            cleanText.append(urlSurface[1]);
            oldStart = matcher.end();
        }

        if (oldStart < len) cleanText.append(markedupText.substring(oldStart, len));
        return new Pair<StringBuilder,Map<List<Integer>, String>>(cleanText, offsets2Title);
    }

    public DataFields getFields(String doc){
        /*
            Helper function to get required data from
            a single document and creates a temporary
            DataFields object to store those data
        */
        String [] lines = doc.trim().split("\n");
        if(lines.length == 0) return null;
        assert (lines[0].startsWith("<doc id=")); // Line 1 should be <doc ... >

        // Gets Wikititle
        String title = lines[0].split("title=\"")[1];
        String wikiTitle = title.substring(0,title.length()-2); // Removes extra characters
        wikiTitle = removeSpecialCharacters(wikiTitle);
        if (wikiTitle.startsWith("List_of") || wikiTitle.startsWith("Lists_of")) return null;

        // Gets curID
        String firstLine = lines[0].split("curid=")[1];
        int index = firstLine.indexOf("\"");
        String id = firstLine.substring(0, index);
        int curId = Integer.parseInt(id);

        // Gets page title
        String pageTitle = lines[1];
        pageTitle = removeSpecialCharacters(pageTitle);

        // Gets text
        String text = getDocText(lines);
        return new DataFields(wikiTitle, pageTitle, curId, text);
    }

    public WikiPage parseDoc(String doc){
        /*
            Helper function to create a WikiPage
            object from a single document
        */
        if (!doc.trim().isEmpty()){
            DataFields dataObj = getFields(doc);
            if(dataObj == null) return null;  // Returns null if the wikipage is to be excluded.  This includes List pages.

            // Gets Text and internal hyperlinks
            Pair<StringBuilder, Map<List<Integer>, String>> cleanText2Offset = cleanDocText(dataObj.getText());
            String doctext = cleanText2Offset.getFirst().toString();

            Map<List<Integer>, String> hyperlinks = cleanText2Offset.getSecond();
            WikiPage wp = new WikiPage();
            wp.setWikiPageFields(dataObj.getWikiTitle(), dataObj.getPageTitle(), dataObj.getId(), doctext, hyperlinks);
            return wp;
        }
        else return null;
    }

    public static String[] breakDocs(String filename){
        /**
         * Takes the name of the file to parse as input
         * Returns: an array of documents as strings
         */
        String text = null;
        text = FileUtils.readFileToString(filename);
        if(text == null) return null;

        String [] docs = text.split("</doc>");
        return docs;
    }

    public List<WikiPage> parseFile(String filename){
        /*
            This breaks an entire file into multiple docs and parses
            them to get the required data.  It returns a list of wikipage
            objects from the file
        */
        List<WikiPage> data = new ArrayList();
        String [] docs = breakDocs(filename);
        if(docs == null) return data;
        for(int i = 0; i < docs.length; i++){
            String doc = docs[i];
            WikiPage wp = parseDoc(doc);
            if(wp != null) { // This can be null when the datafield object created from a single document is null
                data.add(wp);
            }
        }

        return data;
    }

    public void run(){
        try{
            List<WikiPage> res = parseFile(this.infile);
            JsonConverter.ConvertToJson(res);
        }catch(Exception e){
            logger.severe("Wiki Parsing failed : \nInFile : " + infile + " \nOutfile : " + outfile);
            logger.severe("Exception: " + e.toString());
        }
    }

}
