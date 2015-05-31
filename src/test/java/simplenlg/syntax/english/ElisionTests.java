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

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * Tests for elision of phrases and words
 */
public class ElisionTests extends SimpleNLG4Test {

	public ElisionTests(String name) {
		super(name);
	}
	
	/**
	 * Test elision of phrases in various places in the sentence
	 */
//	public void testPhraseElision() {
//		SPhraseSpec s1 = this.phraseFactory.createClause();
//		s1.setSubject(this.np4); //the rock
//		this.kiss.setComplement(this.np5);//kiss the curtain
//		s1.setVerbPhrase(this.kiss);
//		
//		Assert.assertEquals("the rock kisses the curtain", this.realiser.realise(s1).getRealisation());
//		
//		//elide subject np
//		this.np4.setFeature(Feature.ELIDED, true);
//		Assert.assertEquals("kisses the curtain", this.realiser.realise(s1).getRealisation());
//		
//		//elide vp
//		this.np4.setFeature(Feature.ELIDED, false);
//		this.kiss.setFeature(Feature.ELIDED, true);
//		Assert.assertEquals("the rock", this.realiser.realise(s1).getRealisation());
//		
//		//elide complement only
//		this.kiss.setFeature(Feature.ELIDED, false);
//		this.np5.setFeature(Feature.ELIDED, true);
//		Assert.assertEquals("the rock kisses", this.realiser.realise(s1).getRealisation());
//	}
	
	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}
	
	
	/**
     * Test for elision of specific words rather than phrases
     */
	@Test
    public void testWordElision() {
            this.realiser.setDebugMode(true);
            SPhraseSpec s1 = this.phraseFactory.createClause();
            s1.setSubject(this.np4); //the rock
            this.kiss.setComplement(this.np5);//kiss the curtain
            s1.setVerbPhrase(this.kiss);
            
            this.np5.setFeature(Feature.ELIDED, true);
            Assert.assertEquals("the rock kisses", this.realiser.realise(s1).getRealisation());
    }
	
	
	
	/**
	 * Test for elision of specific words rather than phrases
	 *
	@Test
	public void testWordElision() {
		this.realiser.setDebugMode(true);
		SPhraseSpec s1 = this.phraseFactory.createClause();
		s1.setSubject(this.np4); //the rock
		this.kiss.setComplement(this.np5);//kiss the curtain
		s1.setVerbPhrase(this.kiss);
		
		this.kiss.getHead().setFeature(Feature.ELIDED, true);
		Assert.assertEquals("the rock kisses the curtain", this.realiser.realise(s1).getRealisation());
	} */

}
