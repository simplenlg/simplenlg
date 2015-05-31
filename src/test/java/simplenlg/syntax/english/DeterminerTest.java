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

import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;

import junit.framework.Assert;


/**
 * Some determiner tests -- in particular for indefinite articles like "a" or "an".
 * 
 * @author Saad Mahamood, Data2Text Limited.
 *
 */
public class DeterminerTest extends SimpleNLG4Test {
	
	
	public DeterminerTest(String name) {
		super(name);
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
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
}
