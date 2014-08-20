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
 * This class defines a list of features internally used within the SimpleNLG
 * system.
 *  Note that there are three feature classes in SimpleNLG.
 * <UL>
 * <LI> <code>Feature</code>: features typically set up developers invoking SimpleNLG
 * <LI> <code>LexicalFeature</code>: features typically set up the SimpleNLG lexicon
 * <LI> <code>InternalFeature</code>: features typically used internally by SimpleNLG
 * </UL>
 * 
 * Elements in the system can, in theory, take any kind of feature. Some
 * features will only be expected by certain processors, however. 
 * Developers can define their own features but should
 * choose names that do not conflict with those presented here.
 * </p>
 * <p>
 * The details for each feature are supplied in a table. The entries are:
 * </p>
 * <table border="1">
 * <tr>
 * <td><b>Feature name</b></td>
 * <td>This is the name that will appear in the element's feature list as
 * produced by the <code>toString()</code> method or by calling
 * <code>getAllFeatureNames</code></td>
 * </tr>
 * <tr>
 * <td><b>Expected type</b></td>
 * <td>As features are represented as a <code>Map</code> connecting a
 * <code>String</code> and an <code>Object</code> then, in theory, a feature can
 * take any object as a value. This table entry defines the type that the
 * SimpleNLG system expects.</td>
 * </tr>
 * <tr>
 * <td><b>Created by</b></td>
 * <td>Defines where the feature is created. In addition, all features can be
 * added specifically by users</td>
 * </tr>
 * <tr>
 * <td><b>Used by</b></td>
 * <td>Defines which processors use the feature.</td>
 * </tr>
 * <tr>
 * <td><b>Applies to</b></td>
 * <td>Defines which structural, syntactical or lexical elements this feature is
 * applied to.</td>
 * </tr>
 * <tr>
 * <td><b>Default</b></td>
 * <td>Any default values attributed to the feature are given here.</td>
 * </tr>
 * <tr>
 * </table>
 * 
 * @author E. Reiter and D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */

@SuppressWarnings("nls")
public abstract class InternalFeature {

	/**
	 * <p>
	 * This feature determines if the element is an acronym.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isAcronym</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory creates the feature on noun phrases. The syntax
	 * processor creates the feature on nouns.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>No processors currently use this feature. It is expected to be used
	 * by the orthography processor.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Nouns and noun phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String ACRONYM = "acronym";
	/**
	 * <p>
	 * This feature is used to reference the base word element as created by the
	 * lexicon.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>baseWord</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>WordElement</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Currently initially set by the syntax processor but should be done by
	 * the phrase factory with the syntax and morphology processors only setting
	 * this if the it doesn't already exist.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor refers to the base word when determining
	 * adjective ordering. The morphology processor also needs the base word for
	 * performing morphology on the lexical items.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td><code>InflectedWordElement</code>s of any category.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String BASE_WORD = "base_word";
	/**
	 * <p>
	 * This feature determines the status of a sentence.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>clauseStatus</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>ClauseStatus</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory creates this feature when creating sentences.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor refers to the clause status when deciding
	 * whether to add the complementiser or not. Only subordinate clauses will
	 * have the complementiser added.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>ClauseStatus.MATRIX</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String CLAUSE_STATUS = "clause_status";
	/**
	 * <p>
	 * This feature refers to the list of complements for the phrase.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>complements</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List&lt;NLGElement&gt;</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory has the functionality for taking complements when
	 * creating prepositional phrases or sentences. Complements can also be
	 * added to other types of phrases by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor realises the complements in the correct
	 * syntactical order when realising phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Phrases of any type.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>ClauseStatus.MATRIX</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String COMPLEMENTS = "complements";
	/**
	 * <p>
	 * This feature refers to the list of components in a
	 * <code>ListElement</code>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>components</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List&lt;NLGElements&gt;</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The syntax processor creates <code>ListElement</code>s with
	 * components. This is done as part of the normal realisation of phrases
	 * into a list of words. Components can be added by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax and morphology processors both access the components
	 * feature when realising <code>ListElement</code>s.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td><code>ListElement</code>s.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String COMPONENTS = "components";
	/**
	 * <p>
	 * This feature is the list of coordinated phrases in a
	 * <code>CoordinatedPhraseElement</code>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>coordinates</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List&lt;NLGElements&gt;</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td><code>CoordinatedPhraseElement</code> has convenience methods for
	 * adding the coordinate phrases to a particular.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processors creates the structure of coordinated phrases
	 * and adds in the conjoining word where appropriate.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td><code>CoordinatedPhraseElements</code>s only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String COORDINATES = "coordinates";
	/**
	 * <p>
	 * This feature defines the role each element plays in the structure of the
	 * text. For example, the phrase <em>John played football</em> has
	 * <em>John</em> as the subject, <em>play</em> as the base verb and
	 * <em>football</em> as the complement.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>discourseFunction</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>DiscourseFunction</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The syntax processor defines the function of each word as it parses
	 * phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses the function when checking pronoun
	 * inflections.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Any NLGElement but typically words.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String DISCOURSE_FUNCTION = "discourse_function";
	
	public static final String NON_MORPH = "non_morph";
	/**
	 * <p>
	 * This feature tracks any front modifiers in sentences. Front modifiers are
	 * placed after the cue phrase but before the subject.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>frontModifiers</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>NLGElement</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Front modifiers need to be manually added by users.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor realises front modifiers in their correct place
	 * within the structure of the sentence.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Sentences.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String FRONT_MODIFIERS = "front_modifiers";
	/**
	 * <p>
	 * This feature points to the head element in a phrase. The head element is
	 * deemed to be the subject in a noun phrase, the verb in a verb phrase, the
	 * adjective in an adjective phrase, the adverb in an adverb phrase or the
	 * preposition in a preposition phrase. The <code>PhraseElement</code> has a
	 * convenience method for getting and setting the head feature.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>phraseHead</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>NLGElement</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory sets an appropriate head when constructing
	 * phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor uses the head element when constructing the
	 * correct syntax for the text. The head element is also important for
	 * determining the main verb in a verb group.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td>The value is set to an appropriate element by the phrase factory.</td>
	 * </tr>
	 * </table>
	 */
	public static final String HEAD = "head";
	/**
	 * <p>
	 * This flag is used to determine if the modal should be included in the
	 * verb phrase.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>ignoreModal</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The syntax processor sets this flag when dealing with interrogatives.
	 * </td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor will ignore modals on certain interrogatives.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Coordinated phrases and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String IGNORE_MODAL = "ignore_modal";
	/**
	 * <p>
	 * This flag determines if the sentence is interrogative or not.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isInterrogative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The syntax processor sets this feature on sentences if a contained
	 * phrase is interrogative.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>Orthography processor uses this to add a question mark instead of a
	 * period at the end of interrogative sentences.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Sentences only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String INTERROGATIVE = "interrogative";
	/**
	 * <p>
	 * This feature represents the list of post-modifier elements.
	 * Post-modifiers are added to the end of phrases and coordinated phrases.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>postModifiers</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List&lt;NLGElement&gt;</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user specifies the post-modifiers. Convenience methods are added.
	 * </td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor correctly adds the post-modifiers into the
	 * structure of the text.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, phrases and coordinated phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String POSTMODIFIERS = "postmodifiers";
	/**
	 * <p>
	 * This feature represents the list of premodifier elements. Premodifiers
	 * are added to phrases before the head of the phrase, and to coordinated
	 * phrases before the coordinates.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>preModifiers</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List&lt;NLGElement&gt;</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user specifies the premodifiers. Convenience methods are added.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor correctly adds the premodifiers into the
	 * structure of the text.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, phrases and coordinated phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PREMODIFIERS = "premodifiers";
	/**
	 * <p>
	 * This flag is used to define whether a noun phrase has had its specifier
	 * raised. It is used in conjunction with the <code>RAISE_SECIFIER</code>
	 * feature.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isRaised</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The syntax processor sets this flag on noun phrases when processing
	 * coordinated phrases whose specifier has been raised.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor to correctly add or remove specifiers.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Noun phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String RAISED = "raised";
	/**
	 * <p>
	 * This flag determines if auxiliary verbs should be realised in coordinated
	 * verb phrases.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isRealiseAuxiliary</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The syntax processor sets this flag on verb phrases when processing
	 * coordinated phrases which has had the <code>AGGREGATE_AUXILIARY</code>
	 * feature set.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor to correctly add or ignore auxiliary verbs in
	 * verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verb phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String REALISE_AUXILIARY = "realise_auxiliary";
	/**
	 * <p>
	 * This feature contains the specifier for a noun phrase. For example
	 * <em>the</em> and <em>my</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>specifier</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>NLGElement</code> or <code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Specifiers are added to noun phrases when they are constructed by the
	 * phrase factory.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor places specifiers before the main subject in a
	 * noun phrase.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Noun phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String SPECIFIER = "specifier";
	/**
	 * <p>
	 * This feature represents the list of subjects in a clause.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>subjects</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List&lt;NLGElement&gt;</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Subjects are added to clauses through the phrase factory.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor realises all subjects in the correct place.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String SUBJECTS = "subjects";
	/**
	 * <p>
	 * This feature represents the verb phrase in a clause.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>verbPhrase</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>NLGElement</code>, typically a <code>PhraseElement</code>, but
	 * can also be a <code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The verb phrase is added to clauses through the phrase factory.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor realises the verb phrase in the correct place.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String VERB_PHRASE = "verb_phrase";

	/**
	 * The constructor is never needed.
	 */
	private InternalFeature() {
		// do nothing
	}
}
