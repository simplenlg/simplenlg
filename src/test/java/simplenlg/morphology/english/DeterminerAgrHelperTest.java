/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * https://www.mozilla.org/en-US/MPL/
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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, and Saad Mahamood.
 */
package simplenlg.morphology.english;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeterminerAgrHelperTest {

	@Test
	public void testRequiresAn() {

		assertTrue(DeterminerAgrHelper.requiresAn("elephant"));

		assertFalse(DeterminerAgrHelper.requiresAn("cow"));

		// Does not hand phonetics
		assertFalse(DeterminerAgrHelper.requiresAn("hour"));

		// But does have exceptions for some numerals
		assertFalse(DeterminerAgrHelper.requiresAn("one"));

		assertFalse(DeterminerAgrHelper.requiresAn("100"));

	}

	@Test
	public void testCheckEndsWithIndefiniteArticle1() {

		String cannedText = "I see a";

		String np = "elephant";

		String expected = "I see an";

		String actual = DeterminerAgrHelper.checkEndsWithIndefiniteArticle(cannedText, np);

		assertEquals(expected, actual);

	}

	@Test
	public void testCheckEndsWithIndefiniteArticle2() {

		String cannedText = "I see a";

		String np = "cow";

		String expected = "I see a";

		String actual = DeterminerAgrHelper.checkEndsWithIndefiniteArticle(cannedText, np);

		assertEquals(expected, actual);

	}

	@Test
	public void testCheckEndsWithIndefiniteArticle3() {

		String cannedText = "I see an";

		String np = "cow";

		// Does not handle "an" -> "a"
		String expected = "I see an";

		String actual = DeterminerAgrHelper.checkEndsWithIndefiniteArticle(cannedText, np);

		assertEquals(expected, actual);

	}

}
