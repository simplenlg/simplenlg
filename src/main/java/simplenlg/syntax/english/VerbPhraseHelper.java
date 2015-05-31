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

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * <p>
 * This class contains static methods to help the syntax processor realise verb
 * phrases. It adds auxiliary verbs into the element tree as required.
 * </p>
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
abstract class VerbPhraseHelper {

	/**
	 * The main method for realising verb phrases.
	 * 
	 * @param parent
	 *            the <code>SyntaxProcessor</code> that called this method.
	 * @param phrase
	 *            the <code>PhraseElement</code> to be realised.
	 * @return the realised <code>NLGElement</code>.
	 */
	static NLGElement realise(SyntaxProcessor parent, PhraseElement phrase) {
		ListElement realisedElement = null;
		Stack<NLGElement> vgComponents = null;
		Stack<NLGElement> mainVerbRealisation = new Stack<NLGElement>();
		Stack<NLGElement> auxiliaryRealisation = new Stack<NLGElement>();

		if (phrase != null) {
			vgComponents = createVerbGroup(parent, phrase);
			splitVerbGroup(vgComponents, mainVerbRealisation,
					auxiliaryRealisation);

			realisedElement = new ListElement();

			if (!phrase.hasFeature(InternalFeature.REALISE_AUXILIARY)
					|| phrase.getFeatureAsBoolean(
							InternalFeature.REALISE_AUXILIARY).booleanValue()) {

				realiseAuxiliaries(parent, realisedElement,
						auxiliaryRealisation);

				PhraseHelper.realiseList(parent, realisedElement, phrase
						.getPreModifiers(), DiscourseFunction.PRE_MODIFIER);

				realiseMainVerb(parent, phrase, mainVerbRealisation,
						realisedElement);

			} else if (isCopular(phrase.getHead())) {
				realiseMainVerb(parent, phrase, mainVerbRealisation,
						realisedElement);
				PhraseHelper.realiseList(parent, realisedElement, phrase
						.getPreModifiers(), DiscourseFunction.PRE_MODIFIER);

			} else {
				PhraseHelper.realiseList(parent, realisedElement, phrase
						.getPreModifiers(), DiscourseFunction.PRE_MODIFIER);
				realiseMainVerb(parent, phrase, mainVerbRealisation,
						realisedElement);
			}
			realiseComplements(parent, phrase, realisedElement);
			PhraseHelper.realiseList(parent, realisedElement, phrase
					.getPostModifiers(), DiscourseFunction.POST_MODIFIER);
		}

		return realisedElement;
	}

	/**
	 * Realises the auxiliary verbs in the verb group.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the noun phrase.
	 * @param auxiliaryRealisation
	 *            the stack of auxiliary verbs.
	 */
	private static void realiseAuxiliaries(SyntaxProcessor parent,
			ListElement realisedElement, Stack<NLGElement> auxiliaryRealisation) {

		NLGElement aux = null;
		NLGElement currentElement = null;
		while (!auxiliaryRealisation.isEmpty()) {
			aux = auxiliaryRealisation.pop();
			currentElement = parent.realise(aux);
			if (currentElement != null) {
				realisedElement.addComponent(currentElement);
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
						DiscourseFunction.AUXILIARY);
			}
		}
	}

	/**
	 * Realises the main group of verbs in the phrase.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param mainVerbRealisation
	 *            the stack of the main verbs in the phrase.
	 * @param realisedElement
	 *            the current realisation of the noun phrase.
	 */
	private static void realiseMainVerb(SyntaxProcessor parent,
			PhraseElement phrase, Stack<NLGElement> mainVerbRealisation,
			ListElement realisedElement) {

		NLGElement currentElement = null;
		NLGElement main = null;

		while (!mainVerbRealisation.isEmpty()) {
			main = mainVerbRealisation.pop();
			main.setFeature(Feature.INTERROGATIVE_TYPE, phrase
					.getFeature(Feature.INTERROGATIVE_TYPE));
			currentElement = parent.realise(main);

			if (currentElement != null) {
				realisedElement.addComponent(currentElement);
			}
		}
	}

	/**
	 * Realises the complements of this phrase.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param realisedElement
	 *            the current realisation of the noun phrase.
	 */
	private static void realiseComplements(SyntaxProcessor parent,
			PhraseElement phrase, ListElement realisedElement) {

		ListElement indirects = new ListElement();
		ListElement directs = new ListElement();
		ListElement unknowns = new ListElement();
		Object discourseValue = null;
		NLGElement currentElement = null;

		for (NLGElement complement : phrase
				.getFeatureAsElementList(InternalFeature.COMPLEMENTS)) {

			discourseValue = complement
					.getFeature(InternalFeature.DISCOURSE_FUNCTION);
			currentElement = parent.realise(complement);
			if (currentElement != null) {
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
						DiscourseFunction.COMPLEMENT);

				if (DiscourseFunction.INDIRECT_OBJECT.equals(discourseValue)) {
					indirects.addComponent(currentElement);
				} else if (DiscourseFunction.OBJECT.equals(discourseValue)) {
					directs.addComponent(currentElement);
				} else {
					unknowns.addComponent(currentElement);
				}
			}
		}
		if (!InterrogativeType.isIndirectObject(phrase
				.getFeature(Feature.INTERROGATIVE_TYPE))) {
			realisedElement.addComponents(indirects.getChildren());
		}
		if (!phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			if (!InterrogativeType.isObject(phrase
					.getFeature(Feature.INTERROGATIVE_TYPE))) {
				realisedElement.addComponents(directs.getChildren());
			}
			realisedElement.addComponents(unknowns.getChildren());
		}
	}

	/**
	 * Splits the stack of verb components into two sections. One being the verb
	 * associated with the main verb group, the other being associated with the
	 * auxiliary verb group.
	 * 
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param mainVerbRealisation
	 *            the main group of verbs.
	 * @param auxiliaryRealisation
	 *            the auxiliary group of verbs.
	 */
	private static void splitVerbGroup(Stack<NLGElement> vgComponents,
			Stack<NLGElement> mainVerbRealisation,
			Stack<NLGElement> auxiliaryRealisation) {

		boolean mainVerbSeen = false;

		for (NLGElement word : vgComponents) {
			if (!mainVerbSeen) {
				mainVerbRealisation.push(word);
				if (!word.equals("not")) { //$NON-NLS-1$
					mainVerbSeen = true;
				}
			} else {
				auxiliaryRealisation.push(word);
			}
		}

	}

	/**
	 * Creates a stack of verbs for the verb phrase. Additional auxiliary verbs
	 * are added as required based on the features of the verb phrase.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @return the verb group as a <code>Stack</code> of <code>NLGElement</code>
	 *         s.
	 */
	static final private Stack<NLGElement> createVerbGroup(
			SyntaxProcessor parent, PhraseElement phrase) {

		String actualModal = null;
		Object formValue = phrase.getFeature(Feature.FORM);
		Tense tenseValue = (Tense) phrase.getFeature(Feature.TENSE);
		String modal = phrase.getFeatureAsString(Feature.MODAL);
		boolean modalPast = false;
		Stack<NLGElement> vgComponents = new Stack<NLGElement>();
		boolean interrogative = phrase.hasFeature(Feature.INTERROGATIVE_TYPE);

		if (Form.GERUND.equals(formValue) || Form.INFINITIVE.equals(formValue)) {
			tenseValue = Tense.PRESENT;
		}

		if (Form.INFINITIVE.equals(formValue)) {
			actualModal = "to"; //$NON-NLS-1$

		} else if (formValue == null || Form.NORMAL.equals(formValue)) {
			if (Tense.FUTURE.equals(tenseValue)
					&& modal == null
					&& ((!(phrase.getHead() instanceof CoordinatedPhraseElement)) || (phrase
							.getHead() instanceof CoordinatedPhraseElement && interrogative))) {

				actualModal = "will"; //$NON-NLS-1$

			} else if (modal != null) {
				actualModal = modal;

				if (Tense.PAST.equals(tenseValue)) {
					modalPast = true;
				}
			}
		}

		pushParticles(phrase, parent, vgComponents);
		NLGElement frontVG = grabHeadVerb(phrase, tenseValue, modal != null);
		checkImperativeInfinitive(formValue, frontVG);

		if (phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			frontVG = addBe(frontVG, vgComponents, Form.PAST_PARTICIPLE);
		}

		if (phrase.getFeatureAsBoolean(Feature.PROGRESSIVE).booleanValue()) {
			frontVG = addBe(frontVG, vgComponents, Form.PRESENT_PARTICIPLE);
		}

		if (phrase.getFeatureAsBoolean(Feature.PERFECT).booleanValue()
				|| modalPast) {
			frontVG = addHave(frontVG, vgComponents, modal, tenseValue);
		}

		frontVG = pushIfModal(actualModal != null, phrase, frontVG,
				vgComponents);
		frontVG = createNot(phrase, vgComponents, frontVG, modal != null);

		if (frontVG != null) {
			pushFrontVerb(phrase, vgComponents, frontVG, formValue,
					interrogative);
		}

		pushModal(actualModal, phrase, vgComponents);
		return vgComponents;
	}

	/**
	 * Pushes the modal onto the stack of verb components.
	 * 
	 * @param actualModal
	 *            the modal to be used.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 */
	private static void pushModal(String actualModal, PhraseElement phrase,
			Stack<NLGElement> vgComponents) {
		if (actualModal != null
				&& !phrase.getFeatureAsBoolean(InternalFeature.IGNORE_MODAL)
						.booleanValue()) {
			vgComponents.push(new InflectedWordElement(actualModal,
					LexicalCategory.MODAL));
		}
	}

	/**
	 * Pushes the front verb onto the stack of verb components.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param formValue
	 *            the <code>Form</code> of the phrase.
	 * @param interrogative
	 *            <code>true</code> if the phrase is interrogative.
	 */
	private static void pushFrontVerb(PhraseElement phrase,
			Stack<NLGElement> vgComponents, NLGElement frontVG,
			Object formValue, boolean interrogative) {
		Object interrogType = phrase.getFeature(Feature.INTERROGATIVE_TYPE);
		
		if (Form.GERUND.equals(formValue)) {
			frontVG.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
			vgComponents.push(frontVG);

		} else if (Form.PAST_PARTICIPLE.equals(formValue)) {
			frontVG.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
			vgComponents.push(frontVG);

		} else if (Form.PRESENT_PARTICIPLE.equals(formValue)) {
			frontVG.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
			vgComponents.push(frontVG);

		} else if ((!(formValue == null || Form.NORMAL.equals(formValue)) || interrogative)
				&& !isCopular(phrase.getHead()) && vgComponents.isEmpty()) {

			// AG: fix below: if interrogative, only set non-morph feature in
			// case it's not WHO_SUBJECT OR WHAT_SUBJECT			
			if (!(InterrogativeType.WHO_SUBJECT.equals(interrogType) || InterrogativeType.WHAT_SUBJECT
					.equals(interrogType))) {
				frontVG.setFeature(InternalFeature.NON_MORPH, true);
			}

			vgComponents.push(frontVG);

		} else {
			NumberAgreement numToUse = determineNumber(phrase.getParent(),
					phrase);
			frontVG.setFeature(Feature.TENSE, phrase.getFeature(Feature.TENSE));
			frontVG.setFeature(Feature.PERSON, phrase
					.getFeature(Feature.PERSON));
			frontVG.setFeature(Feature.NUMBER, numToUse);
			
			//don't push the front VG if it's a negated interrogative WH object question
			if (!(phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue() && (InterrogativeType.WHO_OBJECT
					.equals(interrogType) || InterrogativeType.WHAT_OBJECT
					.equals(interrogType)))) {
				vgComponents.push(frontVG);
			}
		}
	}

	/**
	 * Adds <em>not</em> to the stack if the phrase is negated.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param hasModal
	 *            the phrase has a modal
	 * @return the new element for the front of the group.
	 */
	private static NLGElement createNot(PhraseElement phrase,
			Stack<NLGElement> vgComponents, NLGElement frontVG, boolean hasModal) {
		NLGElement newFront = frontVG;

		if (phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue()) {
			NLGFactory factory = phrase.getFactory();

			// before adding "do", check if this is an object WH
			// interrogative
			// in which case, don't add anything as it's already done by
			// ClauseHelper
			Object interrType = phrase.getFeature(Feature.INTERROGATIVE_TYPE);
			boolean addDo = !(InterrogativeType.WHAT_OBJECT.equals(interrType) || InterrogativeType.WHO_OBJECT
					.equals(interrType));

			if (!vgComponents.empty() || frontVG != null && isCopular(frontVG)) {
				vgComponents.push(new InflectedWordElement(
						"not", LexicalCategory.ADVERB)); //$NON-NLS-1$
			} else {
				if (frontVG != null && !hasModal) {
					frontVG.setFeature(Feature.NEGATED, true);
					vgComponents.push(frontVG);
				}

				vgComponents.push(new InflectedWordElement(
						"not", LexicalCategory.ADVERB)); //$NON-NLS-1$

				if (addDo) {
					if (factory != null) {
						newFront = factory.createInflectedWord("do",
								LexicalCategory.VERB);

					} else {
						newFront = new InflectedWordElement(
								"do", LexicalCategory.VERB); //$NON-NLS-1$
					}
				}
			}
		}

		return newFront;
	}

	/**
	 * Pushes the front verb on to the stack if the phrase has a modal.
	 * 
	 * @param hasModal
	 *            the phrase has a modal
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @return the new element for the front of the group.
	 */
	private static NLGElement pushIfModal(boolean hasModal,
			PhraseElement phrase, NLGElement frontVG,
			Stack<NLGElement> vgComponents) {

		NLGElement newFront = frontVG;
		if (hasModal
				&& !phrase.getFeatureAsBoolean(InternalFeature.IGNORE_MODAL)
						.booleanValue()) {
			if (frontVG != null) {
				frontVG.setFeature(InternalFeature.NON_MORPH, true);
				vgComponents.push(frontVG);
			}
			newFront = null;
		}
		return newFront;
	}

	/**
	 * Adds <em>have</em> to the stack.
	 * 
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param modal
	 *            the modal to be used.
	 * @param tenseValue
	 *            the <code>Tense</code> of the phrase.
	 * @return the new element for the front of the group.
	 */
	private static NLGElement addHave(NLGElement frontVG,
			Stack<NLGElement> vgComponents, String modal, Tense tenseValue) {
		NLGElement newFront = frontVG;

		if (frontVG != null) {
			frontVG.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
			vgComponents.push(frontVG);
		}
		newFront = new InflectedWordElement("have", LexicalCategory.VERB); //$NON-NLS-1$
		newFront.setFeature(Feature.TENSE, tenseValue);
		if (modal != null) {
			newFront.setFeature(InternalFeature.NON_MORPH, true);
		}
		return newFront;
	}

	/**
	 * Adds the <em>be</em> verb to the front of the group.
	 * 
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param frontForm
	 *            the form the current front verb is to take.
	 * @return the new element for the front of the group.
	 */
	private static NLGElement addBe(NLGElement frontVG,
			Stack<NLGElement> vgComponents, Form frontForm) {

		if (frontVG != null) {
			frontVG.setFeature(Feature.FORM, frontForm);
			vgComponents.push(frontVG);
		}
		return new InflectedWordElement("be", LexicalCategory.VERB); //$NON-NLS-1$
	}

	/**
	 * Checks to see if the phrase is in imperative, infinitive or bare
	 * infinitive form. If it is then no morphology is done on the main verb.
	 * 
	 * @param formValue
	 *            the <code>Form</code> of the phrase.
	 * @param frontVG
	 *            the first verb in the verb group.
	 */
	private static void checkImperativeInfinitive(Object formValue,
			NLGElement frontVG) {

		if ((Form.IMPERATIVE.equals(formValue)
				|| Form.INFINITIVE.equals(formValue) || Form.BARE_INFINITIVE
				.equals(formValue))
				&& frontVG != null) {
			frontVG.setFeature(InternalFeature.NON_MORPH, true);
		}
	}

	/**
	 * Grabs the head verb of the verb phrase and sets it to future tense if the
	 * phrase is future tense. It also turns off negation if the group has a
	 * modal.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param tenseValue
	 *            the <code>Tense</code> of the phrase.
	 * @param hasModal
	 *            <code>true</code> if the verb phrase has a modal.
	 * @return the modified head element
	 */
	private static NLGElement grabHeadVerb(PhraseElement phrase,
			Tense tenseValue, boolean hasModal) {
		NLGElement frontVG = phrase.getHead();

		if (frontVG != null) {
			if (frontVG instanceof WordElement) {
				frontVG = new InflectedWordElement((WordElement) frontVG);
			}

			// AG: tense value should always be set on frontVG
			if (tenseValue != null) {
				frontVG.setFeature(Feature.TENSE, tenseValue);
			}

			// if (Tense.FUTURE.equals(tenseValue) && frontVG != null) {
			// frontVG.setFeature(Feature.TENSE, Tense.FUTURE);
			// }

			if (hasModal) {
				frontVG.setFeature(Feature.NEGATED, false);
			}
		}

		return frontVG;
	}

	/**
	 * Pushes the particles of the main verb onto the verb group stack.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 */
	private static void pushParticles(PhraseElement phrase,
			SyntaxProcessor parent, Stack<NLGElement> vgComponents) {
		Object particle = phrase.getFeature(Feature.PARTICLE);

		if (particle instanceof String) {
			vgComponents.push(new StringElement((String) particle));

		} else if (particle instanceof NLGElement) {
			vgComponents.push(parent.realise((NLGElement) particle));
		}
	}

	/**
	 * Determines the number agreement for the phrase ensuring that any number
	 * agreement on the parent element is inherited by the phrase.
	 * 
	 * @param parent
	 *            the parent element of the phrase.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @return the <code>NumberAgreement</code> to be used for the phrase.
	 */
	private static NumberAgreement determineNumber(NLGElement parent,
			PhraseElement phrase) {
		Object numberValue = phrase.getFeature(Feature.NUMBER);
		NumberAgreement number = null;
		if (numberValue != null && numberValue instanceof NumberAgreement) {
			number = (NumberAgreement) numberValue;
		} else {
			number = NumberAgreement.SINGULAR;
		}

		// Ehud Reiter = modified below to force number from VP for WHAT_SUBJECT
		// and WHO_SUBJECT interrogatuves
		if (parent instanceof PhraseElement) {
			if (parent.isA(PhraseCategory.CLAUSE)
					&& (PhraseHelper.isExpletiveSubject((PhraseElement) parent)
							|| InterrogativeType.WHO_SUBJECT.equals(parent
									.getFeature(Feature.INTERROGATIVE_TYPE)) || InterrogativeType.WHAT_SUBJECT
							.equals(parent
									.getFeature(Feature.INTERROGATIVE_TYPE)))
					&& isCopular(phrase.getHead())) {

				if (hasPluralComplement(phrase
						.getFeatureAsElementList(InternalFeature.COMPLEMENTS))) {
					number = NumberAgreement.PLURAL;
				} else {
					number = NumberAgreement.SINGULAR;
				}
			}
		}
		return number;
	}

	/**
	 * Checks to see if any of the complements to the phrase are plural.
	 * 
	 * @param complements
	 *            the list of complements of the phrase.
	 * @return <code>true</code> if any of the complements are plural.
	 */
	private static boolean hasPluralComplement(List<NLGElement> complements) {
		boolean plural = false;
		Iterator<NLGElement> complementIterator = complements.iterator();
		NLGElement eachComplement = null;
		Object numberValue = null;

		while (complementIterator.hasNext() && !plural) {
			eachComplement = complementIterator.next();

			if (eachComplement != null
					&& eachComplement.isA(PhraseCategory.NOUN_PHRASE)) {

				numberValue = eachComplement.getFeature(Feature.NUMBER);
				if (numberValue != null
						&& NumberAgreement.PLURAL.equals(numberValue)) {
					plural = true;
				}
			}
		}
		return plural;
	}

	/**
	 * Checks to see if the base form of the word is copular, i.e. <em>be</em>.
	 * 
	 * @param element
	 *            the element to be checked
	 * @return <code>true</code> if the element is copular.
	 */
	public static boolean isCopular(NLGElement element) {
		boolean copular = false;

		if (element instanceof InflectedWordElement) {
			copular = "be".equalsIgnoreCase(((InflectedWordElement) element) //$NON-NLS-1$
					.getBaseForm());

		} else if (element instanceof WordElement) {
			copular = "be".equalsIgnoreCase(((WordElement) element) //$NON-NLS-1$
					.getBaseForm());

		} else if (element instanceof PhraseElement) {
			// get the head and check if it's "be"
			NLGElement head = element instanceof SPhraseSpec ? ((SPhraseSpec) element)
					.getVerb()
					: ((PhraseElement) element).getHead();

			if (head != null) {
				copular = (head instanceof WordElement && "be"
						.equals(((WordElement) head).getBaseForm()));
			}
		}

		return copular;
	}
}
