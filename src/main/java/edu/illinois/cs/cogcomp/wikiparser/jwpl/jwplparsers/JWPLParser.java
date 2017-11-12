package edu.illinois.cs.cogcomp.wikiparser.jwpl.jwplparsers;

import java.io.File;
import java.nio.file.Paths;

import edu.illinois.cs.cogcomp.wikiparser.constants.JWPLConstants;

/**
 *  This class will call the functions to parse PageMapLine.txt,
 *  Category.txt and category_pages.txt.  The outputs of these
 *  functions will be written to text files in the respective folders.
 */
public class JWPLParser {
    public static void main(String [] args){
        String jwplFilesDir = args[0];
        String outputDir = args[1];

        File f = new File(outputDir);
        if(!f.exists()) {
            System.out.println("JWPL Parser Output Dir doesn't exist");
            System.exit(0);
        }

        String pageMapFile = Paths.get(jwplFilesDir, JWPLConstants.pageMapFile).toString();
        String Category = Paths.get(jwplFilesDir, JWPLConstants.categoryFile).toString();
        String CategoryPages = Paths.get(jwplFilesDir, JWPLConstants.categoryPageFile).toString();

        System.out.println("Parsing JWPL Output files ...");

        System.out.println("[#] Parsing PageMapLine.txt");
        PageMapLineParser parser1 = new PageMapLineParser(outputDir);
        parser1.parsePageMap(pageMapFile);

        System.out.println("[#] Parsing Category.txt");
        CategoryParser parser2 = new CategoryParser(outputDir);
        parser2.parseCategory(Category);

        System.out.println("[#] Parsing category_pages.txt");
        CategoryPagesParser parser3 = new CategoryPagesParser(outputDir);
        parser3.parseCategoryPages(CategoryPages);
    }
}
