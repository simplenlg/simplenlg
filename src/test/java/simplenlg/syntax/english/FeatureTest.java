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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.syntax.english;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * Tests that check that realization of different Features against NLGElements.
 * 
 * @author François Portet
 */

public class FeatureTest extends SimpleNLG4Test {
	
	
	NLGFactory docFactory = new NLGFactory(this.lexicon);
	
	/**
	 * Instantiates a new text spec test.
	 * 
	 * @param name
	 *            the name
	 */
	public FeatureTest(String name) {
		super(name);
	}
	
	@Override
	@After
	public void tearDown() {
		super.tearDown();
		this.docFactory = null; 
	}

	/**
	 * Tests use of the Possessive Feature.
	 */
	@Test
	public void testPossessiveFeature_PastTense() {
		this.phraseFactory.setLexicon(this.lexicon);
		this.realiser.setLexicon(this.lexicon);
		
		// Create the pronoun 'she'
		NLGElement she = phraseFactory.createWord("she",LexicalCategory.PRONOUN);

		// Set possessive on the pronoun to make it 'her'
		she.setFeature(Feature.POSSESSIVE, true);

		// Create a noun phrase with the subject lover and the determiner
		// as she
		PhraseElement herLover = phraseFactory.createNounPhrase(she,"lover");

		// Create a clause to say 'he be her lover'
		PhraseElement clause = phraseFactory.createClause("he", "be", herLover);

		// Add the cue phrase need the comma as orthography
		// currently doesn't handle this.
		// This could be expanded to be a noun phrase with determiner
		// 'two' and noun 'week', set to plural and with a premodifier of
		// 'after'
		clause.setFeature(Feature.CUE_PHRASE, "after two weeks,");

		// Add the 'for a fortnight' as a post modifier. Alternatively
		// this could be added as a prepositional phrase 'for' with a
		// complement of a noun phrase ('a' 'fortnight')
		clause.addPostModifier("for a fortnight");

		// Set 'be' to 'was' as past tense
		clause.setFeature(Feature.TENSE,Tense.PAST);

		// Add the clause to a sentence.
		DocumentElement sentence1 = docFactory.createSentence(clause);

		// Realise the sentence
		NLGElement realised = this.realiser.realise(sentence1);
 		
		Assert.assertEquals("After two weeks, he was her lover for a fortnight.",
				realised.getRealisation());
	}

	/**
	 * Basic tests.
	 */
	@Test
	public void testTwoPossessiveFeature_PastTense() {
		this.phraseFactory.setLexicon(this.lexicon);

		// Create the pronoun 'she'
		NLGElement she = phraseFactory.createWord("she",LexicalCategory.PRONOUN);

		// Set possessive on the pronoun to make it 'her'
		she.setFeature(Feature.POSSESSIVE, true);

		// Create a noun phrase with the subject lover and the determiner
		// as she
		PhraseElement herLover = phraseFactory.createNounPhrase(she,"lover");
		herLover.setPlural(true);

		// Create the pronoun 'he'
		NLGElement he = phraseFactory.createNounPhrase(LexicalCategory.PRONOUN,"he");
		he.setPlural(true);

		// Create a clause to say 'they be her lovers'
		PhraseElement clause = phraseFactory.createClause(he, "be", herLover);
		clause.setFeature(Feature.POSSESSIVE, true);

		// Add the cue phrase need the comma as orthography
		// currently doesn't handle this.
		// This could be expanded to be a noun phrase with determiner
		// 'two' and noun 'week', set to plural and with a premodifier of
		// 'after'
		clause.setFeature(Feature.CUE_PHRASE, "after two weeks,");

		// Add the 'for a fortnight' as a post modifier. Alternatively
		// this could be added as a prepositional phrase 'for' with a
		// complement of a noun phrase ('a' 'fortnight')
		clause.addPostModifier("for a fortnight");

		// Set 'be' to 'was' as past tense
		clause.setFeature(Feature.TENSE,Tense.PAST);
		
		// Add the clause to a sentence.
		DocumentElement sentence1 = docFactory.createSentence(clause);

		// Realise the sentence
		NLGElement realised = this.realiser.realise(sentence1);

		Assert.assertEquals("After two weeks, they were her lovers for a fortnight.", //$NON-NLS-1$
				realised.getRealisation());
	}

	/**
	 * Test use of the Complementiser feature by combining two S's using cue phrase and gerund.
	 */
	@Test
	public void testComplementiserFeature_PastTense() {
		this.phraseFactory.setLexicon(this.lexicon);

		PhraseElement born = phraseFactory.createClause("Dave Bus", "be", "born");
		born.setFeature(Feature.TENSE,Tense.PAST);
		born.addPostModifier("in");
		born.setFeature(Feature.COMPLEMENTISER, "which");

		PhraseElement theHouse = phraseFactory.createNounPhrase("the", "house");
		theHouse.addComplement(born);

		PhraseElement clause = phraseFactory.createClause(theHouse, "be", phraseFactory.createPrepositionPhrase("in", "Edinburgh"));
		DocumentElement sentence = docFactory.createSentence(clause);
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("The house which Dave Bus was born in is in Edinburgh.",
				realised.getRealisation());
	}

	/**
	 * Test use of the Complementiser feature in a {@link CoordinatedPhraseElement} by combine two S's using cue phrase and gerund.
	 */
	@Test
	public void testComplementiserFeatureInACoordinatePhrase_PastTense() {
		this.phraseFactory.setLexicon(this.lexicon);

		NLGElement dave = phraseFactory.createWord("Dave Bus", LexicalCategory.NOUN);
		NLGElement albert = phraseFactory.createWord("Albert", LexicalCategory.NOUN);
		
		CoordinatedPhraseElement coord1 = new CoordinatedPhraseElement(
				dave, albert);
		
		PhraseElement born = phraseFactory.createClause(coord1, "be", "born");
		born.setFeature(Feature.TENSE,Tense.PAST);
		born.addPostModifier("in");
		born.setFeature(Feature.COMPLEMENTISER, "which");

		PhraseElement theHouse = phraseFactory.createNounPhrase("the", "house");
		theHouse.addComplement(born);

		PhraseElement clause = phraseFactory.createClause(theHouse, "be", phraseFactory.createPrepositionPhrase("in", "Edinburgh"));
		DocumentElement sentence = docFactory.createSentence(clause);
		
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("The house which Dave Bus and Albert were born in is in Edinburgh.",
				realised.getRealisation());
	}

	/**
	 * Test the use of the Progressive and Complementiser Features in future tense.
	 */
	@Test
	public void testProgressiveAndComplementiserFeatures_FutureTense() {
		this.phraseFactory.setLexicon(this.lexicon);

		// Inner clause is 'I' 'make' 'sentence' 'for'.
		PhraseElement inner = phraseFactory.createClause("I","make", "sentence for");
		// Inner clause set to progressive.
		inner.setFeature(Feature.PROGRESSIVE,true);
		
		//Complementiser on inner clause is 'whom'
		inner.setFeature(Feature.COMPLEMENTISER, "whom");
		
		// create the engineer and add the inner clause as post modifier 
		PhraseElement engineer = phraseFactory.createNounPhrase("the engineer");
		engineer.addComplement(inner);
		
		// Outer clause is: 'the engineer' 'go' (preposition 'to' 'holidays')
		PhraseElement outer = phraseFactory.createClause(engineer,"go",phraseFactory.createPrepositionPhrase("to","holidays"));

		// Outer clause tense is Future.
		outer.setFeature(Feature.TENSE, Tense.FUTURE);
		
		// Possibly progressive as well not sure.
		outer.setFeature(Feature.PROGRESSIVE,true);
		
		//Outer clause postmodifier would be 'tomorrow'
		outer.addPostModifier("tomorrow");
		DocumentElement sentence = docFactory.createSentence(outer);
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("The engineer whom I am making sentence for will be going to holidays tomorrow.",
				realised.getRealisation());
	}

	
	/**
	 * Tests the use of the Complementiser, Passive, Perfect features in past tense.
	 */
	@Test
	public void testComplementiserPassivePerfectFeatures_PastTense() {
		setUp();
		this.realiser.setLexicon(this.lexicon);
		
		PhraseElement inner = phraseFactory.createClause("I", "play", "poker");
		inner.setFeature(Feature.TENSE,Tense.PAST);
		inner.setFeature(Feature.COMPLEMENTISER, "where");
		
		PhraseElement house = phraseFactory.createNounPhrase("the", "house");
		house.addComplement(inner);
		
		SPhraseSpec outer = phraseFactory.createClause(null, "abandon", house);
		
		outer.addPostModifier("since 1986");
		
		outer.setFeature(Feature.PASSIVE, true);
		outer.setFeature(Feature.PERFECT, true);
		
		DocumentElement sentence = docFactory.createSentence(outer);
		NLGElement realised = realiser.realise(sentence); 

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("The house where I played poker has been abandoned since 1986.",
				realised.getRealisation());
	}
	
	/**
	 * Tests the user of the progressive and complementiser featuers in past tense.
	 */
	@Test
	public void testProgressiveComplementiserFeatures_PastTense() {
		this.phraseFactory.setLexicon(this.lexicon);

		NLGElement sandwich = phraseFactory.createNounPhrase(LexicalCategory.NOUN, "sandwich");
		sandwich.setPlural(true);
		// 
		PhraseElement first = phraseFactory.createClause("I", "make", sandwich);
		first.setFeature(Feature.TENSE,Tense.PAST);
		first.setFeature(Feature.PROGRESSIVE,true);
		first.setPlural(false);
		
		PhraseElement second = phraseFactory.createClause("the mayonnaise", "run out");
		second.setFeature(Feature.TENSE,Tense.PAST);
		// 
		second.setFeature(Feature.COMPLEMENTISER, "when");
		
		first.addComplement(second);
		
		DocumentElement sentence = docFactory.createSentence(first);
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("I was making sandwiches when the mayonnaise ran out.",
				realised.getRealisation());
	}
	
   /**
	* Test the use of Passive in creating a Passive sentence structure: <Object> + [be] + <verb> + [by] + [Subject].
	*/
	@Test
	public void testPassiveFeature() {
		this.realiser.setLexicon(this.lexicon);
	
		PhraseElement phrase = phraseFactory.createClause("recession", "affect", "value");
		phrase.setFeature(Feature.PASSIVE, true);
		DocumentElement sentence = docFactory.createSentence(phrase);
		NLGElement realised = realiser.realise(sentence);
	
		Assert.assertEquals("Value is affected by recession.", realised.getRealisation());
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
	
	
}
