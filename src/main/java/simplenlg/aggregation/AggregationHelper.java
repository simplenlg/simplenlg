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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.InternalFeature;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseCategory;

public class AggregationHelper {

	public static List<DiscourseFunction> FUNCTIONS = Arrays.asList(
			DiscourseFunction.SUBJECT, DiscourseFunction.HEAD,
			DiscourseFunction.COMPLEMENT, DiscourseFunction.PRE_MODIFIER,
			DiscourseFunction.POST_MODIFIER, DiscourseFunction.VERB_PHRASE);

	public static List<DiscourseFunction> RECURSIVE = Arrays
			.asList(DiscourseFunction.VERB_PHRASE);

	public static List<FunctionalSet> collectFunctionalPairs(
			NLGElement phrase1, NLGElement phrase2) {
		List<NLGElement> children1 = getAllChildren(phrase1);
		List<NLGElement> children2 = getAllChildren(phrase2);
		List<FunctionalSet> pairs = new ArrayList<FunctionalSet>();

		if (children1.size() == children2.size()) {
			Periphery periph = Periphery.LEFT;

			for (int i = 0; i < children1.size(); i++) {
				NLGElement child1 = children1.get(i);
				NLGElement child2 = children2.get(i);
				ElementCategory cat1 = child1.getCategory();
				ElementCategory cat2 = child2.getCategory();
				DiscourseFunction func1 = (DiscourseFunction) child1
						.getFeature(InternalFeature.DISCOURSE_FUNCTION);
				DiscourseFunction func2 = (DiscourseFunction) child2
						.getFeature(InternalFeature.DISCOURSE_FUNCTION);

				if (cat1 == cat2 && func1 == func2) {
					pairs.add(FunctionalSet.newInstance(func1, cat1, periph,
							child1, child2));

					if (cat1 == LexicalCategory.VERB) {
						periph = Periphery.RIGHT;
					}

				} else {
					pairs.clear();
					break;
				}
			}
		}

		return pairs;
	}

	private static List<NLGElement> getAllChildren(NLGElement element) {
		List<NLGElement> children = new ArrayList<NLGElement>();
		List<NLGElement> components = element instanceof ListElement ? element
				.getFeatureAsElementList(InternalFeature.COMPONENTS) : element
				.getChildren();

		for (NLGElement child : components) {
			children.add(child);

			if (child.getCategory() == PhraseCategory.VERB_PHRASE
					|| child.getFeature(InternalFeature.DISCOURSE_FUNCTION) == DiscourseFunction.VERB_PHRASE) {
				children.addAll(getAllChildren(child));
			}
		}

		return children;
	}

}
