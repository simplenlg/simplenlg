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
package simplenlg.aggregation;

import java.util.List;

import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseCategory;

/**
 * <P>
 * Implementation of the forward conjunction rule. Given two sentences
 * <code>s1</code> and <code>s2</code>, this rule elides any constituent in the
 * left periphery of <code>s2</code> which is also in <code>s1</code>. The left
 * periphery in simplenlg is roughly defined as the subjects, front modifiers
 * and cue phrase of an {@link simplenlg.phrasespec.SPhraseSpec}.
 * </P>
 * 
 * <P>
 * This rule elides any constituent in the left periphery of <code>s2</code>
 * which is <I>lemma-identical</I> to a constituent with the same function in
 * <code>s1</code>, that is, it is headed by the same lexical item, though
 * possibly with different inflectional features and/or modifiers. Note that
 * this means that <I>the tall man</I> and <I>the man</I> are counted as
 * "identical" for the purposes of this rule. This is only justifiable insofar
 * as the two NPs are co-referential. Since SimpleNLG does not make assumptions
 * about the referentiality (or any part of the semantics) of phrases, it is up
 * to the developer to ensure that this is always the case.
 * </P>
 * 
 * <P>
 * The current implementation is loosely based on the algorithm in Harbusch and
 * Kempen (2009), which is described here:
 * 
 * <a href="http://aclweb.org/anthology-new/W/W09/W09-0624.pdf">
 * http://aclweb.org/anthology-new/W/W09/W09-0624.pdf</a>
 * </P>
 * 
 * <P>
 * <strong>Implementation note:</strong> The current implementation only applies
 * ellipsis to phrasal constituents (i.e. not to their component lexical items).
 * </P>
 * 
 * 
 * <P>
 * <STRONG>Note:</STRONG>: this rule can be used in conjunction with the
 * {@link BackwardConjunctionReductionRule} in {@link Aggregator}.
 * </P>
 * 
 * 
 * @author agatt
 * 
 */
public class ForwardConjunctionReductionRule extends AggregationRule {

	/**
	 * Creates a new <code>ForwardConjunctionReduction</code>.
	 */
	public ForwardConjunctionReductionRule() {
		super();
	}

	/**
	 * Applies forward conjunction reduction to two NLGElements e1 and e2,
	 * succeeding only if they are clauses (that is, e1.getCategory() ==
	 * e2.getCategory == {@link simplenlg.framework.PhraseCategory#CLAUSE}) and
	 * the clauses are not passive.
	 * 
	 * @param previous
	 *            the first phrase
	 * @param next
	 *            the second phrase
	 * @return a coordinate phrase if aggregation is successful,
	 *         <code>null</code> otherwise
	 */
	@Override
	public NLGElement apply(NLGElement previous, NLGElement next) {
		boolean success = false;

		if (previous.getCategory() == PhraseCategory.CLAUSE
				&& next.getCategory() == PhraseCategory.CLAUSE
				&& PhraseChecker.nonePassive(previous, next)) {

			List<PhraseSet> leftPeriphery = PhraseChecker.leftPeriphery(
					previous, next);

			for (PhraseSet pair : leftPeriphery) {

				if (pair.lemmaIdentical()) {
					pair.elideRightmost();
					success = true;
				}
			}
		}

		return success ? this.factory.createCoordinatedPhrase(previous, next)
				: null;
	}

}
