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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell.
 */

package simplenlg.features;

/**
 * <p>
 * An enumeration representing the different types of morphology patterns used
 * by the basic morphology processor included with SimpleNLG. This enumeration
 * is a way of informing the morphology processor which set of rules should be
 * used when inflecting the word.
 * </p>
 * <p>
 * The pattern is recorded in the {@code Feature.PATTERN} feature and applies to
 * adjectives, nouns and verbs.
 * </p>
 * <p>
 * It should be noted that the morphology processor will use user-defined
 * inflections or those found in a lexicon first before applying the supplied
 * rules.
 * </p>
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */

public enum Inflection {

	/**
	 * The morphology processor has simple rules for pluralising Greek and Latin
	 * nouns. The full list can be found in the explanation of the morphology
	 * processor. An example would be turning <em>focus</em> into <em>foci</em>.
	 * The Greco-Latin rules are generally more complex than the basic rules.
	 */
	GRECO_LATIN_REGULAR,

	/**
	 * A word having an irregular pattern essentially means that none of the
	 * supplied rules can be used to correctly inflect the word. The inflection
	 * should be defined by the user or appear in the lexicon. <em>sheep</em> is
	 * an example of an irregular noun.
	 */
	IRREGULAR,

	/**
	 * Regular patterns represent the default rules when dealing with
	 * inflections. A full list can be found in the explanation of the
	 * morphology processor. An example would be adding <em>-s</em> to the end
	 * of regular nouns to pluralise them.
	 */
	REGULAR,

	/**
	 * Regular double patterns apply to verbs where the last consonant is
	 * duplicated before applying the new suffix. For example, the verb
	 * <em>tag</em> has a regular double pattern as its inflected forms include
	 * <em>tagged</em> and <em>tagging</em>.
	 */
	REGULAR_DOUBLE,

	/**
	 * The value for uncountable nouns, which are not inflected in their plural
	 * form.
	 */
	UNCOUNT,

	/**
	 * The value for words which are invariant, that is, are never inflected.
	 */
	INVARIANT;

	/**
	 * convenience method: parses an inflectional code such as
	 * "irreg|woman|women" to retrieve the first element, which is the code
	 * itself, then maps it to the value of <code>Inflection</code>.
	 * 
	 * @param code
	 *            -- the string representing the inflection. The strings are
	 *            those defined in the NIH Lexicon.
	 * @return the Inflection
	 */
	public static Inflection getInflCode(String code) {
		code = code.toLowerCase().trim();
		Inflection infl = null;

		if (code.equals("reg")) {
			infl = Inflection.REGULAR;
		} else if (code.equals("irreg")) {
			infl = Inflection.IRREGULAR;
		} else if (code.equals("regd")) {
			infl = Inflection.REGULAR_DOUBLE;
		} else if (code.equals("glreg")) {
			infl = Inflection.GRECO_LATIN_REGULAR;
		} else if (code.equals("uncount") || code.equals("noncount")
				|| code.equals("groupuncount")) {
			infl = Inflection.UNCOUNT;
		} else if (code.equals("inv")) {
			infl = Inflection.INVARIANT;
		}

		return infl;
	}

}
