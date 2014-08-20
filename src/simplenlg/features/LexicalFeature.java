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

import simplenlg.framework.ElementCategory;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.PhraseCategory;

/**
 * <p>
 * This class defines a list of constant values used by SimpleNLG lexicons. Note
 * that there are three feature classes in SimpleNLG.
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
public abstract class LexicalFeature {

	/**
	 * <p>
	 * This feature is used to map an acronym element to the full forms of the
	 * acronym.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>acronymOf</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List<WordElement></code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Lexicons that support acronyms should set this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>No processors currently use this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Any lexical item.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String ACRONYM_OF = "acronym_of";

	/**
	 * <p>
	 * This feature is used to map a word to its acronyms.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>acronyms</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List<WordElement></code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Lexicons that support acronyms should set this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>No processors currently use this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Any lexical item.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String ACRONYMS = "acronyms";

	/*
	 * <p> This feature is used to list all the possible inflectional variants
	 * of a word. For example, the word <I>fish</I> can be both <I>uncount</I>
	 * (plural: <I>fish</I>) and <I>reg</I> (plural: <I>fishes</I>). </p> <table
	 * border="1"> <tr> <td><b>Feature name</b></td> <td><em>infl</em></td>
	 * </tr> <tr> <td><b>Expected type</b></td>
	 * <td><code>List<Inflection><String></code></td> </tr> <tr> <td><b>Created
	 * by</b></td> <td>Lexicons that support inflectional variants should set
	 * this feature.</td> </tr> <tr> <td><b>Used by</b></td> <td>No processors
	 * currently use this feature.</td> </tr> <tr> <td><b>Applies to</b></td>
	 * <td>Any lexical item.</td> </tr> <tr> <td><b>Default</b></td>
	 * <td><code>null</code></td> </tr> </table>
	 */
	// public static final String INFLECTIONS = "infl";

	/**
	 * <p>
	 * This feature is used to specify, for a given word, what its default
	 * inflectional variant is, if more than one is possible.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>default_infl</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Inflection</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Lexicons that support multiple inflectional variants should set this
	 * feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>{@link simplenlg.morphology.english.MorphologyProcessor}.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Nouns and verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String DEFAULT_INFL = "default_infl";

	/**
	 * <p>
	 * This feature is used to specify the spelling variants of a word.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>spell_vars</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>List<String></code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Lexicons that support multiple spelling variants should set this
	 * feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>No processors currently use this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Any lexical item.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String SPELL_VARS = "spell_vars";

	/**
	 * <p>
	 * This feature is used to specify the default spelling variant of a word,
	 * if it has more than one.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>default_spell</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Lexicons that support multiple spelling variants should set this
	 * feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>{@link simplenlg.morphology.english.MorphologyProcessor}</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Any lexical item.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String DEFAULT_SPELL = "default_spell";

	/**
	 * <p>
	 * This feature is used to define the base form for phrases and words.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>baseForm</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The lexicon accessor also creates the feature when looking up words
	 * in the lexicon. Sometimes the phrase factory sets this feature as well,
	 * as an approximate realisation for debuggin purposes</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses the base form in its simple rules for
	 * determining word inflection. The morphology processor and syntax
	 * processor also use the base form for lexicon look ups if the base word
	 * has not been set. Base forms on phrases are purely to aid debugging.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Phrases and words.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String BASE_FORM = "base_form";
	/**
	 * <p>
	 * This feature is used for determining the position of adjectives. Setting
	 * this value to true means that the adjective can occupy the
	 * <em>classifying</em> position.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>classifying</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any lexicon that supports adjective positioning.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor to determine the ordering of adjectives.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives within noun phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String CLASSIFYING = "classifying";
	/**
	 * <p>
	 * This feature is used for determining the position of adjectives. Setting
	 * this value to true means that the adjective can occupy the
	 * <em>colour</em> position.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>colour</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any lexicon that supports adjective positioning.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor to determine the ordering of adjectives.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives within noun phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String COLOUR = "colour";
	/**
	 * <p>
	 * This feature gives the comparative form for adjectives and adverbs. For
	 * example, <em>dizzier</em> is the comparative form of <em>dizzy</em>,
	 * <em>fatter</em> is the comparative form of <em>fat</em> and
	 * <em>earlier</em> is the comparative form of <em>early</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>comparative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Can be created automatically by the lexicon or added manually by
	 * users.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this information to correctly inflect
	 * words.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives and adverbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String COMPARATIVE = "comparative";
	/**
	 * <p>
	 * This feature determines if a verb is ditransitive, meaning that it can
	 * have a subject, direct object and indirect object. For example in the
	 * phrase <em>he gave Mary ten pounds</em>, the verb <em>give</em> has three
	 * components: the subject is the person doing the giving (<em>he</em>), the
	 * direct object is the object being passed (<em>ten pounds</em>) and the
	 * indirect object is the recipient (<em>Mary</em>).
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>ditransitive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The feature is set by the lexicon if it supports the recording of the
	 * transitive nature of verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The ditransitive value is currently not used.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String DITRANSITIVE = "ditransitive";
	/**
	 * <p>
	 * This feature determines whether a noun is masculine, feminine or neuter
	 * in nature.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>gender</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Gender</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory creates the gender of pronouns when creating
	 * phrases and on all nouns within a noun phrase.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor ensures that the head noun in a noun phrase has
	 * a gender matching that applied to the phrase as a whole. The morphology
	 * processor uses gender to determine the appropriate form for pronouns and
	 * for setting the form of some verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Specifically it applies to nouns and pronouns but the feature is also
	 * written to noun phrases and verbs.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Gender.NEUTER</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String GENDER = "gender";
	/**
	 * <p>
	 * This flag determines if an adverb is an intensifier, such as
	 * <em>very</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>intensifier</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The information is read from Lexicons that support this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>Currently not used.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adverbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String INTENSIFIER = "intensifier";
	/**
	 * <p>
	 * This flag highlights a verb that can only take a subject and no objects.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>intransitive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The information is read from Lexicons that support this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>Currently not used.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String INTRANSITIVE = "intransitive";

	/*
	 * <p> This feature represents non-countable nouns such as <em>mud</em>,
	 * <em>sand</em> and <em>water</em>. </p> <table border="1"> <tr>
	 * <td><b>Feature name</b></td> <td><em>nonCount</em></td> </tr> <tr>
	 * <td><b>Expected type</b></td> <td><code>Boolean</code></td> </tr> <tr>
	 * <td><b>Created by</b></td> <td>Supporting lexicons.</td> </tr> <tr>
	 * <td><b>Used by</b></td> <td>The morphology processor will not pluralise
	 * non-countable nouns.</td> </tr> <tr> <td><b>Applies to</b></td> <td>Nouns
	 * only.</td> </tr> <tr> <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td> </tr> </table>
	 */
	// public static final String NON_COUNT = "nonCount";

	/**
	 * <p>
	 * This feature gives the past tense form of a verb. For example, the past
	 * tense of <em>eat</em> is <em>ate</em>, the past tense of <em>walk</em> is
	 * <em>walked</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>past</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>All supporting lexicons but can be set by the user for irregular
	 * cases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this feature to correctly inflect
	 * verbs. This feature will be looked at first before any reference to
	 * lexicons or morphology rules.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs and verb phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PAST = "past";
	/**
	 * <p>
	 * This feature gives the past participle tense form of a verb. For many
	 * verbs the past participle is exactly the same as the past tense, for
	 * example, the verbs <em>talk</em>, <em>walk</em> and <em>say</em> have
	 * past tense and past participles of <em>talked</em>, <em>walked</em> and
	 * <em>said</em>. Contrast this with the verbs <em>do</em>, <em>eat</em> and
	 * <em>sing</em>. The past tense of these verbs is <em>did</em>,
	 * <em>ate</em> and <em>sang</em> respectively. while the respective past
	 * participles are <em>done</em>, <em>eaten</em> and <em>sung</em>
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>pastParticiple</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>All supporting lexicons but can be set by the user for irregular
	 * cases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this feature to correctly inflect
	 * verbs. This feature will be looked at first before any reference to
	 * lexicons or morphology rules.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs and verb phrases only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PAST_PARTICIPLE = "pastParticiple";
	/**
	 * <p>
	 * This feature gives the plural form of a noun. For example, the plural of
	 * <em>dog</em> is <em>dogs</em> and the plural of <em>sheep</em> is
	 * <em>sheep</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>plural</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>All supporting lexicons but can be set by the user for irregular
	 * cases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this feature to correctly inflect
	 * plural nouns. This feature will be looked at first before any reference
	 * to lexicons or morphology rules.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Nouns only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PLURAL = "plural";
	/**
	 * <p>
	 * This flag is set on adjectives that can also be used as a predicate. For
	 * example <em>happy</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>predicative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any supporting lexicon.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>Currently not used.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PREDICATIVE = "predicative";
	/**
	 * <p>
	 * This feature gives the present participle form of a verb. For example,
	 * the present participle form of <em>eat</em> is <em>eating</em> and the
	 * present participle form of <em>walk</em> is <em>walking</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>presentParticiple</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>All supporting lexicons but can be set by the user for irregular
	 * cases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this feature to correctly inflect
	 * verbs. This feature will be looked at first before any reference to
	 * lexicons or morphology rules.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PRESENT_PARTICIPLE = "presentParticiple";
	/**
	 * <p>
	 * This feature gives the present third person singular form of a verb. For
	 * example, the present participle form of <em>eat</em> is <em>eats</em> as
	 * in <em>the dog eats</em>. Another example is <em>ran</em> being the
	 * present third person singular form of <em>run</em> as in
	 * <em>John ran home</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>present3s</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>All supporting lexicons but can be set by the user for irregular
	 * cases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this feature to correctly inflect
	 * verbs. This feature will be looked at first before any reference to
	 * lexicons or morphology rules.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PRESENT3S = "present3s";
	/**
	 * <p>
	 * This flag is used to determine whether a noun is a proper noun, such as a
	 * person's name.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>proper</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Can be set by supporting lexicons or by the user.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor will not pluralise proper nouns.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Nouns only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String PROPER = "proper";
	/**
	 * <p>
	 * This feature is used for determining the position of adjectives. Setting
	 * this value to true means that the adjective can occupy the
	 * <em>qualitative</em> position.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>qualitative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any lexicon that supports adjective positioning.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The syntax processor to determine the ordering of adjectives.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives within noun phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String QUALITATIVE = "qualitative";
	/**
	 * <p>
	 * This flag is set if a pronoun is written in the reflexive form. For
	 * example, <em>myself</em>, <em>yourself</em>, <em>ourselves</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isReflexive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>The phrase factory will recognise personal pronouns in reflexive
	 * form.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor will correctly inflect reflexive pronouns.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Pronouns only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String REFLEXIVE = "reflexive";
	/**
	 * <p>
	 * This feature is used to define whether an adverb can be used as a clause
	 * modifier, which are normally applied at the beginning of clauses. For
	 * example, <em>unfortunately</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>sentenceModifier</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any lexicon that supports this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>generic addModifier methods, to decide where to put an adverb</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adverbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String SENTENCE_MODIFIER = "sentence_modifier";
	/**
	 * <p>
	 * This feature gives the superlative form for adjectives and adverbs. For
	 * example, <em>fattest</em> is the superlative form of <em>fat</em> and
	 * <em>earliest</em> is the superlative form of <em>early</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>superlative</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>String</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Can be created automatically by the lexicon or added manually by
	 * users.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>The morphology processor uses this information to correctly inflect
	 * words.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adjectives and adverbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>null</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String SUPERLATIVE = "superlative";
	/**
	 * <p>
	 * This flag highlights a verb that can only take a subject and an object.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>transitive</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any lexicon supporting this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>Currently not used.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Verbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code>.</td>
	 * </tr>
	 * </table>
	 */
	public static final String TRANSITIVE = "transitive";

	/**
	 * <p>
	 * This feature is used to define whether an adverb can be used as a verb
	 * modifier, which are normally added in a phrase before the verb itself.
	 * For example, <em>quickly</em>.
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>verbModifier</em></td>
	 * </tr>
	 * <tr>
	 * <td><b>Expected type</b></td>
	 * <td><code>Boolean</code></td>
	 * </tr>
	 * <tr>
	 * <td><b>Created by</b></td>
	 * <td>Any lexicon that supports this feature.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Used by</b></td>
	 * <td>generic addModifier methods, to decide where to put an adverb.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Adverbs only.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String VERB_MODIFIER = "verb_modifier";

	/**
	 * <p>
	 * This feature determines if the pronoun is an expletive or not. Expletive
	 * pronouns are usually <em>it</em> or <em>there</em> in sentences such as:<br>
	 * <em><b>It</b> is raining now.</em><br>
	 * <em><b>There</b> are ten desks in the room.</em>
	 * </p>
	 * <table border="1">
	 * <tr>
	 * <td><b>Feature name</b></td>
	 * <td><em>isExpletive</em></td>
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
	 * <td>The syntax processor uses the expletive on verb phrases for
	 * determining the correct number agreement.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Applies to</b></td>
	 * <td>Certain pronouns when used as subjects of verb phrases.</td>
	 * </tr>
	 * <tr>
	 * <td><b>Default</b></td>
	 * <td><code>Boolean.FALSE</code></td>
	 * </tr>
	 * </table>
	 */
	public static final String EXPLETIVE_SUBJECT = "expletive_subject";

	/**
	 * The constructor is never needed.
	 */
	private LexicalFeature() {
		// do nothing
	}

	/**
	 * Return those features related to a word's inflection, depending on its
	 * category, that is, the constants for
	 * <code>PAST, PAST_PARTICIPLE, PLURAl, PRESENT_PARTICIPLE, PRESENT3S, COMPARATIVE</code>
	 * or <code>SUPERLATIVE</code>.
	 * 
	 * @param cat
	 *            the category
	 * @return the inflectional feature names
	 */
	public static String[] getInflectionalFeatures(ElementCategory cat) {
		if (PhraseCategory.NOUN_PHRASE.equals(cat)
				|| LexicalCategory.NOUN.equals(cat))
			return new String[] { PLURAL };
		else if (PhraseCategory.VERB_PHRASE.equals(cat)
				|| LexicalCategory.VERB.equals(cat))
			return new String[] { PAST, PAST_PARTICIPLE, PRESENT_PARTICIPLE,
					PRESENT3S };
		else if (PhraseCategory.ADJECTIVE_PHRASE.equals(cat)
				|| LexicalCategory.ADJECTIVE.equals(cat))
			return new String[] { COMPARATIVE, SUPERLATIVE };
		else
			return null;

	}
}
