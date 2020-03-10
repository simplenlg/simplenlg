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
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * Some tests for coordination, especially of coordinated VPs with modifiers.
 *
 * @author Albert Gatt
 */
public class CoordinationTest extends SimpleNLG4Test {

	public CoordinationTest(String name) {
		super(name);
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Check that empty coordinate phrases are not realised as "null"
	 */
	@Test
	public void emptyCoordinationTest() {
		// first a simple phrase with no coordinates
		CoordinatedPhraseElement coord = this.phraseFactory.createCoordinatedPhrase();
		Assert.assertEquals("", this.realiser.realise(coord).getRealisation());

		// now one with a premodifier and nothing else
		coord.addPreModifier(this.phraseFactory.createAdjectivePhrase("nice"));
		Assert.assertEquals("nice", this.realiser.realise(coord).getRealisation());
	}

	/**
	 * Test pre and post-modification of coordinate VPs inside a sentence.
	 */
	@Test
	public void testModifiedCoordVP() {
		CoordinatedPhraseElement coord = this.phraseFactory.createCoordinatedPhrase(this.getUp, this.fallDown);
		coord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("got up and fell down", this.realiser.realise(coord).getRealisation());

		// add a premodifier
		coord.addPreModifier("slowly");
		Assert.assertEquals("slowly got up and fell down", this.realiser.realise(coord).getRealisation());

		// adda postmodifier
		coord.addPostModifier(this.behindTheCurtain);
		Assert.assertEquals("slowly got up and fell down behind the curtain",
		                    this.realiser.realise(coord).getRealisation());

		// put within the context of a sentence
		SPhraseSpec s = this.phraseFactory.createClause("Jake", coord);
		s.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("Jake slowly got up and fell down behind the curtain",
		                    this.realiser.realise(s).getRealisation());

		// add premod to the sentence
		s.addPreModifier(this.lexicon.getWord("however", LexicalCategory.ADVERB));
		Assert.assertEquals("Jake however slowly got up and fell down behind the curtain",
		                    this.realiser.realise(s).getRealisation());

		// add postmod to the sentence
		s.addPostModifier(this.inTheRoom);
		Assert.assertEquals("Jake however slowly got up and fell down behind the curtain in the room",
		                    this.realiser.realise(s).getRealisation());
	}

	/**
	 * Test due to Chris Howell -- create a complex sentence with front modifier
	 * and coordinateVP. This is a version in which we create the coordinate
	 * phrase directly.
	 */
	@Test
	public void testCoordinateVPComplexSubject() {
		// "As a result of the procedure the patient had an adverse contrast media reaction and went into cardiogenic shock."
		SPhraseSpec s = this.phraseFactory.createClause();

		s.setSubject(this.phraseFactory.createNounPhrase("the", "patient"));

		// first VP
		VPPhraseSpec vp1 = this.phraseFactory.createVerbPhrase(this.lexicon.getWord("have", LexicalCategory.VERB));
		NPPhraseSpec np1 = this.phraseFactory.createNounPhrase("a",
		                                                       this.lexicon.getWord("contrast media reaction",
		                                                                            LexicalCategory.NOUN));
		np1.addPreModifier(this.lexicon.getWord("adverse", LexicalCategory.ADJECTIVE));
		vp1.addComplement(np1);

		// second VP
		VPPhraseSpec vp2 = this.phraseFactory.createVerbPhrase(this.lexicon.getWord("go", LexicalCategory.VERB));
		PPPhraseSpec pp = this.phraseFactory.createPrepositionPhrase("into",
		                                                             this.lexicon.getWord("cardiogenic shock",
		                                                                                  LexicalCategory.NOUN));
		vp2.addComplement(pp);

		// coordinate
		CoordinatedPhraseElement coord = this.phraseFactory.createCoordinatedPhrase(vp1, vp2);
		coord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("had an adverse contrast media reaction and went into cardiogenic shock",
		                    this.realiser.realise(coord).getRealisation());

		// now put this in the sentence
		s.setVerbPhrase(coord);
		s.addFrontModifier("As a result of the procedure");
		Assert.assertEquals(
				"As a result of the procedure the patient had an adverse contrast media reaction and went into cardiogenic shock",
				this.realiser.realise(s).getRealisation());

	}

	/**
	 * Test setting a conjunction to null
	 */
	public void testNullConjunction() {
		SPhraseSpec p = this.phraseFactory.createClause("I", "be", "happy");
		SPhraseSpec q = this.phraseFactory.createClause("I", "eat", "fish");
		CoordinatedPhraseElement pq = this.phraseFactory.createCoordinatedPhrase();
		pq.addCoordinate(p);
		pq.addCoordinate(q);
		pq.setFeature(Feature.CONJUNCTION, "");

		// should come out without conjunction
		Assert.assertEquals("I am happy I eat fish", this.realiser.realise(pq).getRealisation());

		// should come out without conjunction
		pq.setFeature(Feature.CONJUNCTION, null);
		Assert.assertEquals("I am happy I eat fish", this.realiser.realise(pq).getRealisation());

	}

	/**
	 * Check that the negation feature on a child of a coordinate phrase remains
	 * as set, unless explicitly set otherwise at the parent level.
	 */
	@Test
	public void testNegationFeature() {
		SPhraseSpec s1 = this.phraseFactory.createClause("he", "have", "asthma");
		SPhraseSpec s2 = this.phraseFactory.createClause("he", "have", "diabetes");
		s1.setFeature(Feature.NEGATED, true);
		CoordinatedPhraseElement coord = this.phraseFactory.createCoordinatedPhrase(s1, s2);
		String realisation = this.realiser.realise(coord).getRealisation();
		System.out.println(realisation);
		Assert.assertEquals("he does not have asthma and he has diabetes", realisation);
	}
}
