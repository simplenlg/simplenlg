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
package simplenlg.framework;

/**
 * <p>
 * This enumeration defines the different lexical components. The categories
 * define the well understood role each word takes in language. For example,
 * <em>dog</em> is a noun, <em>chase</em> is a verb, <em>the</em> is a
 * determiner, and so on.
 * </p>
 * 
 * 
 * @author A. Gatt and D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public enum LexicalCategory implements ElementCategory {

	/** A default value, indicating an unspecified category. */
	ANY,

	/** The element represents a symbol. */
	SYMBOL,

	/** A noun element. */
	NOUN,

	/** An adjective element. */
	ADJECTIVE,

	/** An adverb element. */
	ADVERB,

	/** A verb element. */
	VERB,

	/** A determiner element often referred to as a specifier. */
	DETERMINER,

	/** A pronoun element. */
	PRONOUN,

	/** A conjunction element. */
	CONJUNCTION,

	/** A preposition element. */
	PREPOSITION,

	/** A complementiser element. */
	COMPLEMENTISER,

	/** A modal element. */
	MODAL,

	/** An auxiliary verb element. */
	AUXILIARY;

	/**
	 * <p>
	 * Checks to see if the given object is equal to this lexical category.
	 * This is done by checking the enumeration if the object is of the type
	 * <code>LexicalCategory</code> or by converting the object and this
	 * category to strings and comparing the strings.
	 * </p>
	 * <p>
	 * For example, <code>LexicalCategory.NOUN</code> will match another
	 * <code>LexicalCategory.NOUN</code> but will also match the string
	 * <em>"noun"</em> as well.
	 */
	public boolean equalTo(Object checkObject) {
		boolean match = false;

		if (checkObject != null) {
			if (checkObject instanceof DocumentCategory) {
				match = this.equals(checkObject);
			} else {
				match = this.toString()
						.equalsIgnoreCase(checkObject.toString());
			}
		}
		return match;
	}
}
