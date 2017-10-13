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
        WikiExtractParser wikiparser = new WikiExtractParser();
        wikiparser.wikiDirectory = args[0];
        wikiparser.outputDir = args[1];
        wikiparser.logger.info("Starting to Parse Wiki Texts");
        wikiparser.extractWiki();
    }
}
