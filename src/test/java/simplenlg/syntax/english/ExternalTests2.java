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
package simplenlg.syntax.english;

import junit.framework.Assert;

import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * Further tests from third parties
 * 
 * @author Albert Gatt, University of Malta and University of Aberdeen
 * 
 */
public class ExternalTests2 extends SimpleNLG4Test {

	public ExternalTests2(String name) {
		super(name);
	}

	/**
	 * Check that empty phrases are not realised as "null"
	 */
	@Test
	public void testEmptyPhraseRealisation() {
		SPhraseSpec emptyClause = this.phraseFactory.createClause();
		Assert.assertEquals("", this.realiser.realise(emptyClause)
				.getRealisation());
	}

	/**
	 * Check that empty coordinate phrases are not realised as "null"
	 */
	@Test
	public void testEmptyCoordination() {
		// first a simple phrase with no coordinates
		CoordinatedPhraseElement coord = this.phraseFactory
				.createCoordinatedPhrase();
		Assert.assertEquals("", this.realiser.realise(coord).getRealisation());

		// now one with a premodifier and nothing else
		coord.addPreModifier(this.phraseFactory.createAdjectivePhrase("nice"));
		Assert.assertEquals("nice", this.realiser.realise(coord)
				.getRealisation());
	}

	/**
	 * Test change from "a" to "an" in the presence of a premodifier with a
	 * vowel
	 */
	@Test
	public void testIndefiniteWithPremodifier() {
		SPhraseSpec s = this.phraseFactory.createClause("there", "be");
		s.setFeature(Feature.TENSE, Tense.PRESENT);
		NPPhraseSpec np = this.phraseFactory.createNounPhrase("a", "stenosis");
		s.setObject(np);

		// check without modifiers -- article should be "a"
		Assert.assertEquals("there is a stenosis", this.realiser.realise(s)
				.getRealisation());

		// add a single modifier -- should turn article to "an"
		np.addPreModifier(this.phraseFactory.createAdjectivePhrase("eccentric"));
		Assert.assertEquals("there is an eccentric stenosis", this.realiser
				.realise(s).getRealisation());
	}

	/**
	 * Test for comma separation between premodifers
	 */
	@Test
	public void testMultipleAdjPremodifiers() {
		NPPhraseSpec np = this.phraseFactory.createNounPhrase("a", "stenosis");
		np.addPreModifier(this.phraseFactory.createAdjectivePhrase("eccentric"));
		np.addPreModifier(this.phraseFactory.createAdjectivePhrase("discrete"));
		Assert.assertEquals("an eccentric, discrete stenosis", this.realiser
				.realise(np).getRealisation());
	}

	/**
	 * Test for comma separation between verb premodifiers
	 */
	@Test
	public void testMultipleAdvPremodifiers() {
		AdvPhraseSpec adv1 = this.phraseFactory.createAdverbPhrase("slowly");
		AdvPhraseSpec adv2 = this.phraseFactory
				.createAdverbPhrase("discretely");

		// case 1: concatenated premods: should have comma
		VPPhraseSpec vp = this.phraseFactory.createVerbPhrase("run");
		vp.addPreModifier(adv1);
		vp.addPreModifier(adv2);
		Assert.assertEquals("slowly, discretely runs", this.realiser
				.realise(vp).getRealisation());

		// case 2: coordinated premods: no comma
		VPPhraseSpec vp2 = this.phraseFactory.createVerbPhrase("eat");
		vp2.addPreModifier(this.phraseFactory.createCoordinatedPhrase(adv1,
				adv2));
		Assert.assertEquals("slowly and discretely eats", this.realiser
				.realise(vp2).getRealisation());
	}

	
	@Test
	public void testParticipleModifier() {

		String verb = "associate";
		VPPhraseSpec adjP = this.phraseFactory.createVerbPhrase(verb);
		adjP.setFeature(Feature.TENSE, Tense.PAST);

		NPPhraseSpec np = this.phraseFactory.createNounPhrase("a", "thrombus");
		np.addPreModifier(adjP);
		String realised = this.realiser.realise(np).getRealisation();
		System.out.println(realised);
		// cch TESTING The following line doesn't work when the lexeme is a
		// verb.
		// morphP.preMod.Add(new AdjPhraseSpec((Lexeme)modifier));

		// It doesn't work for verb "associate" as adjective past participle.
		// Instead of realizing as "associated" it realizes as "ed".
		// Need to use verb phrase.

		// cch TODO : handle general case making phrase type corresponding to
		// lexeme category and usage.
	}

	/**
	 * Check that setComplement replaces earlier complements
	 */
	@Test
	public void testSetComplement() {
		SPhraseSpec s = this.phraseFactory.createClause();
		s.setSubject("I");
		s.setVerb("see");
		s.setObject("a dog");

		Assert.assertEquals("I see a dog", this.realiser.realise(s)
				.getRealisation());

		s.setObject("a cat");
		Assert.assertEquals("I see a cat", this.realiser.realise(s)
				.getRealisation());

		s.setObject("a wolf");
		Assert.assertEquals("I see a wolf", this.realiser.realise(s)
				.getRealisation());

	}

	/**
	 * Test for subclauses involving WH-complements Based on a query by Owen
	 * Bennett
	 */
	@Test
	public void testSubclauses() {
		// Once upon a time, there was an Accountant, called Jeff, who lived in
		// a forest.

		// main sentence
		NPPhraseSpec acct = this.phraseFactory.createNounPhrase("a",
				"accountant");

		// first postmodifier of "an accountant"
		VPPhraseSpec sub1 = this.phraseFactory.createVerbPhrase("call");
		sub1.addComplement("Jeff");
		sub1.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
		// this is an appositive modifier, which makes simplenlg put it between
		// commas
		sub1.setFeature(Feature.APPOSITIVE, true);
		acct.addPostModifier(sub1);

		// second postmodifier of "an accountant" is "who lived in a forest"
		SPhraseSpec sub2 = this.phraseFactory.createClause();
		VPPhraseSpec subvp = this.phraseFactory.createVerbPhrase("live");
		subvp.setFeature(Feature.TENSE, Tense.PAST);
		subvp.setComplement(this.phraseFactory.createPrepositionPhrase("in",
				this.phraseFactory.createNounPhrase("a", "forest")));
		sub2.setVerbPhrase(subvp);
		// simplenlg can't yet handle wh-clauses in NPs, so we need to hack it
		// by setting the subject to "who"
		sub2.setSubject("who");
		acct.addPostModifier(sub2);

		// main sentence
		SPhraseSpec s = this.phraseFactory.createClause("there", "be", acct);
		s.setFeature(Feature.TENSE, Tense.PAST);

		// add front modifier "once upon a time"
		s.addFrontModifier("once upon a time");

		Assert.assertEquals(
				"once upon a time there was an accountant, called Jeff, who lived in a forest",
				this.realiser.realise(s).getRealisation());

	}

	/**
	 * Test for repetition of the future auxiliary "will", courtesy of Luxor
	 * Vlonjati
	 */
	@Test
	public void testFutureTense() {
		SPhraseSpec test = this.phraseFactory.createClause();

		NPPhraseSpec subj = this.phraseFactory.createNounPhrase("I");

		VPPhraseSpec verb = this.phraseFactory.createVerbPhrase("go");

		AdvPhraseSpec adverb = this.phraseFactory
				.createAdverbPhrase("tomorrow");

		test.setSubject(subj);
		test.setVerbPhrase(verb);
		test.setFeature(Feature.TENSE, Tense.FUTURE);
		test.addPostModifier(adverb);
		String sentence = realiser.realiseSentence(test);
		Assert.assertEquals("I will go tomorrow.", sentence);
		
		SPhraseSpec test2 = this.phraseFactory.createClause();
		NLGElement vb = this.phraseFactory.createWord("go", LexicalCategory.VERB);
		test2.setSubject(subj);
		test2.setVerb(vb);
		test2.setFeature(Feature.TENSE, Tense.FUTURE);
		test2.addPostModifier(adverb);
		String sentence2 = realiser.realiseSentence(test);
		Assert.assertEquals("I will go tomorrow.", sentence2);

	}
	
	/**
	 * Tests that no empty space is added when a StringElement is instantiated with an empty string
	 * or null object.
	 */
	@Test
	public void testNullAndEmptyStringElement() {

		NLGElement nullStringElement = this.phraseFactory.createStringElement(null);
		NLGElement emptyStringElement = this.phraseFactory.createStringElement("");
		NLGElement beautiful = this.phraseFactory.createStringElement("beautiful");
		NLGElement horseLike = this.phraseFactory.createStringElement("horse-like");
		NLGElement creature = this.phraseFactory.createStringElement("creature");

		// Test1: null or empty at beginning
		SPhraseSpec test1 = this.phraseFactory.createClause("a unicorn", "be", "regarded as a");
		test1.addPostModifier(emptyStringElement);
		test1.addPostModifier(beautiful);
		test1.addPostModifier(horseLike);
		test1.addPostModifier(creature);
		System.out.println(realiser.realiseSentence(test1));
		Assert.assertEquals("A unicorn is regarded as a beautiful horse-like creature.",
		                    realiser.realiseSentence(test1));

		// Test2: empty or null at end
		SPhraseSpec test2 = this.phraseFactory.createClause("a unicorn", "be", "regarded as a");
		test2.addPostModifier(beautiful);
		test2.addPostModifier(horseLike);
		test2.addPostModifier(creature);
		test2.addPostModifier(nullStringElement);
		System.out.println(realiser.realiseSentence(test2));
		Assert.assertEquals("A unicorn is regarded as a beautiful horse-like creature.",
		                    realiser.realiseSentence(test2));

		// Test3: empty or null in the middle
		SPhraseSpec test3 = this.phraseFactory.createClause("a unicorn", "be", "regarded as a");
		test3.addPostModifier("beautiful");
		test3.addPostModifier("horse-like");
		test3.addPostModifier("");
		test3.addPostModifier("creature");
		System.out.println(realiser.realiseSentence(test3));
		Assert.assertEquals("A unicorn is regarded as a beautiful horse-like creature.",
		                    realiser.realiseSentence(test3));

		// Test4: empty or null in the middle with empty or null at beginning
		SPhraseSpec test4 = this.phraseFactory.createClause("a unicorn", "be", "regarded as a");
		test4.addPostModifier("");
		test4.addPostModifier("beautiful");
		test4.addPostModifier("horse-like");
		test4.addPostModifier(nullStringElement);
		test4.addPostModifier("creature");
		System.out.println(realiser.realiseSentence(test4));
		Assert.assertEquals("A unicorn is regarded as a beautiful horse-like creature.",
		                    realiser.realiseSentence(test4));

	}
	
}
