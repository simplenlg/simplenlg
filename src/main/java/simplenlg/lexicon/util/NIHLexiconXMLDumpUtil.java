/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, and Saad Mahamood.
 */
package simplenlg.lexicon.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;

import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;


/**
 * <p>This class reads in a CSV word list, looks up the words in the NIH lexicon, 
 * and writes the XML words into an output file. This XML file can then be used as the XML Lexicon source for SimpleNLG.</p>
 * 
 * @author Ehud Reiter
 */
public class NIHLexiconXMLDumpUtil {
	
	// filenames
	private static String DB_FILENAME;  // DB location
	private static String WORDLIST_FILENAME;  // word list
	private static String XML_FILENAME;  // word list

	/**
	 * This main method reads a list of CSV words and POS tags and looks up against 
	 * the NIHDB Lexicon for a corresponding entry. If found the baseform is written out into a XML 
	 * file, which can be used in SimpleNLG or elsewhere. 
	 * 
	 * @param args : List of Arguments that this command line application must be provided with in order:
	 * <ol>
	 * 		<li>The full path to the NIHDB Lexicon database file e.g. C:\\NIHDB\\lexAccess2009</li>
	 * 		<li>The full path to the list of baseforms and POS tags to include in the written out XML Lexicon file</li>
	 * 		<li>The full path to the XML file that the XML Lexicon will be written out to.</li>
	 * </ol>
	 * 
	 *<p>Example usage: 
	 *   java simplenlg.lexicon.util.NIHLexiconXMLDumpUtil C:\\NIHDB\\lexAccess2009 C:\\NIHDB\\wordlist.csv C:\\NIHDB\\default-lexicon.xml
	 *   
	 *   You will need to have the HSQLDB driver (org.hsqldb.jdbc.JDBCDriver) on your Java classpath before running this application.
	 *</p>
	 */
	public static void main(String[] args) {
        Lexicon lex = null;
     
        if(args.length == 3) {
        	
        	DB_FILENAME = args[0];
        	WORDLIST_FILENAME = args[1];
        	XML_FILENAME = args[2];
        	
        	// Check to see if the HSQLDB driver is available on the classpath:
        	boolean dbDriverAvaliable = false;
        	try {
        		Class<?> driverClass = Class.forName("org.hsqldb.jdbc.JDBCDriver", false, NIHLexiconXMLDumpUtil.class.getClassLoader());
        		if(null != driverClass) {
        			dbDriverAvaliable = true;
        		}
        	} catch(ClassNotFoundException cnfe) {
        		System.err.println("*** Please add the HSQLDB JDBCDriver to your Java classpath and try again.");
        	}
        	
        	if((null != DB_FILENAME && !DB_FILENAME.isEmpty()) && 
        			(null != WORDLIST_FILENAME && !WORDLIST_FILENAME.isEmpty()) && 
        			(null != XML_FILENAME && !XML_FILENAME.isEmpty()) && dbDriverAvaliable) {
	        	lex = new NIHDBLexicon(DB_FILENAME);
	        	
				try {
					LineNumberReader wordListFile = new LineNumberReader(new FileReader (WORDLIST_FILENAME));
					FileWriter xmlFile = new FileWriter(XML_FILENAME);
					xmlFile.write(String.format("<lexicon>%n"));
					String line = wordListFile.readLine();
					while (line != null) {
						String[] cols = line.split(",");
						String base = cols[0];
						String cat = cols[1];
						WordElement word = null;
						if (cat.equalsIgnoreCase("noun"))
							word = lex.getWord(base, LexicalCategory.NOUN);
						else if (cat.equalsIgnoreCase("verb"))
							word = lex.getWord(base, LexicalCategory.VERB);
						else if (cat.equalsIgnoreCase("adv"))
							word = lex.getWord(base, LexicalCategory.ADVERB);
						else if (cat.equalsIgnoreCase("adj"))
							word = lex.getWord(base, LexicalCategory.ADJECTIVE);
						else if (cat.equalsIgnoreCase("det"))
							word = lex.getWord(base, LexicalCategory.DETERMINER);
						else if (cat.equalsIgnoreCase("prep"))
							word = lex.getWord(base, LexicalCategory.PREPOSITION);
						else if (cat.equalsIgnoreCase("pron"))
							word = lex.getWord(base, LexicalCategory.PRONOUN);
						else if (cat.equalsIgnoreCase("conj"))
							word = lex.getWord(base, LexicalCategory.CONJUNCTION);
						else if (cat.equalsIgnoreCase("modal"))
							word = lex.getWord(base, LexicalCategory.MODAL);
						else if (cat.equalsIgnoreCase("interjection"))
							word = lex.getWord(base, LexicalCategory.NOUN); // Kilgarriff;s interjections are mostly nouns in the lexicon
						
						if (word == null)
							System.out.println("*** The following baseform and POS tag is not found: " + base + ":" + cat);
						else
							xmlFile.write(word.toXML());
						line = wordListFile.readLine();;
					}
					xmlFile.write(String.format("</lexicon>%n"));
					wordListFile.close();
					xmlFile.close();
					
					lex.close();
					
					System.out.println("*** XML Lexicon Export Completed.");
					
				} catch (Exception e) {
					System.err.println("*** An Error occured during the export. The Exception message is below: ");
					System.err.println(e.getMessage());
					System.err.println("************************");
					System.err.println("Please make sure you have the correct application arguments: ");
					printArgumentsMessage();
				}			
        	}
        	else {
        		printErrorArgumentMessage();
        	}
        } else {
        	printErrorArgumentMessage();
        }
	}
	
	/**
	 * Prints Arguments Error Messages if incorrect or not enough parameters have been supplied. 
	 */
	private static void printErrorArgumentMessage() {
		System.err.println("Insuffient number of arguments supplied. Please supply the following Arguments: \n");
		printArgumentsMessage();
	}
	
	/**
	 * Prints this utility applications arguments requirements. 
	 */
	private static void printArgumentsMessage() {
		System.err.println("\t\t 1. The full path to the NIHDB Lexicon database file e.g. C:\\NIHDB\\lexAccess2009 ");
    	System.err.println("\t\t 2. The full path to the list of baseforms and POS tags to include in the written out XML Lexicon file");
    	System.err.println("\t\t 3. The full path to the XML file that the XML Lexicon will be written out to.");
	}
	
	
	
	
	

}
