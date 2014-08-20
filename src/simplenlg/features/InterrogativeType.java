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
 * An enumeration representing the different types of interrogatives or
 * questions that SimpleNLG can realise. The interrogative type is recorded in
 * the {@code Feature.INTERROGATIVE_TYPE} feature and applies to clauses.
 * </p>
 * 
 * @author A. Gatt and D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */

public enum InterrogativeType {

	/**
	 * The type of interrogative relating to the manner in which an event
	 * happened. For example, <em>John kissed Mary</em> becomes
	 * <em>How did John kiss
	 * Mary?</em>
	 */
	HOW,
	
	/**
	 * A how question related to a predicative sentence, such as <i>John is fine</i>, which becomes <i>How is John?</i> 
	 */
	HOW_PREDICATE,

	/**
	 * This type of interrogative is a question pertaining to the object of a
	 * phrase. For example, <em>John bought a horse</em> becomes <em>what did 
	 * John buy?</em> while <em>John gave Mary a flower</em> becomes
	 * <em>What did 
	 * John give Mary?</em>
	 */
	WHAT_OBJECT,

	/**
	 * This type of interrogative is a question pertaining to the subject of a
	 * phrase. For example, <em>A hurricane destroyed the house</em> becomes
	 * <em>what destroyed the house?</em>
	 */
	WHAT_SUBJECT,

	/**
	 * This type of interrogative concerns the object of a verb that is to do
	 * with location. For example, <em>John went to the beach</em> becomes
	 * <em>Where did John go?</em>
	 */
	WHERE,

	/**
	 * This type of interrogative is a question pertaining to the indirect
	 * object of a phrase when the indirect object is a person. For example,
	 * <em>John gave Mary a flower</em> becomes
	 * <em>Who did John give a flower to?</em>
	 */
	WHO_INDIRECT_OBJECT,

	/**
	 * This type of interrogative is a question pertaining to the object of a
	 * phrase when the object is a person. For example,
	 * <em>John kissed Mary</em> becomes <em>who did John kiss?</em>
	 */
	WHO_OBJECT,

	/**
	 * This type of interrogative is a question pertaining to the subject of a
	 * phrase when the subject is a person. For example,
	 * <em>John kissed Mary</em> becomes <em>Who kissed Mary?</em> while
	 * <em>John gave Mary a flower</em> becomes <em>Who gave Mary a flower?</em>
	 */
	WHO_SUBJECT,

	/**
	 * The type of interrogative relating to the reason for an event happening.
	 * For example, <em>John kissed Mary</em> becomes <em>Why did John kiss
	 * Mary?</em>
	 */
	WHY,

	/**
	 * This represents a simple yes/no questions. So taking the example phrases
	 * of <em>John is a professor</em> and <em>John kissed Mary</em> we can
	 * construct the questions <em>Is John a professor?</em> and
	 * <em>Did John kiss Mary?</em>
	 */
	YES_NO,

	/**
	 * This represents a "how many" questions. For example of
	 * <em>dogs chased John/em> becomes <em>How many dogs chased John</em>
	 */
	HOW_MANY;

	/**
	 * A method to determine if the {@code InterrogativeType} is a question
	 * concerning an element with the discourse function of an object.
	 * 
	 * @param type
	 *            the interrogative type to be checked
	 * @return <code>true</code> if the type concerns an object,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isObject(Object type) {
		return WHO_OBJECT.equals(type) || WHAT_OBJECT.equals(type);
	}

	/**
	 * A method to determine if the {@code InterrogativeType} is a question
	 * concerning an element with the discourse function of an indirect object.
	 * 
	 * @param type
	 *            the interrogative type to be checked
	 * @return <code>true</code> if the type concerns an indirect object,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isIndirectObject(Object type) {
		return WHO_INDIRECT_OBJECT.equals(type);
	}

	/**
	 * Convenience method to return the String corresponding to the question
	 * word. Useful, since the types in this enum aren't all simply convertible
	 * to strings (e.g. <code>WHO_SUBJCT</code> and <code>WHO_OBJECT</code> both
	 * correspond to String <i>Who</i>)
	 * 
	 * @return the string corresponding to the question word
	 */
	public String getString() {
		String s = "";

		switch (this) {
		case HOW:
		case HOW_PREDICATE:
			s = "how";
			break;
		case WHAT_OBJECT:
		case WHAT_SUBJECT:
			s = "what";
			break;
		case WHERE:
			s = "where";
			break;
		case WHO_INDIRECT_OBJECT:
		case WHO_OBJECT:
		case WHO_SUBJECT:
			s = "who";
			break;
		case WHY:
			s = "why";
			break;
		case HOW_MANY:
			s = "how many";
			break;
		case YES_NO:
			s = "yes/no";
			break;
		}

		return s;
	}
}
