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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.lexicon;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;

import java.util.Properties;

import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;

// this class reads in a word list, looks up the words in the NIH lexicon,
// and writes the XML words into an output file
public class dumpXML {
	
	// filenames
	static final String DB_FILENAME = "E:\\NIHDB\\lexAccess2009";  // DB location
	static final String WORDLIST_FILENAME = "E:\\NIHDB\\wordlist.csv";  // word list
	static final String XML_FILENAME = "E:\\NIHDB\\default-lexicon.xml";  // word list

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        Lexicon lex = null;
        // use property file for the lexicon
        try {
            Properties prop = new Properties();
            prop.load(dumpXML.class.getClassLoader().
                      getResourceAsStream("lexicon.properties"));
            
            String dbFile = prop.getProperty("DB_FILENAME");
            
            if (null != dbFile)
                lex = new NIHDBLexicon(dbFile);
        } catch (Exception e) {
            lex = new NIHDBLexicon(DB_FILENAME);
        }

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
					System.out.println("Missing " + base + ":" + cat);
				else
					xmlFile.write(word.toXML());
				line = wordListFile.readLine();;
			}
			xmlFile.write(String.format("</lexicon>%n"));
			wordListFile.close();
			xmlFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lex.close();
		System.out.println("done");

	}

}
