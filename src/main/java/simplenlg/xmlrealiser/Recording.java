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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import simplenlg.xmlrealiser.wrapper.DocumentRealisation;
import simplenlg.xmlrealiser.wrapper.NLGSpec;
import simplenlg.xmlrealiser.wrapper.RecordSet;

/**
 * A recording is a utility class that holds xml objects for testing the
 * xmlrealiser.
 * 
 * @author Christopher Howell Agfa Healthcare Corporation
 * @author Albert Gatt, University of Malta
 * 
 */
public class Recording {

	/** The recording on. */
	boolean recordingOn = false;

	/** The recording folder. */
	String recordingFolder;

	/** The record. */
	RecordSet record = null;

	/** The recording file. */
	File recordingFile;

	/**
	 * Instantiate a Recording from an XML file. Recordings can contain multiple
	 * records, each of which represents a single element to be realised.
	 * 
	 * @param directoryPath
	 *            the path to the file
	 */
	public Recording(String directoryPath) {
		recordingFolder = directoryPath;
	}

	/**
	 * Recording on.
	 * 
	 * @return true, if successful
	 */
	public boolean RecordingOn() {
		return recordingOn;
	}

	/**
	 * Gets the recording file.
	 * 
	 * @return the string
	 */
	public String GetRecordingFile() {
		if (recordingOn)
			return recordingFile.getAbsolutePath();
		else
			return "";
	}

	/**
	 * Start.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void start() throws IOException {

		if (recordingFolder.isEmpty() || recordingOn) {
			return;
		}

		File recordingDir = new File(recordingFolder);
		if (!recordingDir.exists()) {
			boolean ok = recordingDir.mkdirs();
			if (!ok) {
				return;
			}

			recordingFile = File.createTempFile("xmlrealiser", ".xml",
					recordingDir);
			recordingOn = true;
			record = new RecordSet();
		}
	}

	/**
	 * Adds a record to this recording.
	 * 
	 * @param input
	 *            the DocumentElement in this record
	 * @param output
	 *            the realisation
	 */
	public void addRecord(simplenlg.xmlrealiser.wrapper.XmlDocumentElement input,
			String output) {
		if (!recordingOn) {
			return;
		}
		DocumentRealisation t = new DocumentRealisation();
		Integer testNumber = record.getRecord().size() + 1;
		String testName = "TEST_" + testNumber.toString();
		t.setName(testName);
		t.setDocument(input);
		t.setRealisation(output);
		record.getRecord().add(t);
	}

	/**
	 * Ends processing for this recording and writes it to an XML file.
	 * 
	 * @throws JAXBException
	 *             the jAXB exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerException
	 *             the transformer exception
	 */
	public void finish() throws JAXBException, IOException,
			TransformerException {
		if (!recordingOn) {
			return;
		}

		recordingOn = false;
		FileOutputStream os = new FileOutputStream(recordingFile);
		os.getChannel().truncate(0);
		writeRecording(record, os);
	}

	/**
	 * Write recording.
	 * 
	 * @param record
	 *            the record
	 * @param os
	 *            the os
	 * @throws JAXBException
	 *             the jAXB exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerException
	 *             the transformer exception
	 */
	public static void writeRecording(RecordSet record, OutputStream os)
			throws JAXBException, IOException, TransformerException {
		JAXBContext jc;
		jc = JAXBContext
				.newInstance(simplenlg.xmlrealiser.wrapper.NLGSpec.class);
		Marshaller m = jc.createMarshaller();

		NLGSpec nlg = new NLGSpec();
		nlg.setRecording(record);

		StringWriter osTemp = new StringWriter();
		m.marshal(nlg, osTemp);

		// Prettify it.
		Source xmlInput = new StreamSource(new StringReader(osTemp.toString()));
		StreamResult xmlOutput = new StreamResult(new OutputStreamWriter(os,
				"UTF-8"));
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		if (transformer != null) {
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(xmlInput, xmlOutput);
		}
	}
}