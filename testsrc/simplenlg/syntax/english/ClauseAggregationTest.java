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

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplenlg.aggregation.BackwardConjunctionReductionRule;
import simplenlg.aggregation.Aggregator;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.aggregation.ForwardConjunctionReductionRule;
import simplenlg.features.Feature;
import simplenlg.framework.NLGElement;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * Some tests for aggregation.
 * 
 * @author Albert Gatt, University of Malta & University of Aberdeen
 * 
 */
public class ClauseAggregationTest extends SimpleNLG4Test {
	// set up a few more fixtures
	/** The s4. */
	SPhraseSpec s1, s2, s3, s4, s5, s6;
	Aggregator aggregator;
	ClauseCoordinationRule coord;
	ForwardConjunctionReductionRule fcr;
	BackwardConjunctionReductionRule bcr;

	/**
	 * Instantiates a new clause aggregation test.
	 * 
	 * @param name
	 *            the name
	 */
	public ClauseAggregationTest(String name) {
		super(name);
		aggregator = new Aggregator();
		aggregator.initialise();
		coord = new ClauseCoordinationRule();
		fcr = new ForwardConjunctionReductionRule();
		bcr = new BackwardConjunctionReductionRule();
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

		// the woman kissed the man behind the curtain
		this.s1 = this.phraseFactory.createClause();
		this.s1.setSubject(this.woman);
		this.s1.setVerbPhrase(this.phraseFactory.createVerbPhrase("kiss"));
		this.s1.setObject(this.man);
		this.s1.addPostModifier(this.phraseFactory
				.createPrepositionPhrase("behind", this.phraseFactory
						.createNounPhrase("the", "curtain")));

		// the woman kicked the dog on the rock
		this.s2 = this.phraseFactory.createClause();
		this.s2.setSubject(this.phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
		this.s2.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
		this.s2.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
		this.s2.addPostModifier(this.onTheRock);

		// the woman kicked the dog behind the curtain
		this.s3 = this.phraseFactory.createClause();
		this.s3.setSubject(this.phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
		this.s3.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
		this.s3.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
		this.s3.addPostModifier(this.phraseFactory
				.createPrepositionPhrase("behind", this.phraseFactory
						.createNounPhrase("the", "curtain")));

		// the man kicked the dog behind the curtain
		this.s4 = this.phraseFactory.createClause();
		this.s4.setSubject(this.man); //$NON-NLS-1$
		this.s4.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
		this.s4.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
		this.s4.addPostModifier(this.behindTheCurtain);

		// the girl kicked the dog behind the curtain
		this.s5 = this.phraseFactory.createClause();
		this.s5.setSubject(this.phraseFactory.createNounPhrase("the", "girl")); //$NON-NLS-1$
		this.s5.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
		this.s5.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
		this.s5.addPostModifier(this.behindTheCurtain);

		// the woman kissed the dog behind the curtain
		this.s6 = this.phraseFactory.createClause();
		this.s6.setSubject(this.phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
		this.s6.setVerbPhrase(this.phraseFactory.createVerbPhrase("kiss")); //$NON-NLS-1$
		this.s6.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
		this.s6.addPostModifier(this.phraseFactory
				.createPrepositionPhrase("behind", this.phraseFactory
						.createNounPhrase("the", "curtain")));
	}
	
	@After
	public void tearDown() {
		super.tearDown();
		
		this.s1 = null; this.s2 = null; this.s3 = null; 
		this.s4 = null; this.s5 = null; this.s6 = null;
		this.aggregator = null;
		this.coord = null;
		this.fcr = null;
		this.bcr = null;
	}

	/**
	 * Test clause coordination with two sentences with same subject but
	 * different postmodifiers: fails
	 */
	@Test
	public void testCoordinationSameSubjectFail() {
		List<NLGElement> elements = Arrays.asList((NLGElement) this.s1,
				(NLGElement) this.s2);
		List<NLGElement> result = this.coord.apply(elements);
		Assert.assertEquals(2, result.size());
	}

	/**
	 * Test clause coordination with two sentences one of which is passive:
	 * fails
	 */
	@Test
	public void testCoordinationPassiveFail() {
		this.s1.setFeature(Feature.PASSIVE, true);
		List<NLGElement> elements = Arrays.asList((NLGElement) this.s1,
				(NLGElement) this.s2);
		List<NLGElement> result = this.coord.apply(elements);
		Assert.assertEquals(2, result.size());
	}

//	/**
//	 * Test clause coordination with 2 sentences with same subject: succeeds
//	 */
//	@Test
//	public void testCoordinationSameSubjectSuccess() {
//		List<NLGElement> elements = Arrays.asList((NLGElement) this.s1,
//				(NLGElement) this.s3);
//		List<NLGElement> result = this.coord.apply(elements);
//		Assert.assertTrue(result.size() == 1); // should only be one sentence
//		NLGElement aggregated = result.get(0);
//		Assert
//				.assertEquals(
//						"the woman kisses the man and kicks the dog behind the curtain", //$NON-NLS-1$
//						this.realiser.realise(aggregated).getRealisation());
//	}

	/**
	 * Test clause coordination with 2 sentences with same VP: succeeds
	 */
	@Test
	public void testCoordinationSameVP() {
		List<NLGElement> elements = Arrays.asList((NLGElement) this.s3,
				(NLGElement) this.s4);
		List<NLGElement> result = this.coord.apply(elements);
		Assert.assertTrue(result.size() == 1); // should only be one sentence
		NLGElement aggregated = result.get(0);
		Assert.assertEquals(
				"the woman and the man kick the dog behind the curtain", //$NON-NLS-1$
				this.realiser.realise(aggregated).getRealisation());
	}

	/**
	 * Coordination of sentences with front modifiers: should preserve the mods
	 */
	@Test
	public void testCoordinationWithModifiers() {
		// now add a couple of front modifiers
		this.s3.addFrontModifier(this.phraseFactory
				.createAdverbPhrase("however"));
		this.s4.addFrontModifier(this.phraseFactory
				.createAdverbPhrase("however"));
		List<NLGElement> elements = Arrays.asList((NLGElement) this.s3,
				(NLGElement) this.s4);
		List<NLGElement> result = this.coord.apply(elements);
		Assert.assertTrue(result.size() == 1); // should only be one sentence
		NLGElement aggregated = result.get(0);
		Assert
				.assertEquals(
						"however the woman and the man kick the dog behind the curtain", //$NON-NLS-1$
						this.realiser.realise(aggregated).getRealisation());
	}

	/**
	 * Test coordination of 3 sentences with the same VP
	 */
	public void testCoordinationSameVP2() {
		List<NLGElement> elements = Arrays.asList((NLGElement) this.s3,
				(NLGElement) this.s4, (NLGElement) this.s5);
		List<NLGElement> result = this.coord.apply(elements);
		Assert.assertTrue(result.size() == 1); // should only be one sentence
		NLGElement aggregated = result.get(0);
		Assert
				.assertEquals(
						"the woman and the man and the girl kick the dog behind the curtain", //$NON-NLS-1$
						this.realiser.realise(aggregated).getRealisation());
	}

	/**
	 * Forward conjunction reduction test
	 */
	@Test
	public void testForwardConjReduction() {
		NLGElement aggregated = this.fcr.apply(this.s2, this.s3);
		Assert
				.assertEquals(
						"the woman kicks the dog on the rock and kicks the dog behind the curtain", //$NON-NLS-1$
						this.realiser.realise(aggregated).getRealisation());
	}

	/**
	 * Backward conjunction reduction test
	 */
	@Test
	public void testBackwardConjunctionReduction() {
		NLGElement aggregated = this.bcr.apply(this.s3, this.s6);
		Assert
				.assertEquals(
						"the woman kicks and the woman kisses the dog behind the curtain",
						this.realiser.realise(aggregated).getRealisation());
	}
	
	/**
	 * Test multiple aggregation procedures in a single aggregator. 
	 */
//	@Test
//	public void testForwardBackwardConjunctionReduction() {
//		this.aggregator.addRule(this.fcr);
//		this.aggregator.addRule(this.bcr);
//		realiser.setDebugMode(true);
//		List<NLGElement> result = this.aggregator.realise(Arrays.asList((NLGElement) this.s2, (NLGElement) this.s3));
//		Assert.assertTrue(result.size() == 1); // should only be one sentence
//		NLGElement aggregated = result.get(0);
//		NLGElement aggregated = this.phraseFactory.createdCoordinatedPhrase(this.s2, this.s3);
//		Assert
//				.assertEquals(
//						"the woman kicks the dog on the rock and behind the curtain", //$NON-NLS-1$
//						this.realiser.realise(aggregated).getRealisation());
//	}

}
