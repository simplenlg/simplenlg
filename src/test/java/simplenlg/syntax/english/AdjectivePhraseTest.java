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
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.StringElement;

/**
 * This class incorporates a few tests for adjectival phrases. Also tests for
 * adverbial phrase specs, which are very similar
 *
 * @author agatt
 */
public class AdjectivePhraseTest extends SimpleNLG4Test {

	/**
	 * Instantiates a new adj p test.
	 *
	 * @param name the name
	 */
	public AdjectivePhraseTest(String name) {
		super(name);
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Test premodification & coordination of Adjective Phrases (Not much else
	 * to simplenlg.test)
	 */
	@Test
	public void testAdj() {

		// form the adjphrase "incredibly salacious"
		this.salacious.addPreModifier(this.phraseFactory.createAdverbPhrase("incredibly")); //$NON-NLS-1$
		Assert.assertEquals("incredibly salacious", this.realiser //$NON-NLS-1$
				.realise(this.salacious).getRealisation());

		// form the adjphrase "incredibly beautiful"
		this.beautiful.addPreModifier("amazingly"); //$NON-NLS-1$
		Assert.assertEquals("amazingly beautiful", this.realiser //$NON-NLS-1$
				.realise(this.beautiful).getRealisation());

		// coordinate the two aps
		CoordinatedPhraseElement coordap = new CoordinatedPhraseElement(this.salacious, this.beautiful);
		Assert.assertEquals("incredibly salacious and amazingly beautiful", //$NON-NLS-1$
		                    this.realiser.realise(coordap).getRealisation());

		// changing the inner conjunction
		coordap.setFeature(Feature.CONJUNCTION, "or"); //$NON-NLS-1$
		Assert.assertEquals("incredibly salacious or amazingly beautiful", //$NON-NLS-1$
		                    this.realiser.realise(coordap).getRealisation());

		// coordinate this with a new AdjPhraseSpec
		CoordinatedPhraseElement coord2 = new CoordinatedPhraseElement(coordap, this.stunning);
		Assert.assertEquals("incredibly salacious or amazingly beautiful and stunning", //$NON-NLS-1$
		                    this.realiser.realise(coord2).getRealisation());

		// add a premodifier the coordinate phrase, yielding
		// "seriously and undeniably incredibly salacious or amazingly beautiful
		// and stunning"
		CoordinatedPhraseElement preMod = new CoordinatedPhraseElement(new StringElement("seriously"),
		                                                               new StringElement("undeniably")); //$NON-NLS-1$//$NON-NLS-2$

		coord2.addPreModifier(preMod);
		Assert.assertEquals("seriously and undeniably incredibly salacious or amazingly beautiful and stunning",
		                    //$NON-NLS-1$
		                    this.realiser.realise(coord2).getRealisation());

		// adding a coordinate rather than coordinating should give a different
		// result
		coordap.addCoordinate(this.stunning);
		Assert.assertEquals("incredibly salacious, amazingly beautiful or stunning", //$NON-NLS-1$
		                    this.realiser.realise(coordap).getRealisation());

	}

	/**
	 * Simple test of adverbials
	 */
	@Test
	public void testAdv() {

		PhraseElement sent = this.phraseFactory.createClause("John", "eat"); //$NON-NLS-1$ //$NON-NLS-2$

		PhraseElement adv = this.phraseFactory.createAdverbPhrase("quickly"); //$NON-NLS-1$

		sent.addPreModifier(adv);

		Assert.assertEquals("John quickly eats", this.realiser.realise(sent) //$NON-NLS-1$
				.getRealisation());

		adv.addPreModifier("very"); //$NON-NLS-1$

		Assert.assertEquals("John very quickly eats", this.realiser.realise( //$NON-NLS-1$
		                                                                     sent).getRealisation());

	}

	/**
	 * Test participles as adjectives
	 */
	@Test
	public void testParticipleAdj() {
		PhraseElement ap = this.phraseFactory.createAdjectivePhrase(this.lexicon.getWord("associated",
		                                                                                 LexicalCategory.ADJECTIVE));
		Assert.assertEquals("associated", this.realiser.realise(ap).getRealisation());
	}

	/**
	 * Test for multiple adjective modifiers with comma-separation. Example courtesy of William Bradshaw (Data2Text Ltd).
	 */
	@Test
	public void testMultipleModifiers() {
		PhraseElement np = this.phraseFactory.createNounPhrase(this.lexicon.getWord("message", LexicalCategory.NOUN));
		np.addPreModifier(this.lexicon.getWord("active", LexicalCategory.ADJECTIVE));
		np.addPreModifier(this.lexicon.getWord("temperature", LexicalCategory.ADJECTIVE));
		Assert.assertEquals("active, temperature message", this.realiser.realise(np).getRealisation());

		//now we set the realiser not to separate using commas
		this.realiser.setCommaSepPremodifiers(false);
		Assert.assertEquals("active temperature message", this.realiser.realise(np).getRealisation());

	}

}
