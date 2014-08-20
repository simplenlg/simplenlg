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

import org.junit.After;
import org.junit.Test;

import simplenlg.framework.LexicalCategory;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * Tests for string elements as parts of larger phrases
 * 
 * @author bertugatt
 * 
 */
public class StringElementTests extends SimpleNLG4Test {

	public StringElementTests(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Test that string elements can be used as heads of NP
	 */
	@Test
	public void testStringElementAsHead() {
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
	public void testStringElementAsVP() {
		SPhraseSpec s = this.phraseFactory.createClause();
		s.setVerbPhrase(this.phraseFactory.createStringElement("eats and drinks"));
		s.setSubject(this.phraseFactory.createStringElement("the big fat man"));
		assertEquals("the big fat man eats and drinks", this.realiser
				.realise(s).getRealisation());
	}

}
