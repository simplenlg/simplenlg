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

import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
/**
 * <p>
 * This class defines a verb phrase.  It is essentially
 * a wrapper around the <code>PhraseElement</code> class, with methods
 * for setting common constituents such as Objects.
 * For example, the <code>setVerb</code> method in this class sets
 * the head of the element to be the specified verb
 *
 * From an API perspective, this class is a simplified version of the SPhraseSpec
 * class in simplenlg V3.  It provides an alternative way for creating syntactic
 * structures, compared to directly manipulating a V4 <code>PhraseElement</code>.
 * 
 * Methods are provided for setting and getting the following constituents: 
 * <UL>
 * <LI>PreModifier		(eg, "reluctantly")
 * <LI>Verb				(eg, "gave")
 * <LI>IndirectObject	(eg, "Mary")
 * <LI>Object	        (eg, "an apple")
 * <LI>PostModifier     (eg, "before school")
 * </UL>
 * 
 * NOTE: If there is a complex verb group, a preModifer set at the VP level appears before
 * the verb, while a preModifier set at the clause level appears before the verb group.  Eg
 *   "Mary unfortunately will eat the apple"  ("unfortunately" is clause preModifier)
 *   "Mary will happily eat the apple"  ("happily" is VP preModifier)
 *   
 * NOTE: The setModifier method will attempt to automatically determine whether
 * a modifier should be expressed as a PreModifier or PostModifier
 * 
 * Features (such as negated) must be accessed via the <code>setFeature</code> and
 * <code>getFeature</code> methods (inherited from <code>NLGElement</code>).
 * Features which are often set on VPPhraseSpec include
 * <UL>
 * <LI>Modal    (eg, "John eats an apple" vs "John can eat an apple")
 * <LI>Negated  (eg, "John eats an apple" vs "John does not eat an apple")
 * <LI>Passive  (eg, "John eats an apple" vs "An apple is eaten by John")
 * <LI>Perfect  (eg, "John ate an apple" vs "John has eaten an apple")
 * <LI>Progressive  (eg, "John eats an apple" vs "John is eating an apple")
 * <LI>Tense    (eg, "John ate" vs "John eats" vs "John will eat")
 * </UL>
 * Note that most VP features can be set on an SPhraseSpec, they will automatically
 * be propogated to the VP
 * 
 * <code>VPPhraseSpec</code> are produced by the <code>createVerbPhrase</code>
 * method of a <code>PhraseFactory</code>
 * </p>
 * 
 * @author E. Reiter, University of Aberdeen.
 * @version 4.1
 * 
 */
public class VPPhraseSpec extends PhraseElement {

	
	/** create an empty clause
	 */
	public VPPhraseSpec(NLGFactory phraseFactory) {
		super(PhraseCategory.VERB_PHRASE);
		this.setFactory(phraseFactory);
		
		// set default feature values
		setFeature(Feature.PERFECT, false);
		setFeature(Feature.PROGRESSIVE, false);
		setFeature(Feature.PASSIVE, false);
		setFeature(Feature.NEGATED, false);
		setFeature(Feature.TENSE, Tense.PRESENT);
		setFeature(Feature.PERSON, Person.THIRD);
		setPlural(false);
		setFeature(Feature.FORM, Form.NORMAL);
		setFeature(InternalFeature.REALISE_AUXILIARY, true);
	}
	
	/** sets the verb (head) of a verb phrase.
	 * Extract particle from verb if necessary
	 * @param verb
	 */
	public void setVerb(Object verb) {
		NLGElement verbElement;
		
		if (verb instanceof String) { // if String given, check for particle
			int space = ((String) verb).indexOf(' ');
			
			if (space == -1) { // no space, so no particle
				verbElement = getFactory().createWord(verb, LexicalCategory.VERB);
			
			} else { // space, so break up into verb and particle
				verbElement = getFactory().createWord(((String) verb)
						.substring(0, space), LexicalCategory.VERB);
				setFeature(Feature.PARTICLE, ((String) verb)
						.substring(space + 1, ((String) verb).length()));
			}
		} else { // Object is not a String
			verbElement = getFactory().createNLGElement(verb,LexicalCategory.VERB);
		}
		setHead(verbElement);
	}
	

	/**
	 * @return verb (head) of verb phrase
	 */
	public NLGElement getVerb() {
		return getHead();
	}
	
	/** Sets the direct object of a clause  (assumes this is the only direct object)
	 *
	 * @param object
	 */
	public void setObject(Object object) {
		NLGElement objectPhrase;
		if (object instanceof PhraseElement || object instanceof CoordinatedPhraseElement)
			objectPhrase = (NLGElement) object;
		else
			objectPhrase = getFactory().createNounPhrase(object);

		objectPhrase.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.OBJECT);
		setComplement(objectPhrase);
	}
	
	
	/** Returns the direct object of a clause (assumes there is only one)
	 * 
	 * @return subject of clause (assume only one)
	 */
	public NLGElement getObject() {
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		for (NLGElement complement: complements)
			if (complement.getFeature(InternalFeature.DISCOURSE_FUNCTION) == DiscourseFunction.OBJECT)
				return complement;
		return null;
	}

	/** Set the indirect object of a clause (assumes this is the only direct indirect object)
	 *
	 * @param indirectObject
	 */
	public void setIndirectObject(Object indirectObject) {
		NLGElement indirectObjectPhrase;
		if (indirectObject instanceof PhraseElement || indirectObject instanceof CoordinatedPhraseElement)
			indirectObjectPhrase = (NLGElement) indirectObject;
		else
			indirectObjectPhrase = getFactory().createNounPhrase(indirectObject);

		indirectObjectPhrase.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.INDIRECT_OBJECT);
		setComplement(indirectObjectPhrase);
	}
	
	/** Returns the indirect object of a clause (assumes there is only one)
	 * 
	 * @return subject of clause (assume only one)
	 */
	public NLGElement getIndirectObject() {
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		for (NLGElement complement: complements)
			if (complement.getFeature(InternalFeature.DISCOURSE_FUNCTION) == DiscourseFunction.INDIRECT_OBJECT)
				return complement;
		return null;
	}

	// note that addFrontModifier, addPostModifier, addPreModifier are inherited from PhraseElement
	// likewise getFrontModifiers, getPostModifiers, getPreModifiers

	
	/** Add a modifier to a verb phrase
	 * Use heuristics to decide where it goes
	 * @param modifier
	 */
	@Override
	public void addModifier(Object modifier) {
		// adverb is preModifier
		// string which is one lexicographic word is looked up in lexicon,
		// if it is an adverb than it becomes a preModifier
		// Everything else is postModifier
		
		if (modifier == null)
			return;
		
		// get modifier as NLGElement if possible
		NLGElement modifierElement = null;
		if (modifier instanceof NLGElement)
			modifierElement = (NLGElement) modifier;
		else if (modifier instanceof String) {
			String modifierString = (String)modifier;
			if (modifierString.length() > 0 && !modifierString.contains(" "))
				modifierElement = getFactory().createWord(modifier, LexicalCategory.ANY);
		}
		
		// if no modifier element, must be a complex string
		if (modifierElement == null) {
			addPostModifier((String)modifier);
			return;
		}
		
		// extract WordElement if modifier is a single word
		WordElement modifierWord = null;
		if (modifierElement != null && modifierElement instanceof WordElement)
			modifierWord = (WordElement) modifierElement;
		else if (modifierElement != null && modifierElement instanceof InflectedWordElement)
			modifierWord = ((InflectedWordElement) modifierElement).getBaseWord();
		
		if (modifierWord != null && modifierWord.getCategory() == LexicalCategory.ADVERB) {
			addPreModifier(modifierWord);
			return;
		}
		
		// default case
		addPostModifier(modifierElement);
	}


}
