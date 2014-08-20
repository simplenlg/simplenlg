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
import java.util.Arrays;
import java.util.List;

import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.features.NumberAgreement;

/**
 * <p>
 * This class defines coordination between two or more phrases. Coordination
 * involves the linking of phrases together through the use of key words such as
 * <em>and</em> or <em>but</em>.
 * </p>
 * 
 * <p>
 * The class does not perform any ordering on the coordinates and when realised
 * they appear in the same order they were added to the coordination.
 * </p>
 * 
 * <p>
 * As this class appears similar to the <code>PhraseElement</code> class from an
 * API point of view, it could have extended from the <code>PhraseElement</code>
 * class. However, they are fundamentally different in their nature and thus
 * form two distinct classes with similar APIs.
 * </p>
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class CoordinatedPhraseElement extends NLGElement {

	/** Coordinators which make the coordinate plural (eg, "and" but not "or")*/
	@SuppressWarnings("nls")
	private static final List<String> PLURAL_COORDINATORS = Arrays.asList("and");
	
	/**
	 * Creates a blank coordinated phrase ready for new coordinates to be added.
	 * The default conjunction used is <em>and</em>.
	 */
	public CoordinatedPhraseElement() {
		super();
		this.setFeature(Feature.CONJUNCTION, "and"); //$NON-NLS-1$
	}

	/**
	 * Creates a coordinated phrase linking the two phrase together. The default
	 * conjunction used is <em>and</em>.
	 * 
	 * @param coordinate1
	 *            the first coordinate.
	 * @param coordinate2
	 *            the second coordinate.
	 */
	public CoordinatedPhraseElement(Object coordinate1, Object coordinate2) {

		this.addCoordinate(coordinate1);
		this.addCoordinate(coordinate2);
		this.setFeature(Feature.CONJUNCTION, "and"); //$NON-NLS-1$
	}

	/**
	 * Adds a new coordinate to this coordination. If the new coordinate is a
	 * <code>NLGElement</code> then it is added directly to the coordination. If
	 * the new coordinate is a <code>String</code> a <code>StringElement</code>
	 * is created and added to the coordination. <code>StringElement</code>s
	 * will have their complementisers suppressed by default. In the case of
	 * clauses, complementisers will be suppressed if the clause is not the
	 * first element in the coordination.
	 * 
	 * @param newCoordinate
	 *            the new coordinate to be added.
	 */
	public void addCoordinate(Object newCoordinate) {
		List<NLGElement> coordinates = getFeatureAsElementList(InternalFeature.COORDINATES);
		if (coordinates == null) {
			coordinates = new ArrayList<NLGElement>();
			setFeature(InternalFeature.COORDINATES, coordinates);
		} else if (coordinates.size() == 0) {
			setFeature(InternalFeature.COORDINATES, coordinates);
		}
		if (newCoordinate instanceof NLGElement) {
			if (((NLGElement) newCoordinate).isA(PhraseCategory.CLAUSE)
					&& coordinates.size() > 0) {

				((NLGElement) newCoordinate).setFeature(
						Feature.SUPRESSED_COMPLEMENTISER, true);
			}
			coordinates.add((NLGElement) newCoordinate);
		} else if (newCoordinate instanceof String) {
			NLGElement coordElement = new StringElement((String) newCoordinate);
			coordElement.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
			coordinates.add(coordElement);
		}
		setFeature(InternalFeature.COORDINATES, coordinates);
	}

	@Override
	public List<NLGElement> getChildren() {
		return this.getFeatureAsElementList(InternalFeature.COORDINATES);
	}

	/**
	 * Clears the existing coordinates in this coordination. It performs exactly
	 * the same as <code>removeFeature(Feature.COORDINATES)</code>.
	 */
	public void clearCoordinates() {
		removeFeature(InternalFeature.COORDINATES);
	}

	/**
	 * Adds a new pre-modifier to the phrase element. Pre-modifiers will be
	 * realised in the syntax before the coordinates.
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
	 * Adds a new pre-modifier to the phrase element. Pre-modifiers will be
	 * realised in the syntax before the coordinates.
	 * 
	 * @param newPreModifier
	 *            the new pre-modifier as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void addPreModifier(String newPreModifier) {
		List<NLGElement> preModifiers = getFeatureAsElementList(InternalFeature.PREMODIFIERS);
		if (preModifiers == null) {
			preModifiers = new ArrayList<NLGElement>();
		}
		preModifiers.add(new StringElement(newPreModifier));
		setFeature(InternalFeature.PREMODIFIERS, preModifiers);
	}

	/**
	 * Retrieves the list of pre-modifiers currently associated with this
	 * coordination.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s.
	 */
	public List<NLGElement> getPreModifiers() {
		return getFeatureAsElementList(InternalFeature.PREMODIFIERS);
	}

	/**
	 * Retrieves the list of complements currently associated with this
	 * coordination.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s.
	 */
	public List<NLGElement> getComplements() {
		return getFeatureAsElementList(InternalFeature.COMPLEMENTS);
	}

	/**
	 * Adds a new post-modifier to the phrase element. Post-modifiers will be
	 * realised in the syntax after the coordinates.
	 * 
	 * @param newPostModifier
	 *            the new post-modifier as an <code>NLGElement</code>.
	 */
	public void addPostModifier(NLGElement newPostModifier) {
		List<NLGElement> postModifiers = getFeatureAsElementList(InternalFeature.POSTMODIFIERS);
		if (postModifiers == null) {
			postModifiers = new ArrayList<NLGElement>();
		}
		postModifiers.add(newPostModifier);
		setFeature(InternalFeature.POSTMODIFIERS, postModifiers);
	}

	/**
	 * Adds a new post-modifier to the phrase element. Post-modifiers will be
	 * realised in the syntax after the coordinates.
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
	 * Retrieves the list of post-modifiers currently associated with this
	 * coordination.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s.
	 */
	public List<NLGElement> getPostModifiers() {
		return getFeatureAsElementList(InternalFeature.POSTMODIFIERS);
	}

	@Override
	public String printTree(String indent) {
		String thisIndent = indent == null ? " |-" : indent + " |-"; //$NON-NLS-1$ //$NON-NLS-2$
		String childIndent = indent == null ? " | " : indent + " | "; //$NON-NLS-1$ //$NON-NLS-2$
		String lastIndent = indent == null ? " \\-" : indent + " \\-"; //$NON-NLS-1$ //$NON-NLS-2$
		String lastChildIndent = indent == null ? "   " : indent + "   "; //$NON-NLS-1$ //$NON-NLS-2$
		StringBuffer print = new StringBuffer();
		print.append("CoordinatedPhraseElement:\n"); //$NON-NLS-1$

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
	 * Adds a new complement to the phrase element. Complements will be realised
	 * in the syntax after the coordinates. Complements differ from
	 * post-modifiers in that complements are crucial to the understanding of a
	 * phrase whereas post-modifiers are optional.
	 * 
	 * @param newComplement
	 *            the new complement as an <code>NLGElement</code>.
	 */
	public void addComplement(NLGElement newComplement) {
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		if (complements == null) {
			complements = new ArrayList<NLGElement>();
		}
		complements.add(newComplement);
		setFeature(InternalFeature.COMPLEMENTS, complements);
	}

	/**
	 * Adds a new complement to the phrase element. Complements will be realised
	 * in the syntax after the coordinates. Complements differ from
	 * post-modifiers in that complements are crucial to the understanding of a
	 * phrase whereas post-modifiers are optional.
	 * 
	 * @param newComplement
	 *            the new complement as a <code>String</code>. It is used to
	 *            create a <code>StringElement</code>.
	 */
	public void addComplement(String newComplement) {
		List<NLGElement> complements = getFeatureAsElementList(InternalFeature.COMPLEMENTS);
		if (complements == null) {
			complements = new ArrayList<NLGElement>();
		}
		complements.add(new StringElement(newComplement));
		setFeature(InternalFeature.COMPLEMENTS, complements);
	}

	/**
	 * A convenience method for retrieving the last coordinate in this
	 * coordination.
	 * 
	 * @return the last coordinate as represented by a <code>NLGElement</code>
	 */
	public NLGElement getLastCoordinate() {
		List<NLGElement> children = getChildren();
		return children != null && children.size() > 0 ? children.get(children
				.size() - 1) : null;
	}
	
	/** set the conjunction to be used in a coordinatedphraseelement
	 * @param conjunction
	 */
	public void setConjunction(String conjunction) {
		setFeature(Feature.CONJUNCTION, conjunction);
	}
	
	/**
	 * @return  conjunction used in coordinatedPhraseElement
	 */
	public String getConjunction() {
		return getFeatureAsString(Feature.CONJUNCTION);
	}
	
	/**
	 * @return true if this coordinate is plural in a syntactic sense
	 */
	public boolean checkIfPlural() {
		// doing this right is quite complex, take simple approach for now
		int size = getChildren().size();
		if (size == 1)
			return (NumberAgreement.PLURAL.equals(getLastCoordinate().getFeature(Feature.NUMBER)));
		else
			return PLURAL_COORDINATORS.contains(getConjunction());
	}
}
