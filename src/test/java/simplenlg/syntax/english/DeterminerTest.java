/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * https://www.mozilla.org/en-US/MPL/
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
package simplenlg.syntax.english;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * Some determiner tests -- in particular for indefinite articles like "a" or "an".
 *
 * @author Saad Mahamood, Data2Text Limited.
 */
public class DeterminerTest {

	/**
	 * The realiser.
	 */
	private Realiser realiser;

	private NLGFactory phraseFactory;

	private Lexicon lexicon;

	private final String DB_FILENAME = "src/test/resources/NIHLexicon/lexAccess2013.data";

	/**
	 * Set up the variables we'll need for this simplenlg.test to run (Called
	 * automatically by JUnit)
	 */
	@Before
	public void setUp() {
		this.lexicon = new XMLLexicon();  // built in lexicon

		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser(this.lexicon);
	}

	@After
	public void tearDown() {
		this.realiser = null;

		this.phraseFactory = null;

		if(null != lexicon) {
			lexicon = null;
		}
	}

	/**
	 * testLowercaseConstant - Test for when there is a lower case constant
	 */
	@Test
	public void testLowercaseConstant() {

		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "dog");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("A dog.", output);
	}

	/**
	 * testLowercaseVowel - Test for "an" as a specifier.
	 */
	@Test
	public void testLowercaseVowel() {
		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "owl");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("An owl.", output);
	}

	/**
	 * testUppercaseConstant - Test for when there is a upper case constant
	 */
	@Test
	public void testUppercaseConstant() {

		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "Cat");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("A Cat.", output);
	}

	/**
	 * testUppercaseVowel - Test for "an" as a specifier for upper subjects.
	 */
	@Test
	public void testUppercaseVowel() {
		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "Emu");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("An Emu.", output);
	}

	/**
	 * testNumericA - Test for "a" specifier with a numeric subject
	 */
	@Test
	public void testNumericA() {
		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "7");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("A 7.", output);
	}

	/**
	 * testNumericAn - Test for "an" specifier with a numeric subject
	 */
	@Test
	public void testNumericAn() {
		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "11");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("An 11.", output);
	}

	/**
	 * testIrregularSubjects - Test irregular subjects that don't conform to the
	 * vowel vs. constant divide.
	 */
	@Test
	public void testIrregularSubjects() {
		SPhraseSpec sentence = this.phraseFactory.createClause();

		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("a", "one");
		sentence.setSubject(subject);

		String output = this.realiser.realiseSentence(sentence);

		Assert.assertEquals("A one.", output);
	}

	/**
	 * testSingluarThisDeterminerNPObject - Test for "this" when used in the singular form as a determiner in a NP Object
	 */
	@Test
	public void testSingluarThisDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("this", "monkey");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("This monkey.", this.realiser.realiseSentence(sentence_1));
	}

	/**
	 * testPluralThisDeterminerNPObject - Test for "this" when used in the plural form as a determiner in a NP Object
	 */
	@Test
	public void testPluralThisDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("this");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("These monkeys.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testSingluarThatDeterminerNPObject - Test for "that" when used in the singular form as a determiner in a NP Object
	 */
	@Test
	public void testSingluarThatDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("that", "monkey");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("That monkey.", this.realiser.realiseSentence(sentence_1));
	}

	/**
	 * testPluralThatDeterminerNPObject - Test for "that" when used in the plural form as a determiner in a NP Object
	 */
	@Test
	public void testPluralThatDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("that");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Those monkeys.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testSingularThoseDeterminerNPObject - Test for "those" when used in the singular form as a determiner in a NP Object
	 */
	@Test
	public void testSingularThoseDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(false);
		nounPhrase_1.setDeterminer("those");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("That monkey.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testSingularTheseDeterminerNPObject - Test for "these" when used in the singular form as a determiner in a NP Object
	 */
	@Test
	public void testSingularTheseDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(false);
		nounPhrase_1.setDeterminer("these");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("This monkey.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testPluralThoseDeterminerNPObject - Test for "those" when used in the plural form as a determiner in a NP Object
	 */
	@Test
	public void testPluralThoseDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("those");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Those monkeys.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testPluralTheseDeterminerNPObject - Test for "these" when used in the plural form as a determiner in a NP Object
	 */
	@Test
	public void testPluralTheseDeterminerNPObject() {
		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("these");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("These monkeys.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testSingularTheseDeterminerNPObject - Test for "these" when used in the singular form as a determiner in a NP Object
	 * using the NIHDB Lexicon.
	 */
	@Test
	public void testSingularTheseDeterminerNPObject_NIHDBLexicon() {
		this.lexicon = new NIHDBLexicon(DB_FILENAME);
		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser(this.lexicon);

		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(false);
		nounPhrase_1.setDeterminer("these");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("This monkey.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testSingularThoseDeterminerNPObject - Test for "those" when used in the singular form as a determiner in a NP Object
	 * using the NIHDB Lexicon
	 */
	@Test
	public void testSingularThoseDeterminerNPObject_NIHDBLexicon() {
		this.lexicon = new NIHDBLexicon(DB_FILENAME);
		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser(this.lexicon);

		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(false);
		nounPhrase_1.setDeterminer("those");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("That monkey.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testPluralThatDeterminerNPObject - Test for "that" when used in the plural form as a determiner in a NP Object
	 * using the NIHDB Lexicon.
	 */
	@Test
	public void testPluralThatDeterminerNPObject_NIHDBLexicon() {
		this.lexicon = new NIHDBLexicon(DB_FILENAME);
		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser(this.lexicon);

		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("that");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Those monkeys.", this.realiser.realiseSentence(sentence_1));

	}

	/**
	 * testPluralThisDeterminerNPObject - Test for "this" when used in the plural form as a determiner in a NP Object
	 * using the NIHDBLexicon.
	 */
	@Test
	public void testPluralThisDeterminerNPObject_NIHDBLexicon() {
		this.lexicon = new NIHDBLexicon(DB_FILENAME);
		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser(this.lexicon);

		SPhraseSpec sentence_1 = this.phraseFactory.createClause();

		NPPhraseSpec nounPhrase_1 = this.phraseFactory.createNounPhrase("monkey");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("this");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("These monkeys.", this.realiser.realiseSentence(sentence_1));

	}

}
