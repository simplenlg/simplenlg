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
import org.junit.Before;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * JUnit test case for interrogatives.
 *
 * @author agatt
 */
public class InterrogativeTest extends SimpleNLG4Test {

	// set up a few more fixtures
	/**
	 * The s5.
	 */
	SPhraseSpec s1, s2, s3, s4, s5;

	/**
	 * Instantiates a new interrogative test.
	 *
	 * @param name the name
	 */
	public InterrogativeTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see simplenlg.test.SimplenlgTest#setUp()
	 */
	@Override
	@Before
	protected void setUp() {
		super.setUp();

		// // the man gives the woman John's flower
		PhraseElement john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		PhraseElement flower = this.phraseFactory.createNounPhrase(john, "flower"); //$NON-NLS-1$
		PhraseElement _woman = this.phraseFactory.createNounPhrase("the", "woman"); //$NON-NLS-1$ //$NON-NLS-2$
		this.s3 = this.phraseFactory.createClause(this.man, this.give, flower);
		this.s3.setIndirectObject(_woman);

		CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(this.phraseFactory.createNounPhrase("Jane"),
		                                                                 //$NON-NLS-1$
		                                                                 this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		this.s4 = this.phraseFactory.createClause(subjects, "pick up", //$NON-NLS-1$
		                                          "the balls"); //$NON-NLS-1$
		this.s4.addPostModifier("in the shop"); //$NON-NLS-1$
		this.s4.setFeature(Feature.CUE_PHRASE, "however"); //$NON-NLS-1$
		this.s4.addFrontModifier("tomorrow"); //$NON-NLS-1$
		this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
		// this.s5 = new SPhraseSpec();
		// this.s5.setSubject(new NPPhraseSpec("the", "dog"));
		// this.s5.setHead("be");
		// this.s5.setComplement(new NPPhraseSpec("the", "rock"),
		// DiscourseFunction.OBJECT);

	}

	/**
	 * Tests a couple of fairly simple questions.
	 */
	@Test
	public void testSimpleQuestions() {
		setUp();
		this.phraseFactory.setLexicon(this.lexicon);
		this.realiser.setLexicon(this.lexicon);

		// simple present
		this.s1 = this.phraseFactory.createClause(this.woman, this.kiss, this.man);
		this.s1.setFeature(Feature.TENSE, Tense.PRESENT);
		this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

		NLGFactory docFactory = new NLGFactory(this.lexicon);
		DocumentElement sent = docFactory.createSentence(this.s1);
		Assert.assertEquals("Does the woman kiss the man?", this.realiser //$NON-NLS-1$
				.realise(sent).getRealisation());

		// simple past
		// sentence: "the woman kissed the man"
		this.s1 = this.phraseFactory.createClause(this.woman, this.kiss, this.man);
		this.s1.setFeature(Feature.TENSE, Tense.PAST);
		this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("did the woman kiss the man", this.realiser //$NON-NLS-1$
				.realise(this.s1).getRealisation());

		// copular/existential: be-fronting
		// sentence = "there is the dog on the rock"
		this.s2 = this.phraseFactory.createClause("there", "be", this.dog); //$NON-NLS-1$ //$NON-NLS-2$
		this.s2.addPostModifier(this.onTheRock);
		this.s2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("is there the dog on the rock", this.realiser //$NON-NLS-1$
				.realise(this.s2).getRealisation());

		// perfective
		// sentence -- "there has been the dog on the rock"
		this.s2 = this.phraseFactory.createClause("there", "be", this.dog); //$NON-NLS-1$ //$NON-NLS-2$
		this.s2.addPostModifier(this.onTheRock);
		this.s2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		this.s2.setFeature(Feature.PERFECT, true);
		Assert.assertEquals("has there been the dog on the rock", //$NON-NLS-1$
		                    this.realiser.realise(this.s2).getRealisation());

		// progressive
		// sentence: "the man was giving the woman John's flower"
		PhraseElement john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		PhraseElement flower = this.phraseFactory.createNounPhrase(john, "flower"); //$NON-NLS-1$
		PhraseElement _woman = this.phraseFactory.createNounPhrase("the", "woman"); //$NON-NLS-1$ //$NON-NLS-2$
		this.s3 = this.phraseFactory.createClause(this.man, this.give, flower);
		this.s3.setIndirectObject(_woman);
		this.s3.setFeature(Feature.TENSE, Tense.PAST);
		this.s3.setFeature(Feature.PROGRESSIVE, true);
		this.s3.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		NLGElement realised = this.realiser.realise(this.s3);
		Assert.assertEquals("was the man giving the woman John's flower", //$NON-NLS-1$
		                    realised.getRealisation());

		// modal
		// sentence: "the man should be giving the woman John's flower"
		setUp();
		john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower = this.phraseFactory.createNounPhrase(john, "flower"); //$NON-NLS-1$
		_woman = this.phraseFactory.createNounPhrase("the", "woman"); //$NON-NLS-1$ //$NON-NLS-2$
		this.s3 = this.phraseFactory.createClause(this.man, this.give, flower);
		this.s3.setIndirectObject(_woman);
		this.s3.setFeature(Feature.TENSE, Tense.PAST);
		this.s3.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		this.s3.setFeature(Feature.MODAL, "should"); //$NON-NLS-1$
		Assert.assertEquals("should the man have given the woman John's flower", //$NON-NLS-1$
		                    this.realiser.realise(this.s3).getRealisation());

		// complex case with cue phrases
		// sentence: "however, tomorrow, Jane and Andrew will pick up the balls
		// in the shop"
		// this gets the front modifier "tomorrow" shifted to the end
		setUp();
		CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(this.phraseFactory.createNounPhrase("Jane"),
		                                                                 //$NON-NLS-1$
		                                                                 this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		this.s4 = this.phraseFactory.createClause(subjects, "pick up", //$NON-NLS-1$
		                                          "the balls"); //$NON-NLS-1$
		this.s4.addPostModifier("in the shop"); //$NON-NLS-1$
		this.s4.setFeature(Feature.CUE_PHRASE, "however,"); //$NON-NLS-1$
		this.s4.addFrontModifier("tomorrow"); //$NON-NLS-1$
		this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("however, will Jane and Andrew pick up the balls in the shop tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());
	}

	/**
	 * Test for sentences with negation.
	 */
	@Test
	public void testNegatedQuestions() {
		setUp();
		this.phraseFactory.setLexicon(this.lexicon);
		this.realiser.setLexicon(this.lexicon);

		// sentence: "the woman did not kiss the man"
		this.s1 = this.phraseFactory.createClause(this.woman, "kiss", this.man);
		this.s1.setFeature(Feature.TENSE, Tense.PAST);
		this.s1.setFeature(Feature.NEGATED, true);
		this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("did the woman not kiss the man", this.realiser //$NON-NLS-1$
				.realise(this.s1).getRealisation());

		// sentence: however, tomorrow, Jane and Andrew will not pick up the
		// balls in the shop
		CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(this.phraseFactory.createNounPhrase("Jane"),
		                                                                 //$NON-NLS-1$
		                                                                 this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		this.s4 = this.phraseFactory.createClause(subjects, "pick up", //$NON-NLS-1$
		                                          "the balls"); //$NON-NLS-1$
		this.s4.addPostModifier("in the shop"); //$NON-NLS-1$
		this.s4.setFeature(Feature.CUE_PHRASE, "however,"); //$NON-NLS-1$
		this.s4.addFrontModifier("tomorrow"); //$NON-NLS-1$
		this.s4.setFeature(Feature.NEGATED, true);
		this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("however, will Jane and Andrew not pick up the balls in the shop tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());
	}

	/**
	 * Tests for coordinate VPs in question form.
	 */
	@Test
	public void testCoordinateVPQuestions() {

		// create a complex vp: "kiss the dog and walk in the room"
		setUp();
		CoordinatedPhraseElement complex = new CoordinatedPhraseElement(this.kiss, this.walk);
		this.kiss.addComplement(this.dog);
		this.walk.addComplement(this.inTheRoom);

		// sentence: "However, tomorrow, Jane and Andrew will kiss the dog and
		// will walk in the room"
		CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(this.phraseFactory.createNounPhrase("Jane"),
		                                                                 //$NON-NLS-1$
		                                                                 this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		this.s4 = this.phraseFactory.createClause(subjects, complex);
		this.s4.setFeature(Feature.CUE_PHRASE, "however"); //$NON-NLS-1$
		this.s4.addFrontModifier("tomorrow"); //$NON-NLS-1$
		this.s4.setFeature(Feature.TENSE, Tense.FUTURE);

		Assert.assertEquals("however tomorrow Jane and Andrew will kiss the dog and will walk in the room",
		                    //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// setting to interrogative should automatically give us a single,
		// wide-scope aux
		setUp();
		subjects = new CoordinatedPhraseElement(this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
		                                        this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		this.kiss.addComplement(this.dog);
		this.walk.addComplement(this.inTheRoom);
		complex = new CoordinatedPhraseElement(this.kiss, this.walk);
		this.s4 = this.phraseFactory.createClause(subjects, complex);
		this.s4.setFeature(Feature.CUE_PHRASE, "however"); //$NON-NLS-1$
		this.s4.addFrontModifier("tomorrow"); //$NON-NLS-1$
		this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

		Assert.assertEquals("however will Jane and Andrew kiss the dog and walk in the room tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// slightly more complex -- perfective
		setUp();
		this.realiser.setLexicon(this.lexicon);
		subjects = new CoordinatedPhraseElement(this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
		                                        this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		complex = new CoordinatedPhraseElement(this.kiss, this.walk);
		this.kiss.addComplement(this.dog);
		this.walk.addComplement(this.inTheRoom);
		this.s4 = this.phraseFactory.createClause(subjects, complex);
		this.s4.setFeature(Feature.CUE_PHRASE, "however"); //$NON-NLS-1$
		this.s4.addFrontModifier("tomorrow"); //$NON-NLS-1$
		this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		this.s4.setFeature(Feature.PERFECT, true);

		Assert.assertEquals("however will Jane and Andrew have kissed the dog and walked in the room tomorrow",
		                    //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());
	}

	/**
	 * Test for simple WH questions in present tense.
	 */
	@Test
	public void testSimpleQuestions2() {
		setUp();
		this.realiser.setLexicon(this.lexicon);
		PhraseElement s = this.phraseFactory.createClause("the woman", "kiss", //$NON-NLS-1$ //$NON-NLS-2$
		                                                  "the man"); //$NON-NLS-1$

		// try with the simple yes/no type first
		s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("does the woman kiss the man", this.realiser //$NON-NLS-1$
				.realise(s).getRealisation());

		// now in the passive
		s = this.phraseFactory.createClause("the woman", "kiss", //$NON-NLS-1$ //$NON-NLS-2$
		                                    "the man"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		s.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("is the man kissed by the woman", this.realiser //$NON-NLS-1$
				.realise(s).getRealisation());

		// // subject interrogative with simple present
		// // sentence: "the woman kisses the man"
		s = this.phraseFactory.createClause("the woman", "kiss", //$NON-NLS-1$ //$NON-NLS-2$
		                                    "the man"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);

		Assert.assertEquals("who kisses the man", this.realiser.realise(s) //$NON-NLS-1$
				.getRealisation());

		// object interrogative with simple present
		s = this.phraseFactory.createClause("the woman", "kiss", //$NON-NLS-1$ //$NON-NLS-2$
		                                    "the man"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
		Assert.assertEquals("who does the woman kiss", this.realiser //$NON-NLS-1$
				.realise(s).getRealisation());

		// subject interrogative with passive
		s = this.phraseFactory.createClause("the woman", "kiss", //$NON-NLS-1$ //$NON-NLS-2$
		                                    "the man"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		s.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("who is the man kissed by", this.realiser //$NON-NLS-1$
				.realise(s).getRealisation());
	}

	/**
	 * Test for wh questions.
	 */
	@Test
	public void testWHQuestions() {

		// subject interrogative
		setUp();
		this.realiser.setLexicon(this.lexicon);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("however who will pick up the balls in the shop tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// subject interrogative in passive
		setUp();
		this.s4.setFeature(Feature.PASSIVE, true);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);

		Assert.assertEquals("however who will the balls be picked up in the shop by tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// object interrogative
		setUp();
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("however what will Jane and Andrew pick up in the shop tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// object interrogative with passive
		setUp();
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		this.s4.setFeature(Feature.PASSIVE, true);

		Assert.assertEquals("however what will be picked up in the shop by Jane and Andrew tomorrow", //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// how-question + passive
		setUp();
		this.s4.setFeature(Feature.PASSIVE, true);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
		Assert.assertEquals("however how will the balls be picked up in the shop by Jane and Andrew tomorrow",
		                    //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// // why-question + passive
		setUp();
		this.s4.setFeature(Feature.PASSIVE, true);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		Assert.assertEquals("however why will the balls be picked up in the shop by Jane and Andrew tomorrow",
		                    //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// how question with modal
		setUp();
		this.s4.setFeature(Feature.PASSIVE, true);
		this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
		this.s4.setFeature(Feature.MODAL, "should"); //$NON-NLS-1$
		Assert.assertEquals("however how should the balls be picked up in the shop by Jane and Andrew tomorrow",
		                    //$NON-NLS-1$
		                    this.realiser.realise(this.s4).getRealisation());

		// indirect object
		setUp();
		this.realiser.setLexicon(this.lexicon);
		this.s3.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_INDIRECT_OBJECT);
		Assert.assertEquals("who does the man give John's flower to", //$NON-NLS-1$
		                    this.realiser.realise(this.s3).getRealisation());
	}

	/**
	 * WH movement in the progressive
	 */
	@Test
	public void testProgrssiveWHSubjectQuestions() {
		SPhraseSpec p = this.phraseFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("eat");
		p.setObject(this.phraseFactory.createNounPhrase("the", "pie"));
		p.setFeature(Feature.PROGRESSIVE, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who is eating the pie", //$NON-NLS-1$
		                    this.realiser.realise(p).getRealisation());
	}

	/**
	 * WH movement in the progressive
	 */
	@Test
	public void testProgrssiveWHObjectQuestions() {
		SPhraseSpec p = this.phraseFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("eat");
		p.setObject(this.phraseFactory.createNounPhrase("the", "pie"));
		p.setFeature(Feature.PROGRESSIVE, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what is Mary eating", //$NON-NLS-1$
		                    this.realiser.realise(p).getRealisation());

		// AG -- need to check this; it doesn't work
		// p.setFeature(Feature.NEGATED, true);
		//		Assert.assertEquals("what is Mary not eating", //$NON-NLS-1$
		// this.realiser.realise(p).getRealisation());

	}

	/**
	 * Negation with WH movement for subject
	 */
	@Test
	public void testNegatedWHSubjQuestions() {
		SPhraseSpec p = this.phraseFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("eat");
		p.setObject(this.phraseFactory.createNounPhrase("the", "pie"));
		p.setFeature(Feature.NEGATED, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who does not eat the pie", //$NON-NLS-1$
		                    this.realiser.realise(p).getRealisation());
	}

	/**
	 * Negation with WH movement for object
	 */
	@Test
	public void testNegatedWHObjQuestions() {
		SPhraseSpec p = this.phraseFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("eat");
		p.setObject(this.phraseFactory.createNounPhrase("the", "pie"));
		p.setFeature(Feature.NEGATED, true);

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		NLGElement realisation = this.realiser.realise(p);
		Assert.assertEquals("what does Mary not eat", //$NON-NLS-1$
		                    realisation.getRealisation());
	}

	/**
	 * Test questyions in the tutorial.
	 */
	@Test
	public void testTutorialQuestions() {
		setUp();
		this.realiser.setLexicon(this.lexicon);

		PhraseElement p = this.phraseFactory.createClause("Mary", "chase", //$NON-NLS-1$ //$NON-NLS-2$
		                                                  "George"); //$NON-NLS-1$
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("does Mary chase George", this.realiser.realise(p) //$NON-NLS-1$
				.getRealisation());

		p = this.phraseFactory.createClause("Mary", "chase", //$NON-NLS-1$ //$NON-NLS-2$
		                                    "George"); //$NON-NLS-1$
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
		Assert.assertEquals("who does Mary chase", this.realiser.realise(p) //$NON-NLS-1$
				.getRealisation());

	}

	/**
	 * Subject WH Questions with modals
	 */
	@Test
	public void testModalWHSubjectQuestion() {
		SPhraseSpec p = this.phraseFactory.createClause(this.dog, "upset", this.man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("the dog upset the man", this.realiser.realise(p).getRealisation());

		// first without modal
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who upset the man", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("what upset the man", this.realiser.realise(p).getRealisation());

		// now with modal auxiliary
		p.setFeature(Feature.MODAL, "may");

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who may have upset the man", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.TENSE, Tense.FUTURE);
		Assert.assertEquals("who may upset the man", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("what may have upset the man", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.TENSE, Tense.FUTURE);
		Assert.assertEquals("what may upset the man", this.realiser.realise(p).getRealisation());
	}

	/**
	 * Subject WH Questions with modals
	 */
	@Test
	public void testModalWHObjectQuestion() {
		SPhraseSpec p = this.phraseFactory.createClause(this.dog, "upset", this.man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);

		Assert.assertEquals("who did the dog upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.MODAL, "may");
		Assert.assertEquals("who may the dog have upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what may the dog have upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
		Assert.assertEquals("who may the dog upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what may the dog upset", this.realiser.realise(p).getRealisation());
	}

	/**
	 * Questions with tenses requiring auxiliaries + subject WH
	 */
	@Test
	public void testAuxWHSubjectQuestion() {
		SPhraseSpec p = this.phraseFactory.createClause(this.dog, "upset", this.man);
		p.setFeature(Feature.TENSE, Tense.PRESENT);
		p.setFeature(Feature.PERFECT, true);
		Assert.assertEquals("the dog has upset the man", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who has upset the man", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("what has upset the man", this.realiser.realise(p).getRealisation());
	}

	/**
	 * Questions with tenses requiring auxiliaries + subject WH
	 */
	@Test
	public void testAuxWHObjectQuestion() {
		SPhraseSpec p = this.phraseFactory.createClause(this.dog, "upset", this.man);

		// first without any aux
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what did the dog upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
		Assert.assertEquals("who did the dog upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.TENSE, Tense.PRESENT);
		p.setFeature(Feature.PERFECT, true);

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
		Assert.assertEquals("who has the dog upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what has the dog upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.PERFECT, true);

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
		Assert.assertEquals("who will the dog have upset", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what will the dog have upset", this.realiser.realise(p).getRealisation());

	}

	/**
	 * Test for questions with "be"
	 */
	@Test
	public void testBeQuestions() {
		SPhraseSpec p = this.phraseFactory.createClause(this.phraseFactory.createNounPhrase("a", "ball"),
		                                                this.phraseFactory.createWord("be", LexicalCategory.VERB),
		                                                this.phraseFactory.createNounPhrase("a", "toy"));

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what is a ball", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("is a ball a toy", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("what is a toy", this.realiser.realise(p).getRealisation());

		SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "be", "beautiful");
		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		Assert.assertEquals("why is Mary beautiful", this.realiser.realise(p2).getRealisation());

		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
		Assert.assertEquals("where is Mary beautiful", this.realiser.realise(p2).getRealisation());

		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who is beautiful", this.realiser.realise(p2).getRealisation());
	}

	/**
	 * Test for questions with "be" in future tense
	 */
	@Test
	public void testBeQuestionsFuture() {
		SPhraseSpec p = this.phraseFactory.createClause(this.phraseFactory.createNounPhrase("a", "ball"),
		                                                this.phraseFactory.createWord("be", LexicalCategory.VERB),
		                                                this.phraseFactory.createNounPhrase("a", "toy"));
		p.setFeature(Feature.TENSE, Tense.FUTURE);

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what will a ball be", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("will a ball be a toy", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("what will be a toy", this.realiser.realise(p).getRealisation());

		SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "be", "beautiful");
		p2.setFeature(Feature.TENSE, Tense.FUTURE);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		Assert.assertEquals("why will Mary be beautiful", this.realiser.realise(p2).getRealisation());

		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
		Assert.assertEquals("where will Mary be beautiful", this.realiser.realise(p2).getRealisation());

		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who will be beautiful", this.realiser.realise(p2).getRealisation());
	}

	/**
	 * Tests for WH questions with be in past tense
	 */
	@Test
	public void testBeQuestionsPast() {
		SPhraseSpec p = this.phraseFactory.createClause(this.phraseFactory.createNounPhrase("a", "ball"),
		                                                this.phraseFactory.createWord("be", LexicalCategory.VERB),
		                                                this.phraseFactory.createNounPhrase("a", "toy"));
		p.setFeature(Feature.TENSE, Tense.PAST);

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what was a ball", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("was a ball a toy", this.realiser.realise(p).getRealisation());

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("what was a toy", this.realiser.realise(p).getRealisation());

		SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "be", "beautiful");
		p2.setFeature(Feature.TENSE, Tense.PAST);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		Assert.assertEquals("why was Mary beautiful", this.realiser.realise(p2).getRealisation());

		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
		Assert.assertEquals("where was Mary beautiful", this.realiser.realise(p2).getRealisation());

		p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("who was beautiful", this.realiser.realise(p2).getRealisation());
	}

	/**
	 * Test WHERE, HOW and WHY questions, with copular predicate "be"
	 */
	public void testSimpleBeWHQuestions() {
		SPhraseSpec p = this.phraseFactory.createClause("I", "be");

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
		Assert.assertEquals("Where am I?", realiser.realiseSentence(p));

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		Assert.assertEquals("Why am I?", realiser.realiseSentence(p));

		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
		Assert.assertEquals("How am I?", realiser.realiseSentence(p));

	}

	/**
	 * Test a simple "how" question, based on query from Albi Oxa
	 */
	@Test
	public void testHowPredicateQuestion() {
		SPhraseSpec test = this.phraseFactory.createClause();
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("You");

		subject.setFeature(Feature.PRONOMINAL, true);
		subject.setFeature(Feature.PERSON, Person.SECOND);
		test.setSubject(subject);
		test.setVerb("be");

		test.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW_PREDICATE);
		test.setFeature(Feature.TENSE, Tense.PRESENT);

		String result = realiser.realiseSentence(test);
		Assert.assertEquals("How are you?", result);

	}

	/**
	 * Case 1 checks that "What do you think about John?" can be generated.
	 * <p>
	 * Case 2 checks that the same clause is generated, even when an object is
	 * declared.
	 */
	@Test
	public void testWhatObjectInterrogative() {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlg = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);

		// Case 1, no object is explicitly given:
		SPhraseSpec clause = nlg.createClause("you", "think");
		PPPhraseSpec aboutJohn = nlg.createPrepositionPhrase("about", "John");
		clause.addPostModifier(aboutJohn);
		clause.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		String realisation = realiser.realiseSentence(clause);
		System.out.println(realisation);
		Assert.assertEquals("What do you think about John?", realisation);

		// Case 2:
		// Add "bad things" as the object so the object doesn't remain null:
		clause.setObject("bad things");
		realisation = realiser.realiseSentence(clause);
		System.out.println(realiser.realiseSentence(clause));
		Assert.assertEquals("What do you think about John?", realisation);
	}
}
