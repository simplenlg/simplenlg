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

package simplenlg.features;

/**
 * <p>
 * An enumeration representing the grammatical function that an element might
 * take. The discourse function is recorded under the {@code
 * Feature.DISCOURSE_FUNCTION} feature and applies to any type of {@code
 * NLGElement}.
 * </p>
 * 
 * @author A. Gatt and D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public enum DiscourseFunction {

	/**
	 * Auxiliaries are the additional verbs added to a verb phrase to alter the
	 * meaning being described. For example, <em>will</em> can be added as an
	 * auxiliary to a verb phrase to represent the future tense of the verb,
	 * <em>John <b>will</b> kiss Mary</em>.
	 */
	AUXILIARY,

	/**
	 * Complements are additional components that are required to complement the
	 * meaning of a sentence. For example,
	 * <em>put the bread <b>on the table</b></em> requires the complement
	 * <em>on the table</em> to make the clause meaningful.
	 */
	COMPLEMENT,

	/**
	 * A conjunction is a word that links items together in a coordinated
	 * phrase. The most common conjunctions are <em>and</em> and <em>but</em>.
	 */
	CONJUNCTION,

	/**
	 * Cue phrases are added to sentence to indicate document structure or flow.
	 * They normally do not add any semantic information to the phrase. For
	 * example,
	 * <em><b>Firstly</b>, let me just say it is an honour to be here.</em>
	 * <em><b>Incidentally</b>, John kissed Mary last night.</em>
	 */
	CUE_PHRASE,

	/**
	 * Front modifiers are modifiers that apply to clauses. They are placed in
	 * the syntactical structure after the cue phrase but before the subject.
	 * For example, <em>However, <b>last night</b> John kissed Mary.</em>
	 */
	FRONT_MODIFIER,

	/**
	 * This represents the main item of the phrase. For verb phrases, the head
	 * will be the main verb. For noun phrases, the head will be the subject
	 * noun. For adjective, adverb and prepositional phrases, the head will be
	 * the adjective, adverb and preposition respectively.
	 */
	HEAD,

	/**
	 * This is the indirect object of a verb phrase or an additional object that
	 * is affected by the action performed. This is typically the recipient of
	 * <em>give</em>. For example, Mary is the indirect object in the phrase
	 * <em>John gives <b>Mary</b> the flower</em>.
	 */
	INDIRECT_OBJECT,

	/**
	 * This is the object of a verb phrase and represents the item that the
	 * action is performed upon. For example, the flower is the object in the
	 * phrase <em>John gives Mary <b>the flower</b></em>.
	 */
	OBJECT,

	/**
	 * Pre-modifiers, typically adjectives and adverbs, appear before the head
	 * of a phrase. They can apply to noun phrases and verb phrases. For
	 * example, <em>the <b>beautiful</b> woman</em>,
	 * <em>the <b>ferocious</b> dog</em>.
	 */
	PRE_MODIFIER,

	/**
	 * Post-modifiers, typically adjectives and adverbs, are added after the
	 * head of the phrase. For example, <em>John walked <b>quickly</b></em>.
	 */
	POST_MODIFIER,

	/**
	 * The specifier, otherwise known as the determiner, is a word that can be
	 * placed before a noun in a noun phrase. Example specifiers include:
	 * <em>the</em>, <em>some</em>, <em>a</em> and <em>an</em> as well as the
	 * personal pronouns such as <em>my</em>, <em>your</em>, <em>their</em>.
	 */
	SPECIFIER,

	/**
	 * This is the subject of a verb phrase and represents the entity performing
	 * the action. For example, John is the subject in the phrase
	 * <em><b>John</b> gives Mary the flower.</em>
	 */
	SUBJECT,

	/**
	 * The verb phrase highlights the part of a clause that forms the verb
	 * phrase. Verb phrases can be formed of a single verb or from a verb with a
	 * particle, such as <em>kiss</em>, <em>talk</em>, <em>bark</em>,
	 * <em>fall down</em>, <em>pick up</em>.
	 */
	VERB_PHRASE;
}
