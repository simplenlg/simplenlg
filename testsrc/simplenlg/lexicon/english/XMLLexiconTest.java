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

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplenlg.lexicon.XMLLexicon;

/**
 * @author D. Westwater, Data2Text Ltd
 * 
 */
public class XMLLexiconTest extends TestCase {

	// lexicon object -- an instance of Lexicon
	XMLLexicon lexicon = null;

	// lexicon location - omit, use default lexicon instead
	//static String XML_FILENAME = "res/simple-lexicon.xml";

	@Override
	@Before
	/*
	 * * Sets up the accessor and runs it -- takes ca. 26 sec
	 */
	public void setUp() {
		long startTime = System.currentTimeMillis();
		//this.lexicon = new XMLLexicon(XML_FILENAME); // omit, use default
														// lexicon instead
		this.lexicon = new XMLLexicon();
		long stopTime = System.currentTimeMillis();
		;
		System.out.format("Loading XML lexicon took %d ms%n",
                          stopTime - startTime);

	}

	/*
	 * close the lexicon
	 */
	@Override
	@After
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		if (lexicon != null)
			lexicon.close();
	}

	@Test
	public void testBasics() {
		SharedLexiconTests.doBasicTests(lexicon);
	}

}
