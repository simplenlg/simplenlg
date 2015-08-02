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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * @author D. Westwater, Data2Text Ltd
 * 
 */
public class XMLLexiconTest {

	// lexicon object -- an instance of Lexicon
	XMLLexicon lexicon = null;


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
	
		System.out.format("Loading XML lexicon took %d ms%n",
                          stopTime - startTime);

	}

	/**
	 * Close the lexicon and cleanup.
	 */
	@After
	public void tearDown() throws Exception {
		if (lexicon != null)
			lexicon.close();
	}
	
	/**
	 * Runs basic Lexicon tests.
	 */
	@Test
	public void basicLexiconTests() {
		SharedLexiconTests tests = new SharedLexiconTests();
		tests.doBasicTests(lexicon);
	}
	
	/**
	 * Tests the immutability of the XMLLexicon by checking to make sure features 
	 * are not inadvertently propagated to the canonical XMLLexicon WordElement object.
	 */
	@Test
	public void xmlLexiconImmutabilityTest() {
	    NLGFactory factory = new NLGFactory(lexicon);
	    Realiser realiser = new Realiser(lexicon);

	    // "wall" is singular.
	    NPPhraseSpec wall = factory.createNounPhrase("the", "wall");
	    Assert.assertEquals(NumberAgreement.SINGULAR, wall.getFeature(Feature.NUMBER));    

	    // Realise a sentence with plural form of "wall"
	    wall.setPlural(true);       
	    SPhraseSpec sentence = factory.createClause("motion", "observe");
	    sentence.setFeature(Feature.TENSE, Tense.PAST); 
	    PPPhraseSpec pp = factory.createPrepositionPhrase("in", wall);
	    sentence.addPostModifier(pp);
	    realiser.realiseSentence(sentence);

	    // Create a new 'the wall' NP and check to make sure that the syntax processor has
	    // not propagated plurality to the canonical XMLLexicon WordElement object.
	    wall = factory.createNounPhrase("the", "wall");
	    Assert.assertEquals(NumberAgreement.SINGULAR, wall.getFeature(Feature.NUMBER));    
	}

}
