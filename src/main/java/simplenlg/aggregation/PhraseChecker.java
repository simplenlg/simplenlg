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
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.NLGElement;
import simplenlg.phrasespec.SPhraseSpec;

/**
 * This class contains a number of utility methods for checking and collecting
 * sentence components during the process of aggregation.
 * 
 * @author agatt
 * 
 */
public abstract class PhraseChecker {

	/**
	 * Check that the sentences supplied are identical
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every pair of sentences <code>s1</code>
	 *         and <code>s2</code>, <code>s1.equals(s2)</code>.
	 */
	public static boolean sameSentences(NLGElement... sentences) {
		boolean equal = false;

		if (sentences.length >= 2) {
			for (int i = 1; i < sentences.length; i++) {
				equal = sentences[i - 1].equals(sentences[i]);
			}
		}

		return equal;
	}

	/**
	 * Check whether these sentences have expletive subjects (there, it etc)
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if all the sentences have expletive subjects
	 */
	public static boolean expletiveSubjects(NLGElement... sentences) {
		boolean expl = true;

		for (int i = 1; i < sentences.length && expl; i++) {
			expl = (sentences[i] instanceof SPhraseSpec ? ((SPhraseSpec) sentences[i])
					.getFeatureAsBoolean(LexicalFeature.EXPLETIVE_SUBJECT)
					: false);

		}

		return expl;

	}

	/**
	 * Check that the sentences supplied have identical front modifiers
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every pair of sentences <code>s1</code>
	 *         and <code>s2</code>,
	 *         <code>s1.getFrontModifiers().equals(s2.getFrontModifiers())</code>
	 *         .
	 */
	public static boolean sameFrontMods(NLGElement... sentences) {
		boolean equal = true;

		if (sentences.length >= 2) {
			for (int i = 1; i < sentences.length && equal; i++) {

				if (!sentences[i - 1].hasFeature(Feature.CUE_PHRASE)
						&& !sentences[i].hasFeature(Feature.CUE_PHRASE)) {
					equal = sentences[i - 1]
							.getFeatureAsElementList(
									InternalFeature.FRONT_MODIFIERS)
							.equals(
									sentences[i]
											.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS));

				} else if (sentences[i - 1].hasFeature(Feature.CUE_PHRASE)
						&& sentences[i].hasFeature(Feature.CUE_PHRASE)) {
					equal = sentences[i - 1]
							.getFeatureAsElementList(
									InternalFeature.FRONT_MODIFIERS)
							.equals(
									sentences[i]
											.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS))
							&& sentences[i]
									.getFeatureAsElementList(Feature.CUE_PHRASE)
									.equals(
											sentences[i - 1]
													.getFeatureAsElementList(Feature.CUE_PHRASE));

				} else {
					equal = false;
				}
			}
		}

		return equal;
	}

	/**
	 * Check that some phrases have the same postmodifiers
	 * 
	 * @param sentences
	 *            the phrases
	 * @return true if they have the same postmodifiers
	 */
	public static boolean samePostMods(NLGElement... sentences) {
		boolean equal = true;

		if (sentences.length >= 2) {

			for (int i = 1; i < sentences.length && equal; i++) {
				equal = sentences[i - 1]
						.getFeatureAsElementList(InternalFeature.POSTMODIFIERS)
						.equals(
								sentences[i]
										.getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
			}
		}

		return equal;
	}

	/**
	 * Check that the sentences supplied have identical subjects
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every pair of sentences <code>s1</code>
	 *         and <code>s2</code>
	 *         <code>s1.getSubjects().equals(s2.getSubjects())</code>.
	 */
	public static boolean sameSubjects(NLGElement... sentences) {
		boolean equal = sentences.length >= 2;

		for (int i = 1; i < sentences.length && equal; i++) {
			equal = sentences[i - 1].getFeatureAsElementList(
					InternalFeature.SUBJECTS).equals(
					sentences[i]
							.getFeatureAsElementList(InternalFeature.SUBJECTS));
		}

		return equal;
	}

	// /**
	// * Check that the sentences have the same complemts raised to subject
	// * position in the passive
	// *
	// * @param sentences
	// * the sentences
	// * @return <code>true</code> if the passive raising complements are the
	// same
	// */
	// public static boolean samePassiveRaisingSubjects(SPhraseSpec...
	// sentences) {
	// boolean samePassiveSubjects = sentences.length >= 2;
	//
	// for (int i = 1; i < sentences.length && samePassiveSubjects; i++) {
	// VPPhraseSpec vp1 = (VPPhraseSpec) sentences[i - 1].getVerbPhrase();
	// VPPhraseSpec vp2 = (VPPhraseSpec) sentences[i].getVerbPhrase();
	// samePassiveSubjects = vp1.getPassiveRaisingComplements().equals(
	// vp2.getPassiveRaisingComplements());
	//
	// }
	//
	// return samePassiveSubjects;
	// }

	/**
	 * Check whether all sentences are passive
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every sentence <code>s</code>,
	 *         <code>s.isPassive() == true</code>.
	 */
	public static boolean allPassive(NLGElement... sentences) {
		boolean passive = true;

		for (int i = 0; i < sentences.length && passive; i++) {
			passive = sentences[i].getFeatureAsBoolean(Feature.PASSIVE);
		}

		return passive;
	}

	/**
	 * Check whether all sentences are active
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every sentence <code>s</code>,
	 *         <code>s.isPassive() == false</code>.
	 */
	public static boolean allActive(NLGElement... sentences) {
		boolean active = true;

		for (int i = 0; i < sentences.length && active; i++) {
			active = !sentences[i].getFeatureAsBoolean(Feature.PASSIVE);
		}

		return active;
	}

	/**
	 * Check whether the sentences have the same <I>surface</I> subjects, that
	 * is, they are either all active and have the same subjects, or all passive
	 * and have the same passive raising subjects.
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if the sentences have the same surface subjects
	 */
	public static boolean sameSurfaceSubjects(NLGElement... sentences) {
		return PhraseChecker.allActive(sentences)
				&& PhraseChecker.sameSubjects(sentences)
				|| PhraseChecker.allPassive(sentences);
		// && PhraseChecker.samePassiveRaisingSubjects(sentences);
	}

	/**
	 * Check that a list of sentences have the same verb
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every pair of sentences <code>s1</code>
	 *         and <code>s2</code>
	 *         <code>s1.getVerbPhrase().getHead().equals(s2.getVerbPhrase().getHead())</code>
	 */
	public static boolean sameVPHead(NLGElement... sentences) {
		boolean equal = sentences.length >= 2;

		for (int i = 1; i < sentences.length && equal; i++) {
			NLGElement vp1 = sentences[i - 1]
					.getFeatureAsElement(InternalFeature.VERB_PHRASE);
			NLGElement vp2 = sentences[i]
					.getFeatureAsElement(InternalFeature.VERB_PHRASE);

			if (vp1 != null && vp2 != null) {
				NLGElement h1 = vp1.getFeatureAsElement(InternalFeature.HEAD);
				NLGElement h2 = vp2.getFeatureAsElement(InternalFeature.HEAD);
				equal = h1 != null && h2 != null ? h1.equals(h2) : false;

			} else {
				equal = false;
			}
		}

		return equal;
	}

	/**
	 * Check that the sentences supplied are either all active or all passive.
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if the sentences have the same voice
	 */
	public static boolean haveSameVoice(NLGElement... sentences) {
		boolean samePassive = true;
		boolean prevIsPassive = false;

		if (sentences.length > 1) {
			prevIsPassive = sentences[0].getFeatureAsBoolean(Feature.PASSIVE);

			for (int i = 1; i < sentences.length && samePassive; i++) {
				samePassive = sentences[i].getFeatureAsBoolean(Feature.PASSIVE) == prevIsPassive;
			}
		}

		return samePassive;
	}

	// /**
	// * Check that the sentences supplied are not existential sentences (i.e.
	// of
	// * the form <I>there be...</I>)
	// *
	// * @param sentences
	// * the sentences
	// * @return <code>true</code> if none of the sentences is existential
	// */
	// public static boolean areNotExistential(SPhraseSpec... sentences) {
	// boolean notex = true;
	//
	// for (int i = 0; i < sentences.length && notex; i++) {
	// notex = !sentences[i].isExistential();
	// }
	//
	// return notex;
	// }

	/**
	 * Check that the sentences supplied have identical verb phrases
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every pair of sentences <code>s1</code>
	 *         and <code>s2</code>,
	 *         <code>s1.getVerbPhrase().equals(s2.getVerbPhrase())</code>.
	 */
	public static boolean sameVP(NLGElement... sentences) {
		boolean equal = sentences.length >= 2;

		for (int i = 1; i < sentences.length && equal; i++) {
			equal = sentences[i - 1].getFeatureAsElement(
					InternalFeature.VERB_PHRASE).equals(
					sentences[i]
							.getFeatureAsElement(InternalFeature.VERB_PHRASE));
		}

		return equal;
	}

	/**
	 * Check that the sentences supplied have the same complements at VP level.
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if for every pair of sentences <code>s1</code>
	 *         and <code>s2</code>, their VPs have the same pre- and
	 *         post-modifiers and complements.
	 */
	public static boolean sameVPArgs(NLGElement... sentences) {
		boolean equal = sentences.length >= 2;

		for (int i = 1; i < sentences.length && equal; i++) {
			NLGElement vp1 = sentences[i - 1]
					.getFeatureAsElement(InternalFeature.VERB_PHRASE);
			NLGElement vp2 = sentences[i]
					.getFeatureAsElement(InternalFeature.VERB_PHRASE);

			equal = vp1
					.getFeatureAsElementList(InternalFeature.COMPLEMENTS)
					.equals(
							vp2
									.getFeatureAsElementList(InternalFeature.COMPLEMENTS));
		}

		return equal;
	}

	/**
	 * check that the phrases supplied are sentences and have the same VP
	 * premodifiers and postmodifiers
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if all pairs of sentences have VPs with the
	 *         same pre and postmodifiers
	 */
	public static boolean sameVPModifiers(NLGElement... sentences) {
		boolean equal = sentences.length >= 2;

		for (int i = 1; i < sentences.length && equal; i++) {
			NLGElement vp1 = sentences[i - 1]
					.getFeatureAsElement(InternalFeature.VERB_PHRASE);
			NLGElement vp2 = sentences[i]
					.getFeatureAsElement(InternalFeature.VERB_PHRASE);

			equal = vp1
					.getFeatureAsElementList(InternalFeature.POSTMODIFIERS)
					.equals(
							vp2
									.getFeatureAsElementList(InternalFeature.POSTMODIFIERS))
					&& vp1
							.getFeatureAsElementList(
									InternalFeature.PREMODIFIERS)
							.equals(
									vp2
											.getFeatureAsElementList(InternalFeature.PREMODIFIERS));
		}

		return equal;
	}

	/**
	 * Collect a list of pairs of constituents with the same syntactic function
	 * from the left periphery of two sentences. The left periphery encompasses
	 * the subjects, front modifiers and cue phrases of the sentences.
	 * 
	 * @param sentences
	 *            the list of sentences
	 * @return a list of pairs of constituents with the same function, if any
	 *         are found
	 */
	public static List<PhraseSet> leftPeriphery(NLGElement... sentences) {
		List<PhraseSet> funcsets = new ArrayList<PhraseSet>();
		PhraseSet cue = new PhraseSet(DiscourseFunction.CUE_PHRASE);
		PhraseSet front = new PhraseSet(DiscourseFunction.FRONT_MODIFIER);
		PhraseSet subj = new PhraseSet(DiscourseFunction.SUBJECT);

		for (NLGElement s : sentences) {
			if (s.hasFeature(Feature.CUE_PHRASE)) {
				cue.addPhrases(s.getFeatureAsElementList(Feature.CUE_PHRASE));
			}

			if (s.hasFeature(InternalFeature.FRONT_MODIFIERS)) {
				front
						.addPhrases(s
								.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS));
			}

			if (s.hasFeature(InternalFeature.SUBJECTS)) {
				subj.addPhrases(s
						.getFeatureAsElementList(InternalFeature.SUBJECTS));
			}
		}

		funcsets.add(cue);
		funcsets.add(front);
		funcsets.add(subj);
		return funcsets;
	}

	/**
	 * Collect a list of pairs of constituents with the same syntactic function
	 * from the right periphery of two sentences. The right periphery
	 * encompasses the complements of the main verb, and its postmodifiers.
	 * 
	 * @param sentences
	 *            the list of sentences
	 * @return a list of pairs of constituents with the same function, if any
	 *         are found
	 */
	public static List<PhraseSet> rightPeriphery(NLGElement... sentences) {
		List<PhraseSet> funcsets = new ArrayList<PhraseSet>();
		PhraseSet comps = new PhraseSet(DiscourseFunction.OBJECT);
		// new PhraseSet(DiscourseFunction.INDIRECT_OBJECT);
		PhraseSet pmods = new PhraseSet(DiscourseFunction.POST_MODIFIER);		
		
		for (NLGElement s : sentences) {
			NLGElement vp = s.getFeatureAsElement(InternalFeature.VERB_PHRASE);

			if (vp != null) {
				if (vp.hasFeature(InternalFeature.COMPLEMENTS)) {
					comps
							.addPhrases(vp
									.getFeatureAsElementList(InternalFeature.COMPLEMENTS));
				}

				if (vp.hasFeature(InternalFeature.POSTMODIFIERS)) {
					pmods
							.addPhrases(vp
									.getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
				}
			}
			
			if (s.hasFeature(InternalFeature.POSTMODIFIERS)) {
				pmods
						.addPhrases(s
								.getFeatureAsElementList(InternalFeature.POSTMODIFIERS));
			}
		}

		funcsets.add(comps);
		funcsets.add(pmods);
		return funcsets;
	}

	/**
	 * Check that no element of a give array of sentences is passive.
	 * 
	 * @param sentences
	 *            the sentences
	 * @return <code>true</code> if none of the sentences is passive
	 */
	public static boolean nonePassive(NLGElement... sentences) {
		boolean nopass = true;

		for (int i = 0; i < sentences.length && nopass; i++) {
			nopass = !sentences[i].getFeatureAsBoolean(Feature.PASSIVE);
		}

		return nopass;
	}
}
