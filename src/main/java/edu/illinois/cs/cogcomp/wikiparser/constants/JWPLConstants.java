package edu.illinois.cs.cogcomp.wikiparser.constants;

/**
 *  This class stores the hard coded file paths for the JWPL input and output files
 */
public class JWPLConstants {
    // Output Dir
    public static final String jwplOutDir = "jwplOutputDir";

    // Input files
    public static final String categoryFile = "/shared/preprocessed/ngupta19/wikidatamachine/Category.txt"; 
    public static final String categoryPageFile = "/shared/preprocessed/ngupta19/wikidatamachine/category_pages.txt";
    public static final String pageMapFile = "/shared/preprocessed/ngupta19/wikidatamachine/PageMapLine.txt";
    
    // Output files for PageMapLine.txt
    public static final String pageIds = "allPageIds.txt"; // List of all page ids
    public static final String resPageIds = "resPageIds.txt"; // List of all resolved page ids
    public static final String pageIdsToTitles = "pageIds2PageTitles.txt"; // Map from page ids (first column) to page titles (second column)
    public static final String unresTitlesToResTitles = "pageTitles2resPageTitles.txt"; // Map from page titles (first column) to resolved page titles (second column) 
    public static final String resListPages = "resListPages.txt"; // List of page ids which belong to list pages
    
    // Output files for Category.txt
    public static final String idsToTitles = "categoryIds2CategoryTitles.txt"; // Map from category id (first column) to category title (second column)
    
    // Output files for category_pages.txt
    public static final String resIdsToCatTitles = "resPageIds2CategoryTitles.txt"; // Map from resolved cur ids to set of category titles
    public static final String resDisambPageIds = "resDisambPageIds.txt"; // List of resolved cur ids of disambiguation pages
    public static final String resNonDisambPageIds = "resNonDisambPageIds.txt"; // List of resolved cur ids of non-disambiguation pages
}
