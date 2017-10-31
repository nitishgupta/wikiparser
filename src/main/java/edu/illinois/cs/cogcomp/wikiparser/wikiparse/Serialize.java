package edu.illinois.cs.cogcomp.wikiparser.wikiparse;

import edu.illinois.cs.cogcomp.wikiparser.ds.WikiPage;
import java.io.*;
import java.util.*;

/**
 *
 * @author Reuben-PC
 * 
 * This class provides helper functions to serialize lists of WikiPage data structures
 * and write them to files in the tmp directory.  The directory can be changed by 
 * the user if need be.  To deserialize the files, simply pass the path of the files to
 * deserialize function.  The return type of this function may be changed in the future 
 * as befits our purposes.
 */
public class Serialize {

    public static void serialize(List<WikiPage> data, String outfile){
        /**
        * Performs object serialization on a list of WikiPage data 
        * 
        * @param data - a list of WikiPage data structures
        * @param outfile - path of directory where file is to be written too
        */
        try {
         FileOutputStream fileOut = new FileOutputStream(outfile);
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(data);
         out.close();
         fileOut.close();
         System.out.println("Serialized data is saved in " + outfile);
      }catch(IOException e) {
         e.printStackTrace();
      }
    }
    
    public static void deserialize(String filename){
        /**
        * Performs deserialization on the object file
        * 
        * *** Important note: Return type of this function may change in the future.
        *                     It is set to void for now but we may have to change it
        *                     according to future utilization of this function.
        * 
        * @param filename - path and name of file to be deserialized back into 
        *                   a list of WikiPage object
        */
        List<WikiPage> res = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            res = (List<WikiPage>)in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException e) {
            System.out.println("IOException has occurred.");
            e.printStackTrace();
            return;
        }catch(ClassNotFoundException e) {
              System.out.println("ClassNotFoundException has occurred.");
              e.printStackTrace();
              return;
        }
        
        if(res != null) System.out.println("Size of list is " + res.size());
    }
}
