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
package simplenlg.external;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * Tests from third parties
 *
 * @author ereiter
 */
public class ExternalTest {

	private Lexicon    lexicon       = null;
	private NLGFactory phraseFactory = null;
	private Realiser   realiser      = null;

	@Before
	public void setup() {
		lexicon = Lexicon.getDefaultLexicon();
		phraseFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
	}

	/**
	 * Basic tests
	 */
	@Test
	public void forcherTest() {
		// Bjorn Forcher's tests
		this.phraseFactory.setLexicon(this.lexicon);
		PhraseElement s1 = this.phraseFactory.createClause(null, "associate", "Marie");
		s1.setFeature(Feature.PASSIVE, true);
		PhraseElement pp1 = this.phraseFactory.createPrepositionPhrase("with"); //$NON-NLS-1$
		pp1.addComplement("Peter"); //$NON-NLS-1$
		pp1.addComplement("Paul"); //$NON-NLS-1$
		s1.addPostModifier(pp1);

		Assert.assertEquals("Marie is associated with Peter and Paul", //$NON-NLS-1$
		                    this.realiser.realise(s1).getRealisation());
		SPhraseSpec s2 = this.phraseFactory.createClause();
		s2.setSubject(this.phraseFactory.createNounPhrase("Peter")); //$NON-NLS-1$
		s2.setVerb("have"); //$NON-NLS-1$
		s2.setObject("something to do"); //$NON-NLS-1$
		s2.addPostModifier(this.phraseFactory.createPrepositionPhrase("with", "Paul")); //$NON-NLS-1$ //$NON-NLS-2$

		Assert.assertEquals("Peter has something to do with Paul", //$NON-NLS-1$
		                    this.realiser.realise(s2).getRealisation());
	}

	@Test
	public void luTest() {
		// Xin Lu's test
		this.phraseFactory.setLexicon(this.lexicon);
		PhraseElement s1 = this.phraseFactory.createClause("we", //$NON-NLS-1$
		                                                   "consider", //$NON-NLS-1$
		                                                   "John"); //$NON-NLS-1$
		s1.addPostModifier("a friend"); //$NON-NLS-1$

		Assert.assertEquals("we consider John a friend", this.realiser //$NON-NLS-1$
				.realise(s1).getRealisation());
	}

	@Test
	public void dwightTest() {
		// Rachel Dwight's test
		this.phraseFactory.setLexicon(this.lexicon);

		NPPhraseSpec noun4 = this.phraseFactory.createNounPhrase("FGFR3 gene in every cell"); //$NON-NLS-1$

		noun4.setSpecifier("the");

		PhraseElement prep1 = this.phraseFactory.createPrepositionPhrase("of", noun4); //$NON-NLS-1$

		PhraseElement noun1 = this.phraseFactory.createNounPhrase("the",
		                                                          "patient's mother"); //$NON-NLS-1$ //$NON-NLS-2$

		PhraseElement noun2 = this.phraseFactory.createNounPhrase("the",
		                                                          "patient's father"); //$NON-NLS-1$ //$NON-NLS-2$

		PhraseElement noun3 = this.phraseFactory.createNounPhrase("changed copy"); //$NON-NLS-1$
		noun3.addPreModifier("one"); //$NON-NLS-1$
		noun3.addComplement(prep1);

		CoordinatedPhraseElement coordNoun1 = new CoordinatedPhraseElement(noun1, noun2);
		coordNoun1.setConjunction("or"); //$NON-NLS-1$

		PhraseElement verbPhrase1 = this.phraseFactory.createVerbPhrase("have"); //$NON-NLS-1$
		verbPhrase1.setFeature(Feature.TENSE, Tense.PRESENT);

		PhraseElement sentence1 = this.phraseFactory.createClause(coordNoun1, verbPhrase1, noun3);

		realiser.setDebugMode(true);
		Assert.assertEquals(
				"the patient's mother or the patient's father has one changed copy of the FGFR3 gene in every cell",
				//$NON-NLS-1$
				this.realiser.realise(sentence1).getRealisation());

		// Rachel's second test
		noun3 = this.phraseFactory.createNounPhrase("a", "gene test"); //$NON-NLS-1$ //$NON-NLS-2$
		noun2 = this.phraseFactory.createNounPhrase("an", "LDL test"); //$NON-NLS-1$ //$NON-NLS-2$
		noun1 = this.phraseFactory.createNounPhrase("the", "clinic"); //$NON-NLS-1$ //$NON-NLS-2$
		verbPhrase1 = this.phraseFactory.createVerbPhrase("perform"); //$NON-NLS-1$

		CoordinatedPhraseElement coord1 = new CoordinatedPhraseElement(noun2, noun3);
		sentence1 = this.phraseFactory.createClause(noun1, verbPhrase1, coord1);
		sentence1.setFeature(Feature.TENSE, Tense.PAST);

		Assert.assertEquals("the clinic performed an LDL test and a gene test", this.realiser //$NON-NLS-1$
				.realise(sentence1).getRealisation());
	}

	@Test
	public void novelliTest() {
		// Nicole Novelli's test
		PhraseElement p = this.phraseFactory.createClause("Mary",
		                                                  "chase",
		                                                  "George"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		PhraseElement pp = this.phraseFactory.createPrepositionPhrase("in", "the park"); //$NON-NLS-1$ //$NON-NLS-2$
		p.addPostModifier(pp);

		Assert.assertEquals("Mary chases George in the park", this.realiser //$NON-NLS-1$
				.realise(p).getRealisation());

		// another question from Nicole
		SPhraseSpec run = this.phraseFactory.createClause("you",
		                                                  "go",
		                                                  "running"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		run.setFeature(Feature.MODAL, "should"); //$NON-NLS-1$
		run.addPreModifier("really"); //$NON-NLS-1$
		SPhraseSpec think = this.phraseFactory.createClause("I", "think"); //$NON-NLS-1$ //$NON-NLS-2$
		think.setObject(run);
		run.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);

		String text = this.realiser.realise(think).getRealisation();
		Assert.assertEquals("I think you should really go running", text); //$NON-NLS-1$
	}

	@Test
	public void piotrekTest() {
		// Piotrek Smulikowski's test
		this.phraseFactory.setLexicon(this.lexicon);
		PhraseElement sent = this.phraseFactory.createClause("I",
		                                                     "shoot",
		                                                     "the duck"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		sent.setFeature(Feature.TENSE, Tense.PAST);

		PhraseElement loc = this.phraseFactory.createPrepositionPhrase("at",
		                                                               "the Shooting Range"); //$NON-NLS-1$ //$NON-NLS-2$
		sent.addPostModifier(loc);
		sent.setFeature(Feature.CUE_PHRASE, "then"); //$NON-NLS-1$

		Assert.assertEquals("then I shot the duck at the Shooting Range", //$NON-NLS-1$
		                    this.realiser.realise(sent).getRealisation());
	}

	@Test
	public void prescottTest() {
		// Michael Prescott's test
		this.phraseFactory.setLexicon(this.lexicon);
		PhraseElement embedded = this.phraseFactory.createClause("Jill",
		                                                         "prod",
		                                                         "Spot"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		PhraseElement sent = this.phraseFactory.createClause("Jack", "see", embedded); //$NON-NLS-1$ //$NON-NLS-2$
		embedded.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
		embedded.setFeature(Feature.FORM, Form.BARE_INFINITIVE);

		Assert.assertEquals("Jack sees Jill prod Spot", this.realiser //$NON-NLS-1$
				.realise(sent).getRealisation());
	}

	@Test
	public void wissnerTest() {
		// Michael Wissner's text
		PhraseElement p = this.phraseFactory.createClause("a wolf", "eat"); //$NON-NLS-1$ //$NON-NLS-2$
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("what does a wolf eat", this.realiser.realise(p) //$NON-NLS-1$
				.getRealisation());

	}

	@Test
	public void phanTest() {
		// Thomas Phan's text
		PhraseElement subjectElement = this.phraseFactory.createNounPhrase("I");
		PhraseElement verbElement = this.phraseFactory.createVerbPhrase("run");

		PhraseElement prepPhrase = this.phraseFactory.createPrepositionPhrase("from");
		prepPhrase.addComplement("home");

		verbElement.addComplement(prepPhrase);
		SPhraseSpec newSentence = this.phraseFactory.createClause();
		newSentence.setSubject(subjectElement);
		newSentence.setVerbPhrase(verbElement);

		Assert.assertEquals("I run from home", this.realiser.realise(newSentence) //$NON-NLS-1$
				.getRealisation());

	}

	@Test
	public void kerberTest() {
		// Frederic Kerber's tests
		SPhraseSpec sp = this.phraseFactory.createClause("he", "need");
		SPhraseSpec secondSp = this.phraseFactory.createClause();
		secondSp.setVerb("build");
		secondSp.setObject("a house");
		secondSp.setFeature(Feature.FORM, Form.INFINITIVE);
		sp.setObject("stone");
		sp.addComplement(secondSp);
		Assert.assertEquals("he needs stone to build a house", this.realiser.realise(sp).getRealisation());

		SPhraseSpec sp2 = this.phraseFactory.createClause("he", "give");
		sp2.setIndirectObject("I");
		sp2.setObject("the book");
		Assert.assertEquals("he gives me the book", this.realiser.realise(sp2).getRealisation());

	}

	@Test
	public void stephensonTest() {
		// Bruce Stephenson's test
		SPhraseSpec qs2 = this.phraseFactory.createClause();
		qs2 = this.phraseFactory.createClause();
		qs2.setSubject("moles of Gold");
		qs2.setVerb("are");
		qs2.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		qs2.setFeature(Feature.PASSIVE, false);
		qs2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW_MANY);
		qs2.setObject("in a 2.50 g sample of pure Gold");
		DocumentElement sentence = this.phraseFactory.createSentence(qs2);
		Assert.assertEquals("How many moles of Gold are in a 2.50 g sample of pure Gold?",
		                    this.realiser.realise(sentence).getRealisation());
	}

	@Test
	public void pierreTest() {
		// John Pierre's test
		SPhraseSpec p = this.phraseFactory.createClause("Mary", "chase", "George");
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("What does Mary chase?", realiser.realiseSentence(p));

		p = this.phraseFactory.createClause("Mary", "chase", "George");
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		Assert.assertEquals("Does Mary chase George?", realiser.realiseSentence(p));

		p = this.phraseFactory.createClause("Mary", "chase", "George");
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
		Assert.assertEquals("Where does Mary chase George?", realiser.realiseSentence(p));

		p = this.phraseFactory.createClause("Mary", "chase", "George");
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		Assert.assertEquals("Why does Mary chase George?", realiser.realiseSentence(p));

		p = this.phraseFactory.createClause("Mary", "chase", "George");
		p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
		Assert.assertEquals("How does Mary chase George?", realiser.realiseSentence(p));

	}

	@Test
	public void data2TextTest() {
		// Data2Text tests
		// test OK to have number at end of sentence
		SPhraseSpec p = this.phraseFactory.createClause("the dog", "weigh", "12");
		Assert.assertEquals("The dog weighs 12.", realiser.realiseSentence(p));

		// test OK to have "there be" sentence with "there" as a StringElement
		NLGElement dataDropout2 = this.phraseFactory.createNLGElement("data dropouts");
		dataDropout2.setPlural(true);
		SPhraseSpec sentence2 = this.phraseFactory.createClause();
		sentence2.setSubject(this.phraseFactory.createStringElement("there"));
		sentence2.setVerb("be");
		sentence2.setObject(dataDropout2);
		Assert.assertEquals("There are data dropouts.", realiser.realiseSentence(sentence2));

		// test OK to have gerund form verb
		SPhraseSpec weather1 = this.phraseFactory.createClause("SE 10-15", "veer", "S 15-20");
		weather1.setFeature(Feature.FORM, Form.GERUND);
		Assert.assertEquals("SE 10-15 veering S 15-20.", realiser.realiseSentence(weather1));

		// test OK to have subject only
		SPhraseSpec weather2 = this.phraseFactory.createClause("cloudy and misty", "be", "XXX");
		weather2.getVerbPhrase().setFeature(Feature.ELIDED, true);
		Assert.assertEquals("Cloudy and misty.", realiser.realiseSentence(weather2));

		// test OK to have VP only
		SPhraseSpec weather3 = this.phraseFactory.createClause("S 15-20", "increase", "20-25");
		weather3.setFeature(Feature.FORM, Form.GERUND);
		weather3.getSubject().setFeature(Feature.ELIDED, true);
		Assert.assertEquals("Increasing 20-25.", realiser.realiseSentence(weather3));

		// conjoined test
		SPhraseSpec weather4 = this.phraseFactory.createClause("S 20-25", "back", "SSE");
		weather4.setFeature(Feature.FORM, Form.GERUND);
		weather4.getSubject().setFeature(Feature.ELIDED, true);

		CoordinatedPhraseElement coord = new CoordinatedPhraseElement();
		coord.addCoordinate(weather1);
		coord.addCoordinate(weather3);
		coord.addCoordinate(weather4);
		coord.setConjunction("then");
		Assert.assertEquals("SE 10-15 veering S 15-20, increasing 20-25 then backing SSE.",
		                    realiser.realiseSentence(coord));

		// no verb
		SPhraseSpec weather5 = this.phraseFactory.createClause("rain", null, "likely");
		Assert.assertEquals("Rain likely.", realiser.realiseSentence(weather5));

	}

	@Test
	public void rafaelTest() {
		// Rafael Valle's tests
		List<NLGElement> ss = new ArrayList<NLGElement>();
		ClauseCoordinationRule coord = new ClauseCoordinationRule();
		coord.setFactory(this.phraseFactory);

		ss.add(this.agreePhrase("John Lennon")); // john lennon agreed with it  
		ss.add(this.disagreePhrase("Geri Halliwell")); // Geri Halliwell disagreed with it
		ss.add(this.commentPhrase("Melanie B")); // Mealnie B commented on it
		ss.add(this.agreePhrase("you")); // you agreed with it
		ss.add(this.commentPhrase("Emma Bunton")); //Emma Bunton commented on it

		List<NLGElement> results = coord.apply(ss);
		List<String> ret = this.realizeAll(results);
		Assert.assertEquals(
				"[John Lennon and you agreed with it, Geri Halliwell disagreed with it, Melanie B and Emma Bunton commented on it]",
				ret.toString());
	}

	private NLGElement commentPhrase(String name) {  // used by testRafael
		SPhraseSpec s = this.phraseFactory.createClause();
		s.setSubject(this.phraseFactory.createNounPhrase(name));
		s.setVerbPhrase(this.phraseFactory.createVerbPhrase("comment on"));
		s.setObject("it");
		s.setFeature(Feature.TENSE, Tense.PAST);
		return s;
	}

	private NLGElement agreePhrase(String name) {  // used by testRafael
		SPhraseSpec s = this.phraseFactory.createClause();
		s.setSubject(this.phraseFactory.createNounPhrase(name));
		s.setVerbPhrase(this.phraseFactory.createVerbPhrase("agree with"));
		s.setObject("it");
		s.setFeature(Feature.TENSE, Tense.PAST);
		return s;
	}

	private NLGElement disagreePhrase(String name) {  // used by testRafael
		SPhraseSpec s = this.phraseFactory.createClause();
		s.setSubject(this.phraseFactory.createNounPhrase(name));
		s.setVerbPhrase(this.phraseFactory.createVerbPhrase("disagree with"));
		s.setObject("it");
		s.setFeature(Feature.TENSE, Tense.PAST);
		return s;
	}

	private ArrayList<String> realizeAll(List<NLGElement> results) { // used by testRafael
		ArrayList<String> ret = new ArrayList<String>();
		for(NLGElement e : results) {
			String r = this.realiser.realise(e).getRealisation();
			ret.add(r);
		}
		return ret;
	}

	@Test
	public void wikipediaTest() {
		// test code fragments in wikipedia
		// realisation
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("the", "woman");
		subject.setPlural(true);
		SPhraseSpec sentence = this.phraseFactory.createClause(subject, "smoke");
		sentence.setFeature(Feature.NEGATED, true);
		Assert.assertEquals("The women do not smoke.", realiser.realiseSentence(sentence));

		// aggregation
		SPhraseSpec s1 = this.phraseFactory.createClause("the man", "be", "hungry");
		SPhraseSpec s2 = this.phraseFactory.createClause("the man", "buy", "an apple");
		NLGElement result = new ClauseCoordinationRule().apply(s1, s2);
		Assert.assertEquals("The man is hungry and buys an apple.", realiser.realiseSentence(result));

	}

	@Test
	public void leanTest() {
		// A Lean's test
		SPhraseSpec sentence = this.phraseFactory.createClause();
		sentence.setVerb("be");
		sentence.setObject("a ball");
		sentence.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("What is a ball?", realiser.realiseSentence(sentence));

		sentence = this.phraseFactory.createClause();
		sentence.setVerb("be");
		NPPhraseSpec object = this.phraseFactory.createNounPhrase("example");
		object.setPlural(true);
		object.addModifier("of jobs");
		sentence.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
		sentence.setObject(object);
		Assert.assertEquals("What are examples of jobs?", realiser.realiseSentence(sentence));

		SPhraseSpec p = this.phraseFactory.createClause();
		NPPhraseSpec sub1 = this.phraseFactory.createNounPhrase("Mary");

		sub1.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		sub1.setFeature(Feature.PRONOMINAL, true);
		sub1.setFeature(Feature.PERSON, Person.FIRST);
		p.setSubject(sub1);
		p.setVerb("chase");
		p.setObject("the monkey");

		String output2 = this.realiser.realiseSentence(p); // Realiser created earlier.
		Assert.assertEquals("I chase the monkey.", output2);

		SPhraseSpec test = this.phraseFactory.createClause();
		NPPhraseSpec subject = this.phraseFactory.createNounPhrase("Mary");

		subject.setFeature(Feature.PRONOMINAL, true);
		subject.setFeature(Feature.PERSON, Person.SECOND);
		test.setSubject(subject);
		test.setVerb("cry");

		test.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		test.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("Why do you cry?", realiser.realiseSentence(test));

		test = this.phraseFactory.createClause();
		subject = this.phraseFactory.createNounPhrase("Mary");

		subject.setFeature(Feature.PRONOMINAL, true);
		subject.setFeature(Feature.PERSON, Person.SECOND);
		test.setSubject(subject);
		test.setVerb("be");
		test.setObject("crying");

		test.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
		test.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("Why are you crying?", realiser.realiseSentence(test));

	}

	@Test
	public void kalijurandTest() {
		// K Kalijurand's test
		String lemma = "walk";

		WordElement word = this.lexicon.lookupWord(lemma, LexicalCategory.VERB);
		InflectedWordElement inflectedWord = new InflectedWordElement(word);

		inflectedWord.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
		String form = realiser.realise(inflectedWord).getRealisation();
		Assert.assertEquals("walked", form);

		inflectedWord = new InflectedWordElement(word);

		inflectedWord.setFeature(Feature.PERSON, Person.THIRD);
		form = realiser.realise(inflectedWord).getRealisation();
		Assert.assertEquals("walks", form);

	}

	@Test
	public void layTest() {
		// Richard Lay's test
		String lemma = "slap";

		WordElement word = this.lexicon.lookupWord(lemma, LexicalCategory.VERB);
		InflectedWordElement inflectedWord = new InflectedWordElement(word);
		inflectedWord.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
		String form = realiser.realise(inflectedWord).getRealisation();
		Assert.assertEquals("slapping", form);

		VPPhraseSpec v = this.phraseFactory.createVerbPhrase("slap");
		v.setFeature(Feature.PROGRESSIVE, true);
		String progressive = this.realiser.realise(v).getRealisation();
		Assert.assertEquals("is slapping", progressive);
	}
}
