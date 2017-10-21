package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;

/**
 *  This class will call the functions to parse PageMapLine.txt,
 *  Category.txt and category_pages.txt.  The outputs of these
 *  functions will be written to text files in the respective folders.
 */
public class JWPLParser {
    public static void main(String [] args){
        String outputDir = args[0];
        
        System.out.println("Starts parsing PageMapLine.txt");
        PageMapLineParser parser1 = new PageMapLineParser(outputDir);
        parser1.parsePageMap(JWPLConstants.pageMapFile);

        System.out.println("Starts parsing Category.txt");
        CategoryParser parser2 = new CategoryParser(outputDir);
        parser2.parseCategory(JWPLConstants.categoryFile);

        System.out.println("Starts parsing Page_Categories.txt");
        CategoryPagesParser parser3 = new CategoryPagesParser(outputDir);
        parser3.parseCategoryPages(JWPLConstants.categoryPageFile);
    }
}
