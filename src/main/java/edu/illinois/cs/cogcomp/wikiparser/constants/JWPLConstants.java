package edu.illinois.cs.cogcomp.wikiparser.constants;

/**
 *  This class stores the hard coded file paths for the JWPL input and output files
 */
public class JWPLConstants {
    // Output Dir
    public static final String jwplOutDir = "jwplOutputDir";

    // Input files
    public static final String categoryFile = "Category.txt";
    public static final String categoryPageFile = "category_pages.txt";
    public static final String pageMapFile = "PageMapLine.txt";
    
    // Output files for PageMapLine.txt
    public static final String pageIds = "allPageIds.txt"; // List of all page ids
    public static final String resPageIds = "resPageIds.txt"; // List of all resolved page ids
    public static final String pageIdsToTitles = "pageIds2PageTitles.txt"; // Map from page ids (first column) to page titles (second column)
    public static final String unresTitlesToResTitles = "pageTitles2resPageTitles.txt"; // Map from page titles (first column) to resolved page titles (second column) 
    public static final String resListPages = "resListPages.txt"; // List of page ids which belong to list pages

    public static final String curId2Title = "curIds2Title.tsv"; // Map from page ids (first column) to page titles (second column)
    public static final String resCurId2ResTitle = "resCurId2ResTitle.tsv"; // Map from page titles (first column) to resolved page titles (second column)
    public static final String resCurId2ResTitle_nonList = "resCurId2ResTitle_nonList.tsv"; // List of page ids which belong to list pages
    public static final String resListPageCurId2ResTitle = "resListPageCurId2ResTitle.tsv"; // List of page ids which belong to list pages


    
    // Output files for Category.txt
    // Map from category id (first column) to category title (second column)
    public static final String catIdToCatTitle = "categoryId2CategoryTitle.tsv";
    
    // Output files for category_pages.txt
    public static final String resCurIdToCatTitles = "resCurId2CategoryTitles.tsv"; // Map from resolved cur ids to set of category titles
    public static final String resCurIdNonDisambig2ResTitle = "resCurIdNonDisambig2ResTitle.tsv"; // List of page ids which belong to list pages
    public static final String resCurIdDisambig2ResTitle = "resCurIdDisambig2ResTitle.tsv"; // List of page ids which belong to list pages
    public static final String resCurIdNonDisambig2ResTitle_nonList = "resCurIdNonDisambig2ResTitle_nonList.tsv"; // List of page ids which belong to list pages
}
