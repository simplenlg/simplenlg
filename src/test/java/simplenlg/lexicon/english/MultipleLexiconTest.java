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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.lexicon.english;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplenlg.features.LexicalFeature;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.MultipleLexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.lexicon.XMLLexicon;

/**
 * @author Dave Westwater, Data2Text Ltd
 *
 */
public class MultipleLexiconTest {

	// NIH, XML lexicon location
	static String DB_FILENAME = "src/test/resources/NIHLexicon/lexAccess2013.data";
	static String XML_FILENAME = "src/main/resources/default-lexicon.xml";
	
	// multi lexicon
	MultipleLexicon lexicon;


	@Before
	public void setUp() throws Exception {
        try {
            Properties prop = new Properties();
            FileReader reader = new FileReader(new File("./src/main/resources/lexicon.properties"));
            prop.load(reader);

            String xmlFile = prop.getProperty("XML_FILENAME");
            String dbFile = prop.getProperty("DB_FILENAME");
            
            this.lexicon = new MultipleLexicon(new XMLLexicon(xmlFile),
                                               new NIHDBLexicon(dbFile));
        } catch (Exception e) {
            e.printStackTrace();
            this.lexicon = new MultipleLexicon(new XMLLexicon(XML_FILENAME),
                                               new NIHDBLexicon(DB_FILENAME));
        }
	}

	@After
	public void tearDown() throws Exception {
		lexicon.close();
	}

	@Test
	public void basicLexiconTests() {
		SharedLexiconTests tests = new SharedLexiconTests();
		tests.doBasicTests(lexicon);
	}
	
	@Test
	public void multipleSpecificsTests() {
		// try to get word which is only in NIH lexicon
		WordElement UK = lexicon.getWord("UK");
		
		Assert.assertEquals(true, UK.getFeatureAsString(LexicalFeature.ACRONYM_OF).contains("United Kingdom"));

		// test alwaysSearchAll flag
		boolean alwaysSearchAll = lexicon.isAlwaysSearchAll();
		
		// tree as noun exists in both, but as verb only in NIH
		lexicon.setAlwaysSearchAll(true);
		Assert.assertEquals(3, lexicon.getWords("tree").size()); // 3 = once in XML plus twice in NIH

		lexicon.setAlwaysSearchAll(false);
		Assert.assertEquals(1, lexicon.getWords("tree").size());

		// restore flag to original state
		lexicon.setAlwaysSearchAll(alwaysSearchAll);	
	}


}
