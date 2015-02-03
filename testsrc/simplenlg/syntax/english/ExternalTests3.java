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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.syntax.english;

import junit.framework.Assert;

import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * Further tests from third parties. This test class exists mainly to host code
 * to address questions posted by users at the SimpleNLG forum.
 * 
 * @author Rodrigo de Oliveira, University of Aberdeen
 * 
 */
public class ExternalTests3 {

	/**
	 * Case 1 checks that "What do you think about John?" can be generated.
	 * 
	 * Case 2 checks that the same clause is generated, even when an object is
	 * declared.
	 */
	@Test
	public void test01() {

		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlg = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);

		// Case 1, no object is explicitly given:
		SPhraseSpec clause = nlg.createClause("you", "think");
		PPPhraseSpec aboutJohn = nlg.createPrepositionPhrase("about", "John");
		clause.addPostModifier(aboutJohn);
		clause.setFeature(Feature.INTERROGATIVE_TYPE,
				InterrogativeType.WHAT_OBJECT);
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
