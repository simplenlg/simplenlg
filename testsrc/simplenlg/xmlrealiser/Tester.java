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
package simplenlg.xmlrealiser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import simplenlg.xmlrealiser.XMLRealiser.LexiconType;
import simplenlg.xmlrealiser.wrapper.DocumentRealisation;
import simplenlg.xmlrealiser.wrapper.RecordSet;

/**
 * This class is intended for regression testing of the xmlrealiser framework.
 * It works by accepting xml files (representing the test cases) and a path to
 * the lexicon DB file to use, and instantiating an <code>XMLRealiser</code> to map
 * the XML to simplenlg objects. It outputs the results in an XML file, named
 * like the input file (with the suffix <i>out</i>), in which the realisation
 * has been appended to each test case.
 * Test files can be created by recording the activity of the xmlrealizer.
 * 
 * @author Christopher Howell, Agfa Healthcare Corporation
 */

@RunWith(Parameterized.class)
public class Tester {

	private File ThisTestFile;

	/**
	 * Read env variable to get path to NIH DB. Read path to single test file.
	 * if empty or "none" read path to directory containing *Test.xml files.
	 */
	@BeforeClass
	public static void setup() {
		String lexDB = System.getenv("LexDBPath");
		LexiconType lexType = LexiconType.NIHDB;

		XMLRealiser.setLexicon(lexType, lexDB);

	}
	
	@AfterClass
	public static void teardown() {
		XMLRealiser.setLexicon(null, null);
	}

	@Parameters
	public static Collection<File[]> TestFileNames() {
		Collection<File[]> allTestFiles;
		FilenameFilter filter = new TestFilenameFilter();
		String testsPath = System.getenv("TestFilePath");
		File path = new File(testsPath);
		if (path.isDirectory()) {
			allTestFiles = listFiles(path, filter, true);
		} else {
			allTestFiles = new Vector<File[]>();
			allTestFiles.add(new File[] {path});
		}
		return allTestFiles;
	}

	public Tester(File fileForTest) {
		this.ThisTestFile = fileForTest;
	}

	@Test
	public void testXmlFile() {
		RecordSet input = null;

		try {

			FileReader reader = new FileReader(ThisTestFile);
			input = XMLRealiser.getRecording(reader);
			reader.close();
		} catch (XMLRealiserException e) {
			Assert.fail("Error parsing test document: " + ThisTestFile.getName() + " "+ e.getMessage());
		} catch (FileNotFoundException e) {
			Assert.fail("Error opening test document: " + ThisTestFile.getName() + " "+ e.getMessage());
		} catch (IOException e) {
			Assert.fail("IOException: " + ThisTestFile.getName() + " "+ e.getMessage());
		}

		String recordingName = input.getName();
		for (DocumentRealisation test : input.getRecord()) {
			String testName = test.getName();
			String realisation = null;
			try {
				realisation = XMLRealiser.realise(test.getDocument());
			} catch (XMLRealiserException e) {
				e.printStackTrace();
				Assert.fail("Error parsing test document: " + ThisTestFile.getName());
			}
			
			// construct message to go with test failure.
			String failureMessage = new String();
			failureMessage = String.format("Test file:<%s>", ThisTestFile.getName());
			if (recordingName != null &&!recordingName.isEmpty())
			{
				failureMessage += " Recording:<" + recordingName + ">";
			}
			if (testName != null && !testName.isEmpty())
			{
				failureMessage += " Test:<" + testName + ">";
			}
			
			Assert.assertEquals(failureMessage, test.getRealisation(), realisation);
		}
	}

	/**
	 * List files.
	 * 
	 * @param directory
	 *            the directory
	 * @param filter
	 *            the filter
	 * @param recurse
	 *            the recurse
	 * @return the collection
	 */
	public static Collection<File[]> listFiles(
	// Java4: public static Collection listFiles(
			File directory, FilenameFilter filter, boolean recurse) {
		// List of files / directories
		Vector<File[]> files = new Vector<File[]>();
		// Java4: Vector files = new Vector();

		// Get files / directories in the directory
		File[] entries = directory.listFiles();

		// Go over entries
		for (File entry : entries) {
			// Java4: for (int f = 0; f < files.length; f++) {
			// Java4: File entry = (File) files[f];

			// If there is no filter or the filter accepts the
			// file / directory, add it to the list
			if (filter == null || filter.accept(directory, entry.getName())) {
				File[] fileAsArray = new File[] { entry };
				files.add(fileAsArray);
			}

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, filter, recurse));
			}
		}

		// Return collection of files
		return files;
	}
}

@Ignore
class TestFilenameFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith("Test.xml"))
			return true;
		else
			return false;
	}
}
