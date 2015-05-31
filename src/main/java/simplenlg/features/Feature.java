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
 * This class defines a list of features which can be set up users of SimpleNLG.
 * Note that there are three feature classes in SimpleNLG.
 * <UL>
 * <LI> <code>Feature</code>: features typically set up developers invoking
 * SimpleNLG
 * <LI> <code>LexicalFeature</code>: features typically set up the SimpleNLG
 * lexicon
 * <LI> <code>InternalFeature</code>: features typically used internally by
 * SimpleNLG
 * </UL>
 * 
 * Elements in the system can, in theory, take any kind of feature. Some
 * features will only be expected by certain processors, however. Developers can
 * define their own features but should choose names that do not conflict with
 * those presented here.
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
abstract public class Feature {

	/**
	 * The constructor is never needed.
	 */
	private Feature() {
		// do nothing
	}

	/**
	 * <p>
	 * This feature determines if the adjectives should be reordered. Some
	 * lexicons might support the positioning of adjectives and by default, this
	 * order will be used. The user can override the ordering of adjectives by
	 * setting this feature to <code>false</code>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>adjectiveOrdering</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Set by the phrase factory on noun phrases. It can be overwritten by
	 * the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor will reorder adjectives in the premodifiers list
	 * if this feature has a value of <code>true</code>.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Noun phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.TRUE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String ADJECTIVE_ORDERING = "adjective_ordering";

	/**
	 * <p>
	 * This feature determines if the auxiliary verbs in a clause should be
	 * aggregated.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>aggregateAuxiliary</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Needs to be manually set by user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor will remove the auxiliary verbs from clauses
	 * when this is set to <code>true</code>.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String AGGREGATE_AUXILIARY = "aggregate_auxiliary";

	/**
	 * <p>
	 * The complementiser is the word that joins a subordinate clause to the
	 * parent clause. For example, the two clauses: <em>Timmy sang a song</em>
	 * and <em>moved Elizabeth to tears</em> can be joined with the
	 * complementiser <em>that</em> to form:
	 * <em>Timmy sang a song <b>that</b> moved Elizabeth to
	 * tears</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>complementiser</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td>Can take any <code>NLGElement</code> or a string. It is recommended
	 * to use
	 * <em>InflectedWordElement<code> as created by the <code>PhraseFactory</code>
	 * method <code>createWord(...)</code>.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The default is created by the <code>PhraseFactory<code> when creating 
	 * clauses.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor uses the value of this feature when adding
	 * complements to subordinate clauses.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td>"that" represented by an <code>InflectedWordElement</code> if a
	 * lexicon is used, otherwise a <code>StringElement</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static String COMPLEMENTISER = "complementiser";

	/**
	 * <p>
	 * This feature represents the word (or phrase) used for linking coordinated
	 * phrases together.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>conjunction</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td>A <code>String</code>.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td><code>CoordinatedPhraseElement</code> creates this feature
	 * automatically. The user can overwrite the value.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor constructs the correct syntax for the
	 * coordinated phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td><code>CoordinatedPhraseElement</code>s.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String CONJUNCTION = "conjunction";

	/**
	 * <p>
	 * This feature represents the type of conjunction this coordination
	 * represents.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>conjunctionType</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>ConjunctionType</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td><code>CoordinatedPhraseElement</code> sets this value automatically.
	 * It can be overwritten by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor constructs the correct syntax for the
	 * coordinated phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td><code>CoordinatedPhraseElement</code>s.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>ConjunctionType.COORDINATING</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String CONJUNCTION_TYPE = "conjunction_type";

	/**
	 * <p>
	 * An appositive is a type of postmodifying phrase which is quasi-synonymous
	 * with, or a possible replacement of, the phrase it modifies. A typical
	 * example occurs with NPs, e.g.:
	 * <em>his house, <b>a large villa</b>, is on the hill</em> where the phrase
	 * <em>a large villa</em> is an appositive postmodifier of
	 * <em>his house</em>. Note that appositives are usually realised surrounded
	 * by commas. Accordingly, this feature is primarily used by the orthography
	 * processor to determine whether commas should be placed around a
	 * postmodifying phrase.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>appositive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The orthography processor to determine comma placement.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Any phrase which is a postmodifier of another phrase.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>false</code> or <code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String APPOSITIVE = "appositive";

	/**
	 * <p>
	 * This feature represents the cue phrase of a sentence. Cue phrases
	 * sometimes appear at the start of sentences. In the following example,
	 * <em>however</em> forms the cue phrase:<br>
	 * <em><b>However</b>, John 
	 * played football instead.</em>
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>cuePhrase</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>NLGElement</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Cue phrases need to be specifically added by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor places the cue phrase at the start of a
	 * sentence.</td>
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
	public static final String CUE_PHRASE = "cue_phrase";

	/**
	 * <p>
	 * This features determines if the phrase is elided. Elided phrases are
	 * omitted from the final realisation.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isElided</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The feature needs to be set by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor removes elided phrases.</td>
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
	public static final String ELIDED = "elided";

	/**
	 * <p>
	 * This feature dictates the form that a verb takes.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>form</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Form</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Initially created by the phrase factory when constructing verb
	 * phrases. The user is free to set the value accordingly.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor set the form on sentences to be the same as that
	 * from the containing verb phrase. The syntax processor will also check a
	 * verb's form for the addition of supporting words such as <em>will</em>
	 * and <em>to</em>. The morphology processor uses the form to determine the
	 * actual inflection of the verb to be used.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Nouns, verbs noun phrases and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Form.NORMAL</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String FORM = "form";

	/**
	 * <p>
	 * This feature determines the type of interrogative (question) used for the
	 * clause.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>interrogativeType</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>InterrogativeType</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user must set this value.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor uses this feature to correctly structure
	 * interrogative clauses.</td>
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
	public static final String INTERROGATIVE_TYPE = "interrogative_type";

	/**
	 * <p>
	 * This flag determines if the Adjective or Adverb should be inflected into
	 * the comparative form. For example, the comparative form for
	 * <em>early</em> is <em>earlier</em>, the comparative form of <em>big</em>
	 * is <em>bigger</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isComparative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor to correctly inflect the word.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives and adverbs only</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String IS_COMPARATIVE = "is_comparative";

	/**
	 * <p>
	 * This flag determines if the Adjective or Adverb should be inflected into
	 * the superlative form. For example, the comparative form for
	 * <em>early</em> is <em>earliest</em>, the superlative form of <em>big</em>
	 * is <em>biggest</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isSuperlative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor to correctly inflect the word.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives and adverbs only</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String IS_SUPERLATIVE = "is_superlative";

	/**
	 * <p>
	 * The modal represents the modal auxiliary verb that is used in a verb
	 * phrase to express mood or tense. The English modals are: <em>can</em>,
	 * <em>may</em>, <em>must</em>, <em>ought</em>, <em>shall</em>,
	 * <em>should</em>, <em>will</em> and <em>would</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>modal</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor adds modals into the structure.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases and verb phrases. In the case of clauses
	 * and coordinated phrases, the modal is passed on to the underlying verb
	 * phrase.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String MODAL = "modal";

	/**
	 * <p>
	 * The flag determines if the corresponding verb phrase should be negated.
	 * For example, negating the clause <em>John kissed Mary</em> results in the
	 * clause <em>John did not kiss Mary</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isNegated</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor, which will correctly add in all necessary
	 * auxiliary verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases and verb phrases. In the case of clauses
	 * and coordinated phrases, the modal is passed on to the underlying verb
	 * phrase. Applying negation to a coordinated phase will negate all the
	 * coordinates.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String NEGATED = "negated";

	/**
	 * <p>
	 * This feature is used to determine if the element is to be represented in
	 * singular or plural form.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>number</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>NumberAgreement</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory will create the number in certain cases, mostly in
	 * dealing with pronouns. However, with supporting lexicons a word such as
	 * <em>dogs</em> will be correctly identified as the plural form of
	 * <em>dog</em>. The user can also set this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor ensures number agreement across phrases and
	 * clauses, while the morphology processor uses the feature to pluralise
	 * words.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases, noun phrases and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>NumberAgreement.SINGULAR</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String NUMBER = "number";

	/*
	 * <p> This feature represents the pattern that a particular word follows
	 * when being inflected. These can be <em>regular</em>, <em>irregular</em>,
	 * <em>regular double</em> and in the case of nouns, <em>Greco-Latin
	 * regular</em>. </p> <table border="1"> <tr> <td><b>Feature name</b></td>
	 * <td><em>pattern</em></td> </tr> <tr> <td><b>Expected type</b></td>
	 * <td><code>Pattern</code></td> </tr> <tr> <td><b>Created by</b></td>
	 * <td>The user.</td> </tr> <tr> <td><b>Used by</b></td> <td>The morphology
	 * processor uses this feature to correctly inflect words following a simple
	 * set of rules when no lexicon or user-defined form is supplied.</td> </tr>
	 * <tr> <td><b>Applies to</b></td> <td>Adjectives, nouns and verbs.</td>
	 * </tr> <tr> <td><b>Default</b></td> <td><code>null</code>.</td> </tr>
	 * </table>
	 */
	// public static final String PATTERN = "pattern";

	/**
	 * <p>
	 * This feature represents a particle used in conjunction with a verb. For
	 * example, the verb phrases <em>fall down</em> and <em>look up</em> have
	 * particles of <em>down</em> and <em>up</em> respectively.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>particle</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code> or <code>NLGElement</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory will automatically check verbs to see if they
	 * contain more than one word. In such a case, the first word becomes the
	 * verb while additional words form the particle. The user can also
	 * explicitly specify a particle</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor adds particles into the structure.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verb phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PARTICLE = "particle";

	/**
	 * <p>
	 * This flag shows if the phrase or clause should be written in the passive
	 * form. For example, the clause <em>the cat ate the mouse</em> can be
	 * written in the passive form as <em>the mouse was eaten by the cat</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isPassive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>User defined.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor uses the passive feature to determine the
	 * correct ordering of subjects and objects in a clause. It also adds in
	 * additional auxiliary verbs to verb phrases, such as <em>was</em> in the
	 * above example. The morphology processor will change pronouns of noun
	 * phrases into passive form.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, noun phrases and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PASSIVE = "passive";

	/**
	 * <p>
	 * This flag shows if the phrase or clause should be written in the perfect
	 * form. The perfect aspect is normally formed from the auxiliary verb
	 * <em>to have</em> followed by the past participle of the main verb. For
	 * example, the phrase <em>the cat eats the mouse</em> would have the
	 * present perfect form of <em>the cat has eaten the mouse</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isPerfect</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>User defined.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor adds in additional auxiliary verbs to verb
	 * phrases, and alters the form of the main verb.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PERFECT = "perfect";

	/**
	 * <p>
	 * This feature represents the first-person, second-person or third-person
	 * nature of the phrase. This predominantly affects pronouns such as
	 * <em>I</em>, <em>you</em> and <em>they</em> but some verbs will also be
	 * modified depending on the person of reference. For example, kiss is used
	 * as the present tense for first and second person (<em>I kiss Mary</em>
	 * and <em>you kiss Mary</em>) while kisses is used for third person (
	 * <em>he kisses Mary</em>).
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>person</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Person</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Will be set automatically by the phrase factory when personal
	 * pronouns are identified. May also be set by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor does some basic checking on the person-nature of
	 * phrases while the morphology processor will correctly inflect pronouns
	 * and verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases, noun phrases and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Person.THIRD</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PERSON = "person";

	/**
	 * <p>
	 * This flag shows if the noun phrase should be written in the possessive
	 * form. The possessive form of a noun is usually formed from the noun with
	 * <em>'s</em> added to the end. For example, <em>dog</em> has a possessive form <em>dog's</em>
	 * . Certain personal pronouns follow different rules, the possessive form
	 * of <em>I</em> is <em>mine</em>, <em>you</em> is <em>yours</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isPossessive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>User defined.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor correctly defines the possessive on noun phrases
	 * that contain more than one subject (we say <em>John and Mary's</em> not <em>John's and Mary's</em>
	 * ). The morphology processor correctly inflects the possessive forms of
	 * nouns and pronouns.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases and noun phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code> in most cases or for pronouns it is
	 * dependent on the pronoun form used.</td>
	 * </tr>
	 * </table>
	 */
	public static final String POSSESSIVE = "possessive";

	/**
	 * <p>
	 * This flag can be set for noun phrases where it is desirable to overwrite
	 * the subject with a suitable pronoun.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isPronominal</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>User defined.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor will replace the subject of a noun phrase with a
	 * suitable personal pronoun.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Noun phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PRONOMINAL = "pronominal";

	/**
	 * <p>
	 * This flag determines if the verb phrase should be constructed in the
	 * progressive form. For example, the progressive form of <em>John kisses 
	 * Mary</em> is <em>John is kissing Mary</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>progressive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Set by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor adds in required auxiliary verbs when dealing
	 * with the progressive form.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases and verb phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PROGRESSIVE = "progressive";

	/**
	 * <p>
	 * This flag can be set when specifiers in a coordinated phrase should be
	 * raised. For example, the coordinated phrase <em>my cat and my dog</em>
	 * can have its specifiers raised becoming <em>my cat and dog</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>raiseSpecifier</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>User defined.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor to correctly add or remove specifiers.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Coordinated phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String RAISE_SPECIFIER = "raise_specifier";

	/**
	 * <p>
	 * This flag is set when it is necessary to suppress the genitive when
	 * dealing with gerund forms.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>suppressGenitive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor will set subjects to be possessive when dealing
	 * with the gerund form unless this flag is set.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String SUPPRESS_GENITIVE_IN_GERUND = "suppress_genitive_in_gerund";

	/**
	 * <p>
	 * This flag determines if complementisers in subordinating clauses should
	 * be suppressed.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isSuppressedComplementiser</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory sets some defaults but can be overridden by the
	 * user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor will ignore suppressed complementisers on
	 * subordinating clauses.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses and coordinated phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code> for clauses or <code>Boolean.TRUE</code>
	 * on clauses when added to a coordinated phrase.</td>
	 * </tr>
	 * </table>
	 */
	public static final String SUPRESSED_COMPLEMENTISER = "suppressed_complementiser";

	/**
	 * <p>
	 * This flag represents the tense or temporal quality of a verb.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>tense</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Tense</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor ensures tense on phrases, clauses and
	 * coordinated phrases is passed on to the underlying verb phrases. The
	 * morphology processor uses the tese to correctly inflect verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Clauses, coordinated phrases and verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Tense.PRESENT</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String TENSE = "tense";
}
