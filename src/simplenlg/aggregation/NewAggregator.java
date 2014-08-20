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
import simplenlg.framework.NLGFactory;
import simplenlg.framework.NLGModule;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.syntax.english.SyntaxProcessor;

public class NewAggregator extends NLGModule {
	private SyntaxProcessor _syntax;
	private NLGFactory _factory;

	public NewAggregator() {

	}

	public void initialise() {
		this._syntax = new SyntaxProcessor();
		this._factory = new NLGFactory();
	}

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NLGElement realise(NLGElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	public NLGElement realise(NLGElement phrase1, NLGElement phrase2) {
		NLGElement result = null;

		if (phrase1 instanceof PhraseElement
				&& phrase2 instanceof PhraseElement
				&& phrase1.getCategory() == PhraseCategory.CLAUSE
				&& phrase2.getCategory() == PhraseCategory.CLAUSE) {

			List<FunctionalSet> funcSets = AggregationHelper
					.collectFunctionalPairs(this._syntax.realise(phrase1),this._syntax.realise(phrase2));

			applyForwardConjunctionReduction(funcSets);
			applyBackwardConjunctionReduction(funcSets);
			result = this._factory.createCoordinatedPhrase(phrase1, phrase2);
		}

		return result;
	}

	// private void applyGapping(List<FunctionalSet> funcPairs) {
	//
	// }

	private void applyForwardConjunctionReduction(List<FunctionalSet> funcSets) {

		for (FunctionalSet pair : funcSets) {
			if (pair.getPeriphery() == Periphery.LEFT && pair.formIdentical()) {
				pair.elideRightMost();
			}
		}

	}

	private void applyBackwardConjunctionReduction(List<FunctionalSet> funcSets) {
		for (FunctionalSet pair : funcSets) {
			if (pair.getPeriphery() == Periphery.RIGHT && pair.formIdentical()) {
				pair.elideLeftMost();
			}
		}
	}

}
