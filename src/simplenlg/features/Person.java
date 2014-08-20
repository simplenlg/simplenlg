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
 * This is an enumeration used to represent the point of view of the narrative.
 * It covers first person, second person and third person. The person is
 * recorded in the {@code Feature.PERSON} feature and applies to clauses,
 * coordinated phrases, noun phrases and verb phrases.
 * </p>
 * 
 * @author A. Gatt, D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */

public enum Person {

	/**
	 * The enumeration to show that the narration is written in the first
	 * person. First person narrative uses the personal pronouns of <em>I</em>
	 * and <em>we</em>.
	 */
	FIRST, 

	/**
	 * The enumeration to show that the narration is written in the second
	 * person. Second person narrative uses the personal pronoun of <em>you</em>.
	 */
	SECOND, 

	/**
	 * The enumeration to show that the narration is written in the third
	 * person. Third person narrative uses the personal pronouns of <em>he</em>, 
	 * <em>her</em> and <em>they</em>.
	 */
	THIRD;
}
