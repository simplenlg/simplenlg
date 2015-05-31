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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood
 */
package simplenlg.syntax.english;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import simplenlg.aggregation.NewAggregator;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGElement;

@Ignore 
public class ClauseAggregationTest2 extends SimpleNLG4Test {

	private NewAggregator aggregator;
	private NLGElement s1, s2, s3, s4;

	/**
	 * Instantiates a new clause aggregation test.
	 * 
	 * @param name
	 *            the name
	 */
	public ClauseAggregationTest2(String name) {
		super(name);
		aggregator = new NewAggregator();
		aggregator.initialise();
	}

	@Before
	public void setUp() {
		super.setUp();
		this.s1 = this.phraseFactory.createClause("John", "write",
				this.phraseFactory.createNounPhrase("an", "article"));
		this.s2 = this.phraseFactory.createClause("Mary", "edit",
				this.phraseFactory.createNounPhrase("an", "article"));
		this.s1.setFeature(Feature.PROGRESSIVE, true);
		this.s2.setFeature(Feature.PROGRESSIVE, true);
		this.s3 = this.phraseFactory.createClause("John", "read",
				this.phraseFactory.createNounPhrase("a", "book"));
		this.s4 = this.phraseFactory.createClause("Mary", "read",
				this.phraseFactory.createNounPhrase("an", "article"));
		this.s3.setFeature(Feature.TENSE, Tense.PAST);
		this.s4.setFeature(Feature.TENSE, Tense.PAST);
		this.s3.setFeature(Feature.PROGRESSIVE, true);
		this.s4.setFeature(Feature.PROGRESSIVE, true);
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
		
		this.aggregator = null;
		this.s1 = null; this.s2 = null; this.s3 = null; this.s4 = null;
		
	}
	
	
	
	//Functionality disabled for the time being
	// @Test
	// public void testConjReduction1() {
	// NLGElement element = this.aggregator.realise(this.s1, this.s2);
	// Assert.assertEquals("John is writing and Mary is editing an article",
	// this.realiser.realise(element).getRealisation());
	// }
	//
	// @Test
	// public void testConjReduction2() {
	// this.s1.setFeature(Feature.PASSIVE, true);
	// this.s2.setFeature(Feature.PASSIVE, true);
	// NLGElement element = this.aggregator.realise(this.s1, this.s2);
	// Assert.assertEquals("John is writing and Mary is editing an article",
	// this.realiser.realise(element).getRealisation());
	// }
	//
	// @Test
	// public void testConjReduction3() {
	// NLGElement element = this.aggregator.realise(this.s3, this.s4);
	// Assert.assertEquals("John was reading a book and Mary an article",
	// this.realiser.realise(element).getRealisation());
	// }

}
