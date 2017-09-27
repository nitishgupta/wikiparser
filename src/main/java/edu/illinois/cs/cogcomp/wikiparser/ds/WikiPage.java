package edu.illinois.cs.cogcomp.wikiparser.ds;

import java.util.*;
import edu.illinois.cs.cogcomp.wikiparser.utils.Pair;

/**
 *
 * @author Reuben-PC
 * 
 * Data Structure to hold all of the required data fields of a wiki page
 */
public class WikiPage implements java.io.Serializable {
    private String wikiTitle;  // This is the title in the hyperlink with underscores and decoded url
    private String pageTitle;  // Title of the page visible in the Wikipedia webpage.  This includes spaces.
    private Integer curId;  // Integer ID that is unique to every wikipedia page
    private String text;  // Relevant text in the page.  Sentences in the same paragraphs are separated by spaces and paragraphs are separated by a new line
    private Map<Pair<Integer, Integer>, String> hyperlinks;  // Internal wiki links in the page.  The first integer is the start and the second is the end char offset in the text field.  String is the link itself.
    
    public WikiPage(){
        this.wikiTitle = null;
        this.pageTitle = null;
        this.text = null;
        this.curId = null;
        this.hyperlinks = null;
    }
    
    public WikiPage(String wikiTitle, String pageTitle, Integer curId){
        this.wikiTitle = wikiTitle;
        this.pageTitle = pageTitle;
        this.curId = curId;
        this.text = null;
        this.hyperlinks = null;
    }
    
    public String getWikiTitle(){
        return this.wikiTitle;
    }
    
    public String PageTitle(){
        return this.pageTitle;
    }
    
    public String getText(){
        return this.text;
    }
    
    public int getCurId(){
        return this.curId;
    }
    
    public Map<Pair<Integer, Integer>, String> getHyperlinks(){
        return this.hyperlinks;
    }
    
    public void setWikiTitle(String wikiTitle){
        this.wikiTitle = wikiTitle;
    }
    
    public void setPageTitle(String pageTitle){
        this.pageTitle = pageTitle;
    }
    
    public void setCurId(Integer curId){
        this.curId = curId;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setLinks(Map<Pair<Integer, Integer>, String> hyperlinks){
        this.hyperlinks = hyperlinks;
    }
    
    public void setWikiPageFields(String wikiTitle, String pageTitle, int curId, String text, Map<Pair<Integer, Integer>, String> hyperlinks){
        this.wikiTitle = wikiTitle;
        this.pageTitle = pageTitle;
        this.curId = curId;
        this.text = text;
        this.hyperlinks = hyperlinks;
    }
}
