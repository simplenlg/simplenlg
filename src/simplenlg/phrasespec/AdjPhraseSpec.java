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

package simplenlg.phrasespec;

import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.NLGFactory;
/**
 * <p>
 * This class defines a adjective phrase.  It is essentially
 * a wrapper around the <code>PhraseElement</code> class, with methods
 * for setting common constituents such as preModifier.
 * For example, the <code>setAdjective</code> method in this class sets
 * the head of the element to be the specified adjective
 *
 * From an API perspective, this class is a simplified version of the AdjPhraseSpec
 * class in simplenlg V3.  It provides an alternative way for creating syntactic
 * structures, compared to directly manipulating a V4 <code>PhraseElement</code>.
 * 
 * Methods are provided for setting and getting the following constituents:
 * <UL>
 * <LI>PreModifier		(eg, "very")
 * <LI>Adjective        (eg, "happy")
 * </UL>
 * 
 * NOTE: AdjPhraseSpec do not usually have (user-set) features
 * 
 * <code>AdjPhraseSpec</code> are produced by the <code>createAdjectivePhrase</code>
 * method of a <code>PhraseFactory</code>
 * </p>
 * 
 * 
 * @author E. Reiter, University of Aberdeen.
 * @version 4.1
 * 
 */
public class AdjPhraseSpec extends PhraseElement {

	public AdjPhraseSpec(NLGFactory phraseFactory) {
		super(PhraseCategory.ADJECTIVE_PHRASE);
		this.setFactory(phraseFactory);
	}

	/** sets the adjective (head) of the phrase
	 * @param adjective
	 */
	public void setAdjective(Object adjective) {
		if (adjective instanceof NLGElement)
			setHead(adjective);
		else {
			// create noun as word
			NLGElement adjectiveElement = getFactory().createWord(adjective, LexicalCategory.ADJECTIVE);

			// set head of NP to nounElement
			setHead(adjectiveElement);
		}
	}

	/**
	 * @return adjective (head) of  phrase
	 */
	public NLGElement getAdjective() {
		return getHead();
	}
	
	// inherit usual modifier routines
	
}
