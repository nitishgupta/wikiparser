package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;

/**
 *  This class will call the functions to parse PageMapLine.txt,
 *  Category.txt and category_pages.txt.  The outputs of these
 *  functions will be written to text files in the respective folders.
 */
public class JWPLParser {
    public static void main(String [] args){
        String file = "C:\\Users\\Reuben-PC\\Desktop\\wikidatamachine\\PageMapLine_test.txt";
        PageMapLineParser parser = new PageMapLineParser();
        System.out.println("Starts parsing PageMapLine.txt");
        parser.parsePageMap(JWPLConstants.pageMapFile);
    }
}
