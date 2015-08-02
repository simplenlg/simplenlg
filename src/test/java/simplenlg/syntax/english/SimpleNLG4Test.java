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

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;


import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * This class is the base class for all JUnit simplenlg.test cases for
 * simplenlg. It sets up a a JUnit fixture, i.e. the basic objects (basic
 * constituents) that all other tests can use.
 * @author agatt
 */
public abstract class SimpleNLG4Test extends TestCase {

	/** The realiser. */
	protected Realiser realiser;

	protected NLGFactory phraseFactory;
	
	protected Lexicon lexicon;
	
	/** The pro test2. */
	protected PhraseElement man, woman, dog, boy, np4, np5, np6, proTest1, proTest2;

	/** The salacious. */
	protected PhraseElement beautiful, stunning, salacious;

	/** The under the table. */
	protected PhraseElement onTheRock, behindTheCurtain, inTheRoom, underTheTable;

	/** The say. */
	protected VPPhraseSpec kick, kiss, walk, talk, getUp, fallDown, give, say;

	/**
	 * Instantiates a new simplenlg test.
	 * 
	 * @param name
	 *            the name
	 */
	public SimpleNLG4Test(String name) {
		super(name);
	}

	/**
	 * Set up the variables we'll need for this simplenlg.test to run (Called
	 * automatically by JUnit)
	 */
	@Override
	@Before
	protected void setUp() {
        lexicon = new XMLLexicon();  // built in lexicon

		this.phraseFactory = new NLGFactory(this.lexicon);
		this.realiser = new Realiser(this.lexicon);
		
		this.man = this.phraseFactory.createNounPhrase("the", "man"); //$NON-NLS-1$ //$NON-NLS-2$
		this.woman = this.phraseFactory.createNounPhrase("the", "woman");  //$NON-NLS-1$//$NON-NLS-2$
		this.dog = this.phraseFactory.createNounPhrase("the", "dog"); //$NON-NLS-1$ //$NON-NLS-2$
		this.boy = this.phraseFactory.createNounPhrase("the", "boy"); //$NON-NLS-1$ //$NON-NLS-2$

		this.beautiful = this.phraseFactory.createAdjectivePhrase("beautiful"); //$NON-NLS-1$
		this.stunning = this.phraseFactory.createAdjectivePhrase("stunning"); //$NON-NLS-1$
		this.salacious = this.phraseFactory.createAdjectivePhrase("salacious"); //$NON-NLS-1$

		this.onTheRock = this.phraseFactory.createPrepositionPhrase("on"); //$NON-NLS-1$
		this.np4 = this.phraseFactory.createNounPhrase("the", "rock"); //$NON-NLS-1$ //$NON-NLS-2$
		this.onTheRock.addComplement(this.np4);

		this.behindTheCurtain = this.phraseFactory.createPrepositionPhrase("behind"); //$NON-NLS-1$
		this.np5 = this.phraseFactory.createNounPhrase("the", "curtain"); //$NON-NLS-1$ //$NON-NLS-2$
		this.behindTheCurtain.addComplement(this.np5);

		this.inTheRoom = this.phraseFactory.createPrepositionPhrase("in"); //$NON-NLS-1$
		this.np6 = this.phraseFactory.createNounPhrase("the", "room"); //$NON-NLS-1$ //$NON-NLS-2$
		this.inTheRoom.addComplement(this.np6);

		this.underTheTable = this.phraseFactory.createPrepositionPhrase("under"); //$NON-NLS-1$
		this.underTheTable.addComplement(this.phraseFactory.createNounPhrase("the", "table")); //$NON-NLS-1$ //$NON-NLS-2$

		this.proTest1 = this.phraseFactory.createNounPhrase("the", "singer"); //$NON-NLS-1$ //$NON-NLS-2$
		this.proTest2 = this.phraseFactory.createNounPhrase("some", "person"); //$NON-NLS-1$ //$NON-NLS-2$

		this.kick = this.phraseFactory.createVerbPhrase("kick"); //$NON-NLS-1$
		this.kiss = this.phraseFactory.createVerbPhrase("kiss"); //$NON-NLS-1$
		this.walk = this.phraseFactory.createVerbPhrase("walk"); //$NON-NLS-1$
		this.talk = this.phraseFactory.createVerbPhrase("talk"); //$NON-NLS-1$
		this.getUp = this.phraseFactory.createVerbPhrase("get up"); //$NON-NLS-1$
		this.fallDown = this.phraseFactory.createVerbPhrase("fall down"); //$NON-NLS-1$
		this.give = this.phraseFactory.createVerbPhrase("give"); //$NON-NLS-1$
		this.say = this.phraseFactory.createVerbPhrase("say"); //$NON-NLS-1$
	}
	
	@Override
	@After
	public void tearDown() {
		this.realiser = null;

		this.phraseFactory = null;
		
		if(null != lexicon) {
			lexicon = null;
		}
		
		this.man = null; this.woman = null; this.dog = null; this.boy = null; 
		this.np4 = null; this.np5 = null; this.np6 = null; this.proTest1 = null; 
		this.proTest2 = null;
		
		this.beautiful = null; this.stunning = null; this.salacious = null;

		this.onTheRock = null; this.behindTheCurtain= null; 
		this.inTheRoom = null; this.underTheTable = null;

		this.kick = null; this.kiss = null;  this.walk = null; this.talk = null; 
		this.getUp = null; this.fallDown = null; this.give = null; this.say = null;
	}
	
	
}
