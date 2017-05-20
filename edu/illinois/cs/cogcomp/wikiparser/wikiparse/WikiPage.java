package wiki.parser;

import java.util.*;
import utils.Pair;

/**
 *
 * @author Reuben-PC
 * 
 * Data Structure to hold all of the required data fields of a wiki page
 */
public class WikiPage {
    private final String wikiTitle;
    private final String pageTitle;
    private final int curId;
    private final String text;
    private final Map<Pair<Integer, Integer>, String> hyperlinks;
    
    public WikiPage(String wikiTitle, String pageTitle, String text, int curId, Map<Pair<Integer, Integer>, String> hyperlinks){
        this.wikiTitle = wikiTitle;
        this.pageTitle = pageTitle;
        this.text = text;
        this.curId = curId;
        this.hyperlinks = hyperlinks;
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
}
