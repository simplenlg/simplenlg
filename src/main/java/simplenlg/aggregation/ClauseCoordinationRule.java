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

import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseCategory;

/**
 * Implementation of a clausal coordination rule. The rule performs the
 * following operations on sentences:
 * 
 * <OL>
 * <LI>If the sentences have the same subject, a new sentence is returned with
 * that subject, and the VP from the component sentences conjoined. For example
 * <I>John kicked the ball.</I> and <I>John sang a song.</I> becomes <I>John
 * kicked the ball and sang a song</I>.</LI>
 * <LI>If the sentences have the same VP, a new sentence is returned with that
 * VP, and the subjects from the component sentences conjoined. For example
 * <I>John kicked the ball.</I> and <I>Mary kicked the ball.</I> become <I>John
 * and Mary kicked the ball</I>.</LI>
 * </OL>
 * 
 * <P>
 * These operations only apply to sentences whose front modifiers are identical,
 * that is, sentences where, for every pair <code>s1</code> and <code>s2</code>,
 * <code>s1.getFrontModifiers().equals(s2.getFrontModifiers())</code>.
 * 
 * <P>
 * <STRONG>Note:</STRONG>: it is not recommended to use this rule in addition to
 * {@link BackwardConjunctionReductionRule} and/or
 * {@link ForwardConjunctionReductionRule}.
 * 
 * @author Albert Gatt, University of Malta & University of Aberdeen
 */
public class ClauseCoordinationRule extends AggregationRule {

	/**
	 * Constructs an instance of the ClauseCoordinationRule
	 */
	public ClauseCoordinationRule() {
		super();
	}

	/**
	 * Applies aggregation to two NLGElements e1 and e2, succeeding only if they
	 * are clauses (that is, e1.getCategory() == e2.getCategory ==
	 * {@link simplenlg.framework.PhraseCategory#CLAUSE}).
	 */
	@Override
	public NLGElement apply(NLGElement previous, NLGElement next) {
		NLGElement aggregated = null;

		if (previous.getCategory() == PhraseCategory.CLAUSE
				&& next.getCategory() == PhraseCategory.CLAUSE
				&& PhraseChecker.nonePassive(previous, next)
				&& !PhraseChecker.expletiveSubjects(previous, next)) {

			// case 1: identical sentences: remove the current
			if (PhraseChecker.sameSentences(previous, next)) {
				aggregated = previous;

				// case 2: subjects identical: coordinate VPs
			} else if (PhraseChecker.sameFrontMods(previous, next)
					&& PhraseChecker.sameSubjects(previous, next)
					&& PhraseChecker.samePostMods(previous, next)) {
				aggregated = this.factory.createClause();
				aggregated.setFeature(InternalFeature.SUBJECTS, previous
						.getFeatureAsElementList(InternalFeature.SUBJECTS));
				aggregated.setFeature(InternalFeature.FRONT_MODIFIERS, previous
						.getFeatureAsElement(InternalFeature.FRONT_MODIFIERS));
				aggregated.setFeature(Feature.CUE_PHRASE, previous
						.getFeatureAsElement(Feature.CUE_PHRASE));
				aggregated
						.setFeature(
								InternalFeature.POSTMODIFIERS,
								previous
										.getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
				NLGElement vp;

				// case 2.1: VPs have different arguments but same
				// head & mods
				if (!PhraseChecker.sameVPArgs(previous, next)
						&& PhraseChecker.sameVPHead(previous, next)
						&& PhraseChecker.sameVPModifiers(previous, next)) {

					NLGElement vp1 = previous
							.getFeatureAsElement(InternalFeature.VERB_PHRASE);
					vp = this.factory.createVerbPhrase();
					vp.setFeature(InternalFeature.HEAD, vp1
							.getFeatureAsElement(InternalFeature.HEAD));
					vp
							.setFeature(
									InternalFeature.COMPLEMENTS,
									vp1
											.getFeatureAsElementList(InternalFeature.COMPLEMENTS));
					vp
							.setFeature(
									InternalFeature.PREMODIFIERS,
									vp1
											.getFeatureAsElementList(InternalFeature.PREMODIFIERS));
					vp
							.setFeature(
									InternalFeature.POSTMODIFIERS,
									vp1
											.getFeatureAsElementList(InternalFeature.POSTMODIFIERS));

					// case 2.2: just create a coordinate vP
				} else {
					NLGElement vp1 = previous
							.getFeatureAsElement(InternalFeature.VERB_PHRASE);
					NLGElement vp2 = next
							.getFeatureAsElement(InternalFeature.VERB_PHRASE);
					vp = this.factory.createCoordinatedPhrase(vp1, vp2);

					// case 2.3: expletive subjects
				}

				aggregated.setFeature(InternalFeature.VERB_PHRASE, vp);

				// case 3: identical VPs: conjoin subjects and front
				// modifiers
			} else if (PhraseChecker.sameFrontMods(previous, next)
					&& PhraseChecker.sameVP(previous, next)
					&& PhraseChecker.samePostMods(previous, next)) {
				aggregated = this.factory.createClause();
				aggregated
						.setFeature(
								InternalFeature.FRONT_MODIFIERS,
								previous
										.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS));
				CoordinatedPhraseElement subjects = this.factory
						.createCoordinatedPhrase();
				subjects.setCategory(PhraseCategory.NOUN_PHRASE);
				List<NLGElement> allSubjects = previous
						.getFeatureAsElementList(InternalFeature.SUBJECTS);
				allSubjects.addAll(next
						.getFeatureAsElementList(InternalFeature.SUBJECTS));

				for (NLGElement subj : allSubjects) {
					subjects.addCoordinate(subj);
				}

				aggregated.setFeature(InternalFeature.SUBJECTS, subjects);
				aggregated
						.setFeature(
								InternalFeature.POSTMODIFIERS,
								previous
										.getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
				aggregated.setFeature(InternalFeature.VERB_PHRASE, previous
						.getFeature(InternalFeature.VERB_PHRASE));
			}
		}

		return aggregated;
	}
}
