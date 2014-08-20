/**
 * 
 */
package simplenlg.xmlrealiser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;

/**
 * The Class XMLRealiser.
 * 
 * @author Christopher Howell Agfa Healthcare Corporation
 * @author Albert Gatt, University of Malta
 */
public class XMLRealiser {

	/** The lex db. */
	static String lexDB = null;

	/** The lexicon. */
	static Lexicon lexicon = null;

	/** The lexicon type. */
	static LexiconType lexiconType = null;

	/** The record. */
	static Recording record = null;

	/**
	 * The Enum OpCode.
	 * 
	 */
	/*
	 * The arg[0] is the op code. op codes are "realise", "setLexicon",
	 * "startRecording", "stopRecording" Usage is: realize <xml string> returns
	 * realised string. setLexicon (XML | NIHDB) <path to lexicon> returns "OK"
	 * or not. startRecording <path to recording directory> returns "OK" or not.
	 * stopRecording returns name of file which contains recording.
	 * Recordings can be used as regression tests. See simplenlg/test/xmlrealiser/Tester.java
	 */
	public enum OpCode {

		/** The noop. */
		noop,
		/** The realise. */
		realise,
		/** The set lexicon. */
		setLexicon,
		/** The start recording. */
		startRecording,
		/** The stop recording. */
		stopRecording
	}

	/**
	 * The Enum LexiconType.
	 */
	public enum LexiconType {

		/** The DEFAULT. */
		DEFAULT,
		/** The XML. */
		XML,
		/** The NIHDB. */
		NIHDB
	}

	/**
	 * The main method to perform realisation.
	 * 
	 * @param args
	 *            the args
	 * @return the string
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static String main(Object[] args) throws XMLRealiserException {

		if (args == null || args.length == 0) {
			throw new XMLRealiserException("invalid args");
		}
		int argx = 0;
		String input = "";
		String output = "OK";
		String opCodeStr = (String) args[argx++];
		OpCode opCode;
		try {
			opCode = Enum.valueOf(OpCode.class, opCodeStr);
		} catch (IllegalArgumentException ex) {
			throw new XMLRealiserException("invalid args");
		}
		switch (opCode) {
		case realise:
			if (args.length <= argx) {
				throw new XMLRealiserException("invalid args");
			}
			input = (String) args[argx++];
			StringReader reader = new StringReader(input);
			simplenlg.xmlrealiser.wrapper.RequestType request = getRequest(reader);
			output = realise(request.getDocument());

			break;
		case setLexicon: {
			if (args.length <= argx + 1) {
				throw new XMLRealiserException("invalid setLexicon args");
			}
			String lexTypeStr = (String) args[argx++];
			String lexFile = (String) args[argx++];
			LexiconType lexType;

			try {
				lexType = Enum.valueOf(LexiconType.class, lexTypeStr);
			} catch (IllegalArgumentException ex) {
				throw new XMLRealiserException("invalid args");
			}

			setLexicon(lexType, lexFile);
			break;
		}
		case startRecording: {
			if (args.length <= argx) {
				throw new XMLRealiserException("invalid args");
			}
			String path = (String) args[argx++];
			startRecording(path);
			break;
		}
		case stopRecording:
			if (record != null) {
				output = record.GetRecordingFile();
				try {
					record.finish();
				} catch (Exception e) {
					throw new XMLRealiserException("xml writing error "
							+ e.getMessage());
				}
			}
			break;
		case noop:
			break;
		default:
			throw new XMLRealiserException("invalid op code " + opCodeStr);
		}

		if (opCode == OpCode.realise) {
		}

		return output;
	}

	/**
	 * Sets the lexicon.
	 * 
	 * @param lexType
	 *            the lex type
	 * @param lexFile
	 *            the lex file
	 */
	public static void setLexicon(LexiconType lexType, String lexFile) {
		if (lexiconType != null && lexicon != null && lexType == lexiconType) {
			return; // done already
		}

		if (lexicon != null) {
			lexicon.close();
			lexicon = null;
			lexiconType = null;
		}

		if (lexType == LexiconType.XML) {
			lexicon = new XMLLexicon(lexFile);
		} else if (lexType == LexiconType.NIHDB) {
			lexicon = new NIHDBLexicon(lexFile);
		} else if (lexType == LexiconType.DEFAULT) {
			lexicon = Lexicon.getDefaultLexicon();
		}

		lexiconType = lexType;
	}

	/**
	 * Gets the request.
	 * 
	 * @param input
	 *            the input
	 * @return the request
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static simplenlg.xmlrealiser.wrapper.RequestType getRequest(
			Reader input) throws XMLRealiserException {
		simplenlg.xmlrealiser.wrapper.NLGSpec spec = UnWrapper
				.getNLGSpec(input);
		simplenlg.xmlrealiser.wrapper.RequestType request = spec.getRequest();
		if (request == null) {
			throw new XMLRealiserException("Must have Request element");
		}

		return request;
	}

	/**
	 * Gets the recording.
	 * 
	 * @param input
	 *            the input
	 * @return the recording
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static simplenlg.xmlrealiser.wrapper.RecordSet getRecording(
			Reader input) throws XMLRealiserException {
		simplenlg.xmlrealiser.wrapper.NLGSpec spec = UnWrapper
				.getNLGSpec(input);
		simplenlg.xmlrealiser.wrapper.RecordSet recording = spec.getRecording();
		if (recording == null) {
			throw new XMLRealiserException("Must have Recording element");
		}

		return recording;

	}

	/**
	 * Realise.
	 * 
	 * @param wt
	 *            the wt
	 * @return the string
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static String realise(
			simplenlg.xmlrealiser.wrapper.XmlDocumentElement wt)
			throws XMLRealiserException {
		String output = "";
		if (wt != null) {
			try {
				if (lexicon == null) {
					lexicon = Lexicon.getDefaultLexicon();
				}
				UnWrapper w = new UnWrapper(lexicon);
				DocumentElement t = w.UnwrapDocumentElement(wt);
				if (t != null) {
					Realiser r = new Realiser(lexicon);
					r.initialise();

					NLGElement tr = r.realise(t);

					output = tr.getRealisation();
				}

			} catch (Exception e) {
				throw new XMLRealiserException("NLG XMLRealiser Error", e);
			}
		}

		return output;
	}

	/**
	 * Start recording.
	 * 
	 * @param path
	 *            the path
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static void startRecording(String path) throws XMLRealiserException {
		if (record != null) {
			try {
				record.finish();
			} catch (Exception e) {
				throw new XMLRealiserException("NLG XMLRealiser Error", e);
			}
		}
		record = new Recording(path);
		try {
			record.start();
		} catch (IOException e) {
			throw new XMLRealiserException("NLG XMLRealiser Error", e);
		}
	}

	/**
	 * Stop recording.
	 * 
	 * @return the string
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static String stopRecording() throws XMLRealiserException {
		String file = "";
		if (record != null) {
			file = record.GetRecordingFile();
			try {
				record.finish();
			} catch (Exception e) {
				throw new XMLRealiserException("NLG XMLRealiser Error", e);
			}
		}

		return file;
	}
}
