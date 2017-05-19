/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wiki.parser;

import java.util.*;

/**
 *
 * @author Reuben-PC
 * 
 * Data Structure to hold all of the required data fields of a wiki page
 */
public class wikidata {
    private final String wikiTitle;
    private final String pageTitle;
    private final int curId;
    private final String text;
    private final Map<Pair<Integer, Integer>, String> hyperlinks;
    
    public wikidata(String wikiTitle, String pageTitle, String text, int curId, Map<Pair<Integer, Integer>, String> hyperlinks){
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
