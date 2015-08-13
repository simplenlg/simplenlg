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

package simplenlg.syntax.english;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.StringElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * Tests for string elements as parts of larger phrases
 * 
 * @author bertugatt
 * 
 */
public class StringElementTest {

	private Lexicon lexicon = null;
	private NLGFactory phraseFactory = null;
	private Realiser realiser = null;
	
	@Before
	public void setup() {
		lexicon = Lexicon.getDefaultLexicon();
		phraseFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
	}
	
	@After
	public void tearDown() {
		lexicon = null;
		phraseFactory = null;
		realiser = null;
	}

	/**
	 * Test that string elements can be used as heads of NP
	 */
	@Test
	public void stringElementAsHeadTest() {
		NPPhraseSpec np = this.phraseFactory.createNounPhrase();
		np.setHead(phraseFactory.createStringElement("dogs and cats"));
		np.setSpecifier(phraseFactory.createWord("the",
				LexicalCategory.DETERMINER));
		assertEquals("the dogs and cats", this.realiser.realise(np)
				.getRealisation());
	}

	/**
	 * Sentences whose VP is a canned string
	 */
	@Test
	public void stringElementAsVPTest() {
		SPhraseSpec s = this.phraseFactory.createClause();
		s.setVerbPhrase(this.phraseFactory.createStringElement("eats and drinks"));
		s.setSubject(this.phraseFactory.createStringElement("the big fat man"));
		assertEquals("the big fat man eats and drinks", this.realiser
				.realise(s).getRealisation());
	}
	
	/**
	 * Test for when the SPhraseSpec has a NPSpec added directly after it:
	 * "Mary loves NP[the cow]."
	 */
	@Test
	public void tailNPStringElementTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		senSpec.addComplement((this.phraseFactory.createStringElement("mary loves")));
		NPPhraseSpec np = this.phraseFactory.createNounPhrase();
		np.setHead("cow");
		np.setDeterminer("the");
		senSpec.addComplement(np);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("Mary loves the cow.", this.realiser.realise(completeSen).getRealisation());
	}
	
	/**
	 * Test for a NP followed by a canned text: "NP[A cat] loves a dog".
	 */
	@Test
	public void frontNPStringElementTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		NPPhraseSpec np = this.phraseFactory.createNounPhrase();
		np.setHead("cat");
		np.setDeterminer("the");
		senSpec.addComplement(np);
		senSpec.addComplement(this.phraseFactory.createStringElement("loves a dog"));
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("The cat loves a dog.", this.realiser.realise(completeSen).getRealisation());	
	}
	
	
	/**
	 * Test for a StringElement followed by a NP followed by a StringElement
	 * "The world loves NP[ABBA] but not a sore loser."
	 */
	@Test
	public void mulitpleStringElementsTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		senSpec.addComplement(this.phraseFactory.createStringElement("the world loves"));
		NPPhraseSpec np = this.phraseFactory.createNounPhrase();
		np.setHead("ABBA");
		senSpec.addComplement(np);
		senSpec.addComplement(this.phraseFactory.createStringElement("but not a sore loser"));
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("The world loves ABBA but not a sore loser.", this.realiser.realise(completeSen).getRealisation());	
	}
	
	/**
	 * Test for multiple NP phrases with a single StringElement phrase:
	 * "NP[John is] a trier NP[for cheese]."
	 */
	@Test
	public void mulitpleNPElementsTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		NPPhraseSpec frontNoun = this.phraseFactory.createNounPhrase();
		frontNoun.setHead("john");
		senSpec.addComplement(frontNoun);
		senSpec.addComplement(this.phraseFactory.createStringElement("is a trier"));
		NPPhraseSpec backNoun = this.phraseFactory.createNounPhrase();
		backNoun.setDeterminer("for");
		backNoun.setNoun("cheese");
		senSpec.addComplement(backNoun);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("John is a trier for cheese.", this.realiser.realise(completeSen).getRealisation());
		
	}
	
	
	/**
	 * White space check: Test to see how SNLG deals with additional whitespaces: 
	 * 
	 * NP[The Nasdaq] rose steadily during NP[early trading], however it plummeted due to NP[a shock] after NP[IBM] announced poor 
     * NP[first quarter results].
	 */
	@Test
	public void whiteSpaceNPTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
		firstNoun.setDeterminer("the");
		firstNoun.setNoun("Nasdaq");
		senSpec.addComplement(firstNoun);
		senSpec.addComplement(this.phraseFactory.createStringElement(" rose steadily during "));
		NPPhraseSpec secondNoun = this.phraseFactory.createNounPhrase();
		secondNoun.setSpecifier("early");
		secondNoun.setNoun("trading");
		senSpec.addComplement(secondNoun);
		senSpec.addComplement(this.phraseFactory.createStringElement(" , however it plummeted due to"));
		NPPhraseSpec thirdNoun = this.phraseFactory.createNounPhrase();
		thirdNoun.setSpecifier("a");
		thirdNoun.setNoun("shock");
		senSpec.addComplement(thirdNoun);
		senSpec.addComplement(this.phraseFactory.createStringElement(" after "));
		NPPhraseSpec fourthNoun = this.phraseFactory.createNounPhrase();
		fourthNoun.setNoun("IBM");
		senSpec.addComplement(fourthNoun);
		senSpec.addComplement(this.phraseFactory.createStringElement("announced poor    "));
		NPPhraseSpec fifthNoun = this.phraseFactory.createNounPhrase();
		fifthNoun.setSpecifier("first quarter");
		fifthNoun.setNoun("results");
		fifthNoun.setPlural(true);
		senSpec.addComplement(fifthNoun);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("The Nasdaq rose steadily during early trading, however it plummeted due to a shock after IBM announced poor first quarter results.", 
				this.realiser.realise(completeSen).getRealisation());
	}
	
	/**
	 * Point absorption test: Check to see if SNLG respects abbreviations at the end of a sentence.
	 * "NP[Yahya] was sleeping his own and dreaming etc."
	 */
	
	public void pointAbsorptionTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
		firstNoun.setNoun("yaha");
		senSpec.addComplement(firstNoun);
		senSpec.addComplement("was sleeping on his own and dreaming etc.");
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("Yaha was sleeping on his own and dreaming etc.", 
				this.realiser.realise(completeSen).getRealisation());
		
		
	}
	
	/**
	 * Point absorption test: As above, but with trailing white space.
	 * "NP[Yaha] was sleeping his own and dreaming etc.      "
	 */
	public void pointAbsorptionTrailingWhiteSpaceTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
		firstNoun.setNoun("yaha");
		senSpec.addComplement(firstNoun);
		senSpec.addComplement("was sleeping on his own and dreaming etc.      ");
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("Yaha was sleeping on his own and dreaming etc.", 
				this.realiser.realise(completeSen).getRealisation());
	}
	
	/**
	 * Abbreviation test: Check to see how SNLG deals with abbreviations in the middle of a sentence.
	 * 
	 * "NP[Yahya] and friends etc. went to NP[the park] to play."
	 */
	@Test
	public void middleAbbreviationTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase();
		firstNoun.setNoun("yahya");
		senSpec.addComplement(firstNoun);
		senSpec.addComplement(this.phraseFactory.createStringElement("and friends etc. went to"));
		NPPhraseSpec secondNoun = this.phraseFactory.createNounPhrase();
		secondNoun.setDeterminer("the");
		secondNoun.setNoun("park");
		senSpec.addComplement(secondNoun);
		senSpec.addComplement("to play");
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("Yahya and friends etc. went to the park to play.", 
				this.realiser.realise(completeSen).getRealisation());
	}

	
	/**
	 * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
	 * "I see an NP[elephant]" 
	 */
	@Test
	public void stringIndefiniteArticleInflectionVowelTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		senSpec.addComplement(this.phraseFactory.createStringElement("I see a"));
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("elephant");
		senSpec.addComplement(firstNoun);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("I see an elephant.", 
				this.realiser.realise(completeSen).getRealisation());
		
	}
	
	/**
	 * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
	 * "I see NP[a elephant]" --> 
	 */
	@Test
	public void NPIndefiniteArticleInflectionVowelTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		senSpec.addComplement(this.phraseFactory.createStringElement("I see"));
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("elephant");
		firstNoun.setDeterminer("a");
		senSpec.addComplement(firstNoun);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		assertEquals("I see an elephant.", 
				this.realiser.realise(completeSen).getRealisation());
		
	}
	
	
	/**
	 * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
	 * "I see an NP[cow]" 
	 */
	@Test
	public void stringIndefiniteArticleInflectionConsonantTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		senSpec.addComplement(this.phraseFactory.createStringElement("I see an"));
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("cow");
		senSpec.addComplement(firstNoun);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		// Do not attempt "an" -> "a"
		assertNotSame("I see an cow.", 
				this.realiser.realise(completeSen).getRealisation());
	}
	
	/**
	 * Indefinite Article Inflection: StringElement to test how SNLG handles a/an situations.
	 * "I see NP[an cow]" --> 
	 */
	@Test
	public void NPIndefiniteArticleInflectionConsonantTest() {
		SPhraseSpec senSpec = this.phraseFactory.createClause();
		senSpec.addComplement(this.phraseFactory.createStringElement("I see"));
		NPPhraseSpec firstNoun = this.phraseFactory.createNounPhrase("cow");
		firstNoun.setDeterminer("an");
		senSpec.addComplement(firstNoun);
		DocumentElement completeSen = this.phraseFactory.createSentence();
		completeSen.addComponent(senSpec);
		// Do not attempt "an" -> "a"
		assertEquals("I see an cow.", 
				this.realiser.realise(completeSen).getRealisation());
	}
	
	
	/**
	 * aggregationStringElementTest: Test to see if we can aggregate two StringElements in a CoordinatedPhraseElement.
	 */
	@Test
	public void aggregationStringElementTest() {
		
		CoordinatedPhraseElement coordinate = 
				phraseFactory.createCoordinatedPhrase(new StringElement("John is going to Tesco"), 
						                              new StringElement("Mary is going to Sainsburys")); 
	    SPhraseSpec sentence = phraseFactory.createClause();
	    sentence.addComplement(coordinate);
	    
	    assertEquals("John is going to Tesco and Mary is going to Sainsburys.", 
				realiser.realiseSentence(sentence));
	}
	
}
