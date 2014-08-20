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
package simplenlg.syntax.english;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite containing all tests for syntax and inflection
 * @author agatt
 * 
 */
public class SyntaxTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for simplenlg.test"); //$NON-NLS-1$
		//$JUnit-BEGIN$
		suite.addTestSuite(AdjectivePhraseTest.class);
		suite.addTestSuite(ClauseAggregationTest.class);
		//suite.addTestSuite(ClauseAggregationTest2.class);
		suite.addTestSuite(ClauseTest.class);
		suite.addTestSuite(CoordinationTest.class);
		suite.addTestSuite(DeterminerTest.class);
		suite.addTestSuite(DocumentElementTest.class);
		suite.addTestSuite(ElisionTests.class);
		suite.addTestSuite(ExternalTest.class);
		suite.addTestSuite(ExternalTests2.class);
		suite.addTestSuite(FPTest.class);
		suite.addTestSuite(InterrogativeTest.class);
		suite.addTestSuite(NounPhraseTest.class);
		suite.addTestSuite(OrthographyFormatTests.class);
		suite.addTestSuite(PhraseSpecTest.class);
		suite.addTestSuite(PrepositionalPhraseTest.class);
		suite.addTestSuite(StringElementTests.class);
		suite.addTestSuite(VerbPhraseTest.class);
		//$JUnit-END$
		return suite;
	}

}
