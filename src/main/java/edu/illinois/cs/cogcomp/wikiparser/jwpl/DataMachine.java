/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.illinois.cs.cogcomp.wikiparser.jwpl;

import de.tudarmstadt.ukp.wikipedia.datamachine.domain.JWPLDataMachine;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
 
/**
 * This class provides the runDM function that is used to call the JWPL DataMachine function.
 */
public class DataMachine {
    public static void runDM(String jwplInputDir) throws Exception {
		System.setProperty("jdk.xml.totalEntitySizeLimit", "500000000");
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		// Path where wiki dump is stored.
		String[] arg = {"english", "Contents", "Disambiguation_pages", jwplInputDir};
		JWPLDataMachine.main(arg);
	}
}
