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
package simplenlg.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import simplenlg.features.ClauseStatus;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.phrasespec.NPPhraseSpec;

/**
 * <p>
 * This class defines a phrase. It covers the expected phrase types: noun
 * phrases, verb phrases, adjective phrases, adverb phrases and prepositional
 * phrases as well as also covering clauses. Phrases can be constructed from
 * scratch by setting the correct features of the phrase elements. However, it
 * is strongly recommended that the <code>PhraseFactory</code> is used to
 * construct phrases.
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class PhraseElement extends NLGElement {

	/**
	 * Creates a new phrase of the given type.
	 * 
	 * @param newCategory
	 *            the <code>PhraseCategory</code> type for this phrase.
	 */
	public PhraseElement(PhraseCategory newCategory) {
		setCategory(newCategory);

		// set default feature value
		setFeature(Feature.ELIDED, false);
	}

	/**
	 * <p>
	 * This method retrieves the child components of this phrase. The list
	 * returned will depend on the category of the element.<br>
	 * <ul>
	 * <li>Clauses consist of cue phrases, front modifiers, pre-modifiers,
	 * subjects, verb phrases and complements.</li>
	 * <li>Noun phrases consist of the specifier, pre-modifiers, the noun
	 * subjects, complements and post-modifiers.</li>
	 * <li>Verb phrases consist of pre-modifiers, the verb group, complements
	 * and post-modifiers.</li>
	 * <li>Canned text phrases have no children thus an empty list will be
	 * returned.</li>
	 * <li>All the other phrases consist of pre-modifiers, the main phrase
	 * element, complements and post-modifiers.</li>
	 * </ul>
	 * </p>
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s representing the
	 *         child elements of this phrase.
	 */
	@Override
	public List<NLGElement> getChildren() {
		List<NLGElement> children = new ArrayList<NLGElement>();
		ElementCategory category = getCategory();
		NLGElement currentElement = null;

		if (category instanceof PhraseCategory) {
			switch ((PhraseCategory) category) {
			case CLAUSE:
				currentElement = getFeatureAsElement(Feature.CUE_PHRASE);
				if (currentElement != null) {
					children.add(currentElement);
				}
				children
						.addAll(getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS));
				children
						.addAll(getFeatureAsElementList(InternalFeature.PREMODIFIERS));
				children
						.addAll(getFeatureAsElementList(InternalFeature.SUBJECTS));
				children
						.addAll(getFeatureAsElementList(InternalFeature.VERB_PHRASE));
				children
						.addAll(getFeatureAsElementList(InternalFeature.COMPLEMENTS));
				break;

			case NOUN_PHRASE:
				currentElement = getFeatureAsElement(InternalFeature.SPECIFIER);
				if (currentElement != null) {
					children.add(currentElement);
				}
				children
						.addAll(getFeatureAsElementList(InternalFeature.PREMODIFIERS));
				currentElement = getHead();
				if (currentElement != null) {
					children.add(currentElement);
				}
				children
						.addAll(getFeatureAsElementList(InternalFeature.COMPLEMENTS));
				children
						.addAll(getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
				break;

			case VERB_PHRASE:
				children
						.addAll(getFeatureAsElementList(InternalFeature.PREMODIFIERS));
				currentElement = getHead();
				if (currentElement != null) {
					children.add(currentElement);
				}
				children
						.addAll(getFeatureAsElementList(InternalFeature.COMPLEMENTS));
				children
						.addAll(getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
				break;

			case CANNED_TEXT:
				// Do nothing
				break;

			default:
				children
						.addAll(getFeatureAsElementList(InternalFeature.PREMODIFIERS));
				currentElement = getHead();
				if (currentElement != null) {
					children.add(currentElement);
				}
				children
						.addAll(getFeatureAsElementList(InternalFeature.COMPLEMENTS));
				children
						.addAll(getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
				break;
			}
		}
		return children;
	}

	/**
	 * Sets the head, or main component, of this current phrase. For example,
	 * the head for a verb phrase should be a verb while the head of a noun
	 * phrase should be a noun. <code>String</code>s are converted to
	 * <code>StringElement</code>s. If <code>null</code> is passed in as the new
	 * head then the head feature is removed.
	 * 
	 * @param newHead
	 *            the new value for the head of this phrase.
	 */
	public void setHead(Object newHead) {
		if (newHead == null) {
			removeFeature(InternalFeature.HEAD);
			return;
		}
		NLGElement headElement;
		if (newHead instanceof NLGElement)
			headElement = (NLGElement) newHead;
		else
			headElement = new StringElement(newHead.toString());

		setFeature(InternalFeature.HEAD, headElement);

	}

	/**
	 * Retrieves the current head of this phrase.
	 * 
	 * @return the <code>NLGElement</code> representing the head.
	 */
	public NLGElement getHead() {
		return getFeatureAsElement(InternalFeature.HEAD);
	}

	/**
	 * <p>
	 * Adds a new complement to the phrase element. Complements will be realised
	 * in the syntax after the head element of the phrase. Complements differ
	 * from post-modifiers in that complements are crucial to the understanding
	 * of a phrase whereas post-modifiers are optional.
	 * </p>
	 * 
	 * <p>
	 * If the new complement being added is a <em>clause</em> or a
	 * <code>CoordinatedPhraseElement</code> then its clause status feature is
	 * set to <code>ClauseStatus.SUBORDINATE</code> and it's discourse function
	 * is set to <code>DiscourseFunction.OBJECT</code> by default unless an
	 * existing discourse function exists on the complement.
	 * </p>
	 * 
	 * <p>
	 * Complements can have different functions. For example, the phrase <I>John
	 * gave Mary a flower</I> has two complements, one a direct object and one
	 * indirect. If a complement is not specified for its discourse function,
	 * then this is automatically set to <code>DiscourseFunction.OBJECT</code>.
	 * </p>
	 * 
	 * @param newComplement
	 *            the new complement as an <code>NLGElement</code>.
	 */
	public void addComplement(NLGElement newComplement) {
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		if (complements == null) {
			complements = new ArrayList<NLGElement>();
		}

		// check if the new complement has a discourse function; if not, assume
		// object
		if(!newComplement.hasFeature(InternalFeature.DISCOURSE_FUNCTION)) {
			newComplement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.OBJECT);
		}

		complements.add(newComplement);
		setFeature(InternalFeature.COMPLEMENTS, complements);
		if (newComplement.isA(PhraseCategory.CLAUSE)
				|| newComplement instanceof CoordinatedPhraseElement) {
			newComplement.setFeature(InternalFeature.CLAUSE_STATUS,
					ClauseStatus.SUBORDINATE);

			if (!newComplement.hasFeature(InternalFeature.DISCOURSE_FUNCTION)) {
				newComplement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
						DiscourseFunction.OBJECT);
			}
		}
	}

	/**
	 * <p>
	 * Sets a complement of the phrase element. If a complement already exists
	 * of the same DISCOURSE_FUNCTION, it is removed. This replaces complements
	 * set earlier via {@link #addComplement(NLGElement)}
	 * </p>
	 * 
	 * @param newComplement
	 *            the new complement as an <code>NLGElement</code>.
	 */
	public void setComplement(NLGElement newComplement) {
		DiscourseFunction function = (DiscourseFunction) newComplement
				.getFeature(InternalFeature.DISCOURSE_FUNCTION);
		removeComplements(function);
		addComplement(newComplement);
	}

	/**
	 * remove complements of the specified DiscourseFunction
	 * 
	 * @param function
	 */
	private void removeComplements(DiscourseFunction function) {
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		if (function == null || complements == null)
			return;
		List<NLGElement> complementsToRemove = new ArrayList<NLGElement>();
		for (NLGElement complement : complements)
			if (function == complement
					.getFeature(InternalFeature.DISCOURSE_FUNCTION))
				complementsToRemove.add(complement);

		if (!complementsToRemove.isEmpty()) {
			complements.removeAll(complementsToRemove);
			setFeature(InternalFeature.COMPLEMENTS, complements);
		}

		return;
	}

	/**
	 * <p>
	 * Adds a new complement to the phrase element. Complements will be realised
	 * in the syntax after the head element of the phrase. Complements differ
	 * from post-modifiers in that complements are crucial to the understanding
	 * of a phrase whereas post-modifiers are optional.
	 * </p>
	 * 
	 * @param newComplement
	 *            the new complement as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void addComplement(String newComplement) {
		StringElement newElement = new StringElement(newComplement);
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		if (complements == null) {
			complements = new ArrayList<NLGElement>();
		}
		complements.add(newElement);
		setFeature(InternalFeature.COMPLEMENTS, complements);
	}

	/**
	 * <p>
	 * Sets the complement to the phrase element. This replaces any complements
	 * set earlier.
	 * </p>
	 * 
	 * @param newComplement
	 *            the new complement as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void setComplement(String newComplement) {
		setFeature(InternalFeature.COMPLEMENTS, null);
		addComplement(newComplement);
	}

	/**
	 * Adds a new post-modifier to the phrase element. Post-modifiers will be
	 * realised in the syntax after the complements.
	 * 
	 * @param newPostModifier
	 *            the new post-modifier as an <code>NLGElement</code>.
	 */
	public void addPostModifier(NLGElement newPostModifier) {
		List<NLGElement> postModifiers = getFeatureAsElementList(InternalFeature.POSTMODIFIERS);
		if (postModifiers == null) {
			postModifiers = new ArrayList<NLGElement>();
		}
		newPostModifier.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.POST_MODIFIER);
		postModifiers.add(newPostModifier);
		setFeature(InternalFeature.POSTMODIFIERS, postModifiers);
	}

	/**
	 * Adds a new post-modifier to the phrase element. Post-modifiers will be
	 * realised in the syntax after the complements.
	 * 
	 * @param newPostModifier
	 *            the new post-modifier as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void addPostModifier(String newPostModifier) {
		List<NLGElement> postModifiers = getFeatureAsElementList(InternalFeature.POSTMODIFIERS);
		if (postModifiers == null) {
			postModifiers = new ArrayList<NLGElement>();
		}
		postModifiers.add(new StringElement(newPostModifier));
		setFeature(InternalFeature.POSTMODIFIERS, postModifiers);
	}

	/**
	 * Set the postmodifier for this phrase. This resets all previous
	 * postmodifiers to <code>null</code> and replaces them with the given
	 * string.
	 * 
	 * @param newPostModifier
	 *            the postmodifier
	 */
	public void setPostModifier(String newPostModifier) {
		this.setFeature(InternalFeature.POSTMODIFIERS, null);
		addPostModifier(newPostModifier);
	}

	/**
	 * Set the postmodifier for this phrase. This resets all previous
	 * postmodifiers to <code>null</code> and replaces them with the given
	 * string.
	 * 
	 * @param newPostModifier
	 *            the postmodifier
	 */
	public void setPostModifier(NLGElement newPostModifier) {
		this.setFeature(InternalFeature.POSTMODIFIERS, null);
		addPostModifier(newPostModifier);
	}

	/**
	 * Adds a new front modifier to the phrase element.
	 * 
	 * @param newFrontModifier
	 *            the new front modifier as an <code>NLGElement</code>.
	 */
	public void addFrontModifier(NLGElement newFrontModifier) {
		List<NLGElement> frontModifiers = getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS);
		if (frontModifiers == null) {
			frontModifiers = new ArrayList<NLGElement>();
		}
		frontModifiers.add(newFrontModifier);
		setFeature(InternalFeature.FRONT_MODIFIERS, frontModifiers);
	}

	/**
	 * Adds a new front modifier to the phrase element.
	 * 
	 * @param newFrontModifier
	 *            the new front modifier as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void addFrontModifier(String newFrontModifier) {
		List<NLGElement> frontModifiers = getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS);

		if (frontModifiers == null) {
			frontModifiers = new ArrayList<NLGElement>();
		}

		frontModifiers.add(new StringElement(newFrontModifier));
		setFeature(InternalFeature.FRONT_MODIFIERS, frontModifiers);
	}

	/**
	 * Set the frontmodifier for this phrase. This resets all previous front
	 * modifiers to <code>null</code> and replaces them with the given string.
	 * 
	 * @param newFrontModifier
	 *            the front modifier
	 */
	public void setFrontModifier(String newFrontModifier) {
		this.setFeature(InternalFeature.FRONT_MODIFIERS, null);
		addFrontModifier(newFrontModifier);
	}

	/**
	 * Set the front modifier for this phrase. This resets all previous front
	 * modifiers to <code>null</code> and replaces them with the given string.
	 * 
	 * @param newFrontModifier
	 *            the front modifier
	 */
	public void setFrontModifier(NLGElement newFrontModifier) {
		this.setFeature(InternalFeature.FRONT_MODIFIERS, null);
		addFrontModifier(newFrontModifier);
	}

	/**
	 * Adds a new pre-modifier to the phrase element.
	 * 
	 * @param newPreModifier
	 *            the new pre-modifier as an <code>NLGElement</code>.
	 */
	public void addPreModifier(NLGElement newPreModifier) {
		List<NLGElement> preModifiers = getFeatureAsElementList(InternalFeature.PREMODIFIERS);
		if (preModifiers == null) {
			preModifiers = new ArrayList<NLGElement>();
		}
		preModifiers.add(newPreModifier);
		setFeature(InternalFeature.PREMODIFIERS, preModifiers);
	}

	/**
	 * Adds a new pre-modifier to the phrase element.
	 * 
	 * @param newPreModifier
	 *            the new pre-modifier as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void addPreModifier(String newPreModifier) {
		addPreModifier(new StringElement(newPreModifier));
	}

	/**
	 * Set the premodifier for this phrase. This resets all previous
	 * premodifiers to <code>null</code> and replaces them with the given
	 * string.
	 * 
	 * @param newPreModifier
	 *            the premodifier
	 */
	public void setPreModifier(String newPreModifier) {
		this.setFeature(InternalFeature.PREMODIFIERS, null);
		addPreModifier(newPreModifier);
	}

	/**
	 * Set the premodifier for this phrase. This resets all previous
	 * premodifiers to <code>null</code> and replaces them with the given
	 * string.
	 * 
	 * @param newPreModifier
	 *            the premodifier
	 */
	public void setPreModifier(NLGElement newPreModifier) {
		this.setFeature(InternalFeature.PREMODIFIERS, null);
		addPreModifier(newPreModifier);
	}

	/**
	 * Add a modifier to a phrase Use heuristics to decide where it goes
	 * 
	 * @param modifier
	 */
	public void addModifier(Object modifier) {
		// default addModifier - always make modifier a preModifier
		if (modifier == null)
			return;

		// assume is preModifier, add in appropriate form
		if (modifier instanceof NLGElement)
			addPreModifier((NLGElement) modifier);
		else
			addPreModifier((String) modifier);
		return;
	}

	/**
	 * Retrieves the current list of pre-modifiers for the phrase.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s.
	 */
	public List<NLGElement> getPreModifiers() {
		return getFeatureAsElementList(InternalFeature.PREMODIFIERS);
	}

	/**
	 * Retrieves the current list of post modifiers for the phrase.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s.
	 */
	public List<NLGElement> getPostModifiers() {
		return getFeatureAsElementList(InternalFeature.POSTMODIFIERS);
	}

	/**
	 * Retrieves the current list of frony modifiers for the phrase.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s.
	 */
	public List<NLGElement> getFrontModifiers() {
		return getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS);
	}

	@Override
	public String printTree(String indent) {
		String thisIndent = indent == null ? " |-" : indent + " |-"; //$NON-NLS-1$ //$NON-NLS-2$
		String childIndent = indent == null ? " | " : indent + " | "; //$NON-NLS-1$ //$NON-NLS-2$
		String lastIndent = indent == null ? " \\-" : indent + " \\-"; //$NON-NLS-1$ //$NON-NLS-2$
		String lastChildIndent = indent == null ? "   " : indent + "   "; //$NON-NLS-1$ //$NON-NLS-2$
		StringBuffer print = new StringBuffer();
		print.append("PhraseElement: category=") //$NON-NLS-1$
				.append(getCategory().toString()).append(", features={"); //$NON-NLS-1$

		Map<String, Object> features = getAllFeatures();
		for (String eachFeature : features.keySet()) {
			print.append(eachFeature).append('=').append(
					features.get(eachFeature).toString()).append(' ');
		}
		print.append("}\n"); //$NON-NLS-1$
		List<NLGElement> children = getChildren();
		int length = children.size() - 1;
		int index = 0;

		for (index = 0; index < length; index++) {
			print.append(thisIndent).append(
					children.get(index).printTree(childIndent));
		}
		if (length >= 0) {
			print.append(lastIndent).append(
					children.get(length).printTree(lastChildIndent));
		}
		return print.toString();
	}

	/**
	 * Removes all existing complements on the phrase.
	 */
	public void clearComplements() {
		removeFeature(InternalFeature.COMPLEMENTS);
	}

	/**
	 * Sets the determiner for the phrase. This only has real meaning on noun
	 * phrases and is added here for convenience. Determiners are some times
	 * referred to as specifiers.
	 * 
	 * @param newDeterminer
	 *            the new determiner for the phrase.
	 * @deprecated Use {@link NPPhraseSpec#setSpecifier(Object)} directly
	 */
	@Deprecated
	public void setDeterminer(Object newDeterminer) {
		NLGFactory factory = new NLGFactory();
		NLGElement determinerElement = factory.createWord(newDeterminer,
				LexicalCategory.DETERMINER);

		if (determinerElement != null) {
			determinerElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
					DiscourseFunction.SPECIFIER);
			setFeature(InternalFeature.SPECIFIER, determinerElement);
			determinerElement.setParent(this);
		}
	}
}
