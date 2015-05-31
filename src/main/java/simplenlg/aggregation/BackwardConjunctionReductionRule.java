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
 * Implementation of the backward conjunction reduction rule. Given two
 * sentences <code>s1</code> and <code>s2</code>, this rule elides any
 * constituent in the right periphery of <code>s1</code> which is
 * <I>form-identical</I> to a constituent with the same function in
 * <code>s2</code>, that is, the two constituents are essentially identical in
 * their final, realised, form.
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
 * *
 * <P>
 * <STRONG>Note:</STRONG>: this rule can be used in conjunction with the
 * {@link ForwardConjunctionReductionRule} in {@link Aggregator}.
 * </P>
 * 
 * @author Albert Gatt, University of Malta and University of Aberdeen
 * 
 */
public class BackwardConjunctionReductionRule extends AggregationRule {

	//private SyntaxProcessor _syntaxProcessor;

	/**
	 * Creates a new <code>BackwardConjunctionReduction</code>.
	 */
	public BackwardConjunctionReductionRule() {
		super();
		//this._syntaxProcessor = new SyntaxProcessor();
	}

	/**
	 * Applies backward conjunction reduction to two NLGElements e1 and e2,
	 * succeeding only if they are clauses (that is, e1.getCategory() ==
	 * e2.getCategory == {@link simplenlg.framework.PhraseCategory#CLAUSE}).
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
			
			List<PhraseSet> rightPeriphery = PhraseChecker.rightPeriphery(
					previous, next);

			for (PhraseSet pair : rightPeriphery) {
				if (pair.lemmaIdentical()) {
					pair.elideLeftmost();
					success = true;
				}
			}
		}

		return success ? this.factory.createCoordinatedPhrase(previous, next)
				: null;
	}

}
