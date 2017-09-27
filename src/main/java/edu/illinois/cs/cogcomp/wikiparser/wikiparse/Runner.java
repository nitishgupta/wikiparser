/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import java.io.File;
import edu.illinois.cs.cogcomp.wikiparser.jwpl.DataMachine;

/**
 *  This class acts as the new entry point for the wikiparser code.  It checks if the output folder
 *  of the JWPL code already exists.  If it does not, run the DataMachine main function. Next, it
 *  calls extractWiki function in the WikiExtractParser class.
 */
public class Runner {
    public static void main(String [] args){
        String jwplInputDir = args[0];
        WikiExtractParser wikiparser = new WikiExtractParser();
        wikiparser.wikiDirectory = args[1];
        wikiparser.outputDir = args[2];
        
        String jwplOutput = jwplInputDir + "\\\\output";  // Gets the path of the JWPL output folder
        File f = new File(jwplOutput);
        
        if(!f.exists() && !f.isDirectory()){  // Runs DataMachine if output folder does not exist
           try{
               DataMachine.runDM(jwplInputDir);
           } catch(Exception e){
               wikiparser.logger.severe(e.toString());
           }
        }
        
        wikiparser.logger.info("Starting to Parse Wiki Texts");
        wikiparser.extractWiki();
    }
}
