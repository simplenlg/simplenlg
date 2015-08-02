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

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.realiser.english.Realiser;

/**
 * Tests for NIHDBLexicon
 * 
 * @author Ehud Reiter, Albert Gatt
 */
public class NIHDBLexiconTest {

	// lexicon object -- an instance of Lexicon
	NIHDBLexicon lexicon = null;

	// DB location -- change this to point to the lex access data dir
	static String DB_FILENAME = "src/test/resources/NIHLexicon/lexAccess2011.data";

	@Before
	/*
	 * * Sets up the accessor and runs it -- takes ca. 26 sec
	 */
	public void setUp() {
        // use property file for the lexicon
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().
                      getResourceAsStream("lexicon.properties"));

            String lexiconPath = prop.getProperty("DB_FILENAME");
            
            if (null != lexiconPath)
                this.lexicon = new NIHDBLexicon(lexiconPath);
        } catch (Exception e) {
            this.lexicon = new NIHDBLexicon(DB_FILENAME);
        }
	}

	/**
	 * Close the lexicon
	 */
	@After
	public void tearDown() throws Exception {
		if (lexicon != null)
			lexicon.close();
	}

	/**
	 * Runs basic Lexicon Tests.
	 */
	@Test
	public void basicLexiconTests() {
		SharedLexiconTests tests = new SharedLexiconTests();
		tests.doBasicTests(lexicon);
	}

	/**
	 * Verb 'be' conjugation tests.
	 */
	public void beInflectionTest() {
		Realiser r = new Realiser();
		WordElement word = lexicon.getWord("be", LexicalCategory.VERB);
		InflectedWordElement inflWord = new InflectedWordElement(word);
		
		//1st person sg past
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("was", r.realise(inflWord).toString());
		
		//2nd person sg past
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("were", r.realise(inflWord).toString());
		
		//3rd person sg past
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("was", r.realise(inflWord).toString());
		
		//1st person sg present
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);		
		Assert.assertEquals("am", r.realise(inflWord).toString());
		
		//2nd person sg present
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("are", r.realise(inflWord).toString());
		
		//3rd person sg present
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("is", r.realise(inflWord).toString());
		
		inflWord.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		
		//1st person pl past
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("were", r.realise(inflWord).toString());
		
		//2nd person pl past
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("were", r.realise(inflWord).toString());
		
		//3rd person pl past
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("were", r.realise(inflWord).toString());
		
		//1st person pl present
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);		
		Assert.assertEquals("are", r.realise(inflWord).toString());
		
		//2nd person pl present
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("are", r.realise(inflWord).toString());
		
		//3rd person pl present
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("are", r.realise(inflWord).toString());
		
	}
	
	/**
	 * Tests the lexicon for recall of Acryonms.
	 */
	@Test
	public void acronymsTests() {
		WordElement uk = lexicon.getWord("UK");
		WordElement unitedKingdom = lexicon.getWord("United Kingdom");
		List<NLGElement> fullForms = uk
				.getFeatureAsElementList(LexicalFeature.ACRONYM_OF);

		// "uk" is an acronym of 3 full forms
		Assert.assertEquals(3, fullForms.size());
		Assert.assertTrue(fullForms.contains(unitedKingdom));
	}

	/**
	 * Tests the lexicon for recall of baseforms with their standard inflected forms. 
	 */
	@Test
	public void standardInflectionsTest() {
		// test keepStandardInflection flag
		boolean keepInflectionsFlag = lexicon.isKeepStandardInflections();

		lexicon.setKeepStandardInflections(true);
		WordElement dog = lexicon.getWord("dog", LexicalCategory.NOUN);
		Assert.assertEquals("dogs", dog
				.getFeatureAsString(LexicalFeature.PLURAL));

		lexicon.setKeepStandardInflections(false);
		WordElement cat = lexicon.getWord("cat", LexicalCategory.NOUN);
		Assert
				.assertEquals(null, cat
						.getFeatureAsString(LexicalFeature.PLURAL));

		// restore flag to original state
		lexicon.setKeepStandardInflections(keepInflectionsFlag);
	}

	/**
	 * Test for NIHDBLexicon functionality when several threads are using the
	 * same Lexicon
	 */
	@SuppressWarnings("static-access")
	public void multiThreadAppAccessTest() {

		LexThread runner1 = new LexThread("lie");
		LexThread runner2 = new LexThread("bark");

		// schedule them and run them
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		service.schedule(runner1, 0, TimeUnit.MILLISECONDS);
		service.schedule(runner2, 0, TimeUnit.MILLISECONDS);

		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException ie) {
			;// do nothing
		}

		service.shutdownNow();

		// check that the right words have been retrieved
		Assert.assertEquals("lie", runner1.word.getBaseForm());
		Assert.assertEquals("bark", runner2.word.getBaseForm());
	}

	/*
	 * Class that implements a thread from which a lexical item can be retrieved
	 */
	private class LexThread extends Thread {
		WordElement word;
		String base;

		public LexThread(String base) {
			this.base = base;
		}

		public void run() {
			word = lexicon.getWord(base, LexicalCategory.VERB);
		}
	}

}
