package edu.illinois.cs.cogcomp.wikiparser.ds;

import java.util.*;

/**
 *
 * @author Reuben-PC
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
