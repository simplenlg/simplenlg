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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.framework.CoordinatedPhraseElement;

// TODO: Auto-generated Javadoc
/**
 * This class groups together some tests for prepositional phrases and
 * coordinate prepositional phrases.
 * @author agatt
 */
public class PrepositionalPhraseTest extends SimpleNLG4Test {

	/**
	 * Instantiates a new pP test.
	 * 
	 * @param name
	 *            the name
	 */
	public PrepositionalPhraseTest(String name) {
		super(name);
	}
	
	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Basic test for the pre-set PP fixtures.
	 */
	@Test
	public void testBasic() {
		Assert.assertEquals("in the room", this.realiser //$NON-NLS-1$
				.realise(this.inTheRoom).getRealisation());
		Assert.assertEquals("behind the curtain", this.realiser //$NON-NLS-1$
				.realise(this.behindTheCurtain).getRealisation());
		Assert.assertEquals("on the rock", this.realiser //$NON-NLS-1$
				.realise(this.onTheRock).getRealisation());
	}

	/**
	 * Test for coordinate NP complements of PPs.
	 */
	@Test
	public void testComplementation() {
		this.inTheRoom.clearComplements();
		this.inTheRoom.addComplement(new CoordinatedPhraseElement(
				this.phraseFactory.createNounPhrase("the", "room"), //$NON-NLS-1$ //$NON-NLS-2$
				this.phraseFactory.createNounPhrase("a", "car"))); //$NON-NLS-1$//$NON-NLS-2$
		Assert.assertEquals("in the room and a car", this.realiser //$NON-NLS-1$
				.realise(this.inTheRoom).getRealisation());
	}

	/**
	 * Test for PP coordination.
	 */
	public void testCoordination() {
		// simple coordination

		CoordinatedPhraseElement coord1 = new CoordinatedPhraseElement(
				this.inTheRoom, this.behindTheCurtain);
		Assert.assertEquals("in the room and behind the curtain", this.realiser //$NON-NLS-1$
				.realise(coord1).getRealisation());

		// change the conjunction
		coord1.setFeature(Feature.CONJUNCTION, "or"); //$NON-NLS-1$
		Assert.assertEquals("in the room or behind the curtain", this.realiser //$NON-NLS-1$
				.realise(coord1).getRealisation());

		// new coordinate
		CoordinatedPhraseElement coord2 = new CoordinatedPhraseElement(
				this.onTheRock, this.underTheTable);
		coord2.setFeature(Feature.CONJUNCTION, "or"); //$NON-NLS-1$
		Assert.assertEquals("on the rock or under the table", this.realiser //$NON-NLS-1$
				.realise(coord2).getRealisation());

		// coordinate two coordinates
		CoordinatedPhraseElement coord3 = new CoordinatedPhraseElement(coord1,
				coord2);

		String text = this.realiser.realise(coord3).getRealisation();
		Assert
				.assertEquals(
						"in the room or behind the curtain and on the rock or under the table", //$NON-NLS-1$
						text);
	}
}
