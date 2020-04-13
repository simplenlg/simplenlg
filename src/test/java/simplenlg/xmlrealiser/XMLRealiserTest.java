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
package simplenlg.xmlrealiser;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import simplenlg.util.TestUtility;
import simplenlg.xmlrealiser.XMLRealiser.LexiconType;
import simplenlg.xmlrealiser.wrapper.DocumentRealisation;
import simplenlg.xmlrealiser.wrapper.RecordSet;
import simplenlg.xmlrealiser.wrapper.RequestType;
import simplenlg.xmlrealiser.wrapper.XmlDocumentElement;

/**
 * {@link XMLRealiserTest} is a JUnit test class to test different test cases for the {@link XMLRealiser}.
 * @author Saad Mahamood.
 */
public class XMLRealiserTest {

	private TestUtility testUtility;

	@Before
	public void setup() throws Exception {
		testUtility = new TestUtility();

		String lexDB = testUtility.getResourceFilePath("NIHLexicon/lexAccess2013.data");
		LexiconType lexType = LexiconType.NIHDB; // Some tests require this.
		XMLRealiser.setLexicon(lexType, lexDB);
	}

	/**
	 * fetchRecordings -- Fetches each <Recording> XML element from a given {@link String} object.
	 *
	 * @param recording -- The {@link String} XML recording text.
	 * @return A {@link java.util.Collection} of {@link HashMap}s that contain XML Document to realise, the
	 * {@link String} expected output, and {@link String} test case name.
	 */
	private Collection<HashMap<String, Object>> fetchRecordings(String recording) {
		Collection<HashMap<String, Object>> tests = new ArrayList<>();
		RecordSet records = null;

		try(StringReader reader = new StringReader(recording)) {
			records = XMLRealiser.getRecording(reader);
		} catch(XMLRealiserException e) {
			Assert.fail("Error parsing test xml: " + " " + e.getMessage());
		}

		if(null != records) {
			for(DocumentRealisation test : records.getRecord()) {
				HashMap<String, Object> testCase = new HashMap<>();
				testCase.put("Document", test.getDocument());
				testCase.put("Expected", test.getRealisation().trim());
				testCase.put("Test", String.format("Test :<%s>", test.getName()));
				tests.add(testCase);
			}
		}

		return tests;
	}

	/**
	 * performRecordingTests -- Performs realisation using the {@link XMLRealiser} and returns the results in a {@link HashMap}.
	 *
	 * @param testCase : The {@link String} XML test case to realise.
	 * @return The {@link HashMap} containing {@link ArrayList<String>}s of realised {@link String}s and expected output
	 * {@link String}s.
	 * @throws XMLRealiserException -- Thrown if there is an error during realisation using the {@link XMLRealiser}.
	 */
	private HashMap<String, ArrayList<String>> performRecordingTests(String testCase) throws XMLRealiserException {
		HashMap<String, ArrayList<String>> testResults = new HashMap<>();
		testResults.put("Expected", new ArrayList<>());
		testResults.put("Realisation", new ArrayList<>());

		if(null != testCase && !testCase.isEmpty()) {
			Collection<HashMap<String, Object>> testRecordingsList = fetchRecordings(testCase);
			for(HashMap<String, Object> recordingTestCase : testRecordingsList) {
				System.out.println(recordingTestCase.get("Test"));
				String realisation = XMLRealiser.realise((XmlDocumentElement) recordingTestCase.get("Document"));
				String expectedResult = (String) recordingTestCase.get("Expected");

				if(!expectedResult.contains("*")) {
					// Whitespace handling for non-bullet points expected output Strings:
					expectedResult = expectedResult.replaceAll("\\s{2,}?", "");
				} else if(expectedResult.contains("*")) {
					// Whitespace handling for bullet points expected Strings:
					String[] bulletpointList = expectedResult.split("\\s{2,}?");
					//@formatter:off
					expectedResult = Arrays.stream(bulletpointList).filter(s -> !s.trim().isEmpty())
							.map(s -> s.trim() + "\n").collect(Collectors.joining());
					//@formatter:on
				}

				System.out.println("Expected: " + expectedResult);
				System.out.println("Realisation: " + realisation);

				testResults.get("Expected").add(expectedResult.trim());
				testResults.get("Realisation").add(realisation.trim());
			}
		}

		return testResults;
	}

	/**
	 * Appositive Clause Test -- Tests the generate of a simple Appositive Clause using the XMLRealiser.
	 */
	@Test
	public void xmlAppositiveClauseTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/AppositiveTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Multi Sentence -- Tests the generate of multiple sentences using the XMLRealiser.
	 */
	@Test
	public void xmlMultiSentenceTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/MultiSentenceTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Multi Clause Test -- Tests the generate of multiple clauses using the XMLRealiser.
	 */
	@Test
	public void xmlMultiClauseTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/ClauseTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Coordinated Phrase Negation Test -- Tests the generate of negated coordinated phrases using the XMLRealiser.
	 */
	@Test
	public void xmlCoordPhraseNegationTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/CoordPhraseNegationTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Coordinated Phrase Test -- Tests the generate of coordinated phrases using the XMLRealiser.
	 */
	@Test
	public void xmlCoordPhraseTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/CoordPhraseTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Multiple Documents Test -- Tests the generate of multiple documents using the XMLRealiser.
	 */
	@Test
	public void xmlMultipleDocumentsTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/MultiDocumentTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Lexical Variation Test -- Tests the generate of lexical variation using the XMLRealiser.
	 */
	@Test
	public void xmlLexicalVariationTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/LexicalVariationTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * NounPhrase Test -- Tests the generate of noun phrases using the XMLRealiser.
	 */
	@Test
	public void xmlNounPhraseTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/NounPhraseTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Interrogative Test -- Tests the generation of an Interrogative using the XML Realiser
	 */
	@Test
	public void xmlInterrogativeTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/InterrogativeTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Formatting Test -- Test the generation of a document that uses text formatting using the XML Realiser
	 */
	@Test
	public void xmlFormattingTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/FormattingTest.xml");
		HashMap<String, ArrayList<String>> testResults = performRecordingTests(testCase);

		Assert.assertArrayEquals(testResults.get("Expected").toArray(), testResults.get("Realisation").toArray());
	}

	/**
	 * Perfect Passive Test -- Test the generation of a perfect passive sentence using the XML Realiser
	 */
	@Test
	public void xmlPerfectPassiveTest() throws XMLRealiserException, IOException, URISyntaxException {
		String testCase = testUtility.getResourceFileAsString("XMLRealiserTest/PerfectPassiveTest.xml");
		RequestType request = XMLRealiser.getRequest(new StringReader(testCase));
		String output = XMLRealiser.realise(request.getDocument());

		Assert.assertEquals("The man has been upset by the child.", output);
	}
}
