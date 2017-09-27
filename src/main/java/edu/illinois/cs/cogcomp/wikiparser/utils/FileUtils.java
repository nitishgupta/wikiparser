/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.illinois.cs.cogcomp.wikiparser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Reuben-PC
 */
public class FileUtils {
    final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static String getTextFromFile(String filename, String charset) {
        try {
                if (filename.endsWith(".gz")) {
                        return IOUtils.toString(new GZIPInputStream(new FileInputStream(filename)), charset);
                } else {
                        return org.apache.commons.io.FileUtils.readFileToString(new File(filename), charset);
                }
        } catch (Exception e) {
                logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    public static String readFileToString(String filename) {
	return getTextFromFile(filename, "UTF8");
    }
}
