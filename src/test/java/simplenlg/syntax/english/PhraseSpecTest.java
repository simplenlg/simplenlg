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
import simplenlg.framework.*;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * test suite for simple XXXPhraseSpec classes
 *
 * @author ereiter
 */

public class PhraseSpecTest extends SimpleNLG4Test {

	public PhraseSpecTest(String name) {
		super(name);
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Check that empty phrases are not realised as "null"
	 */
	@Test
	public void emptyPhraseRealisationTest() {
		SPhraseSpec emptyClause = this.phraseFactory.createClause();
		Assert.assertEquals("", this.realiser.realise(emptyClause).getRealisation());
	}

	/**
	 * Test SPhraseSpec
	 */
	@Test
	public void testSPhraseSpec() {

		// simple test of methods
		SPhraseSpec c1 = (SPhraseSpec) phraseFactory.createClause();
		c1.setVerb("give");
		c1.setSubject("John");
		c1.setObject("an apple");
		c1.setIndirectObject("Mary");
		c1.setFeature(Feature.TENSE, Tense.PAST);
		c1.setFeature(Feature.NEGATED, true);

		// check getXXX methods
		Assert.assertEquals("give", getBaseForm(c1.getVerb()));
		Assert.assertEquals("John", getBaseForm(c1.getSubject()));
		Assert.assertEquals("an apple", getBaseForm(c1.getObject()));
		Assert.assertEquals("Mary", getBaseForm(c1.getIndirectObject()));

		Assert.assertEquals("John did not give Mary an apple", this.realiser //$NON-NLS-1$
				.realise(c1).getRealisation());

		// test modifier placement
		SPhraseSpec c2 = (SPhraseSpec) phraseFactory.createClause();
		c2.setVerb("see");
		c2.setSubject("the man");
		c2.setObject("me");
		c2.addModifier("fortunately");
		c2.addModifier("quickly");
		c2.addModifier("in the park");
		// try setting tense directly as a feature
		c2.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("fortunately the man quickly saw me in the park", this.realiser //$NON-NLS-1$
				.realise(c2).getRealisation());
	}

	// get string for head of constituent
	private String getBaseForm(NLGElement constituent) {
		if(constituent == null)
			return null;
		else if(constituent instanceof StringElement)
			return constituent.getRealisation();
		else if(constituent instanceof WordElement)
			return ((WordElement) constituent).getBaseForm();
		else if(constituent instanceof InflectedWordElement)
			return getBaseForm(((InflectedWordElement) constituent).getBaseWord());
		else if(constituent instanceof PhraseElement)
			return getBaseForm(((PhraseElement) constituent).getHead());
		else
			return null;
	}
}
