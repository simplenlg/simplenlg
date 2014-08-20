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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.lexicon;

import gov.nih.nlm.nls.lexAccess.Api.LexAccessApi;
import gov.nih.nlm.nls.lexAccess.Api.LexAccessApiResult;
import gov.nih.nlm.nls.lexCheck.Lib.AdjEntry;
import gov.nih.nlm.nls.lexCheck.Lib.AdvEntry;
import gov.nih.nlm.nls.lexCheck.Lib.InflVar;
import gov.nih.nlm.nls.lexCheck.Lib.LexRecord;
import gov.nih.nlm.nls.lexCheck.Lib.NounEntry;
import gov.nih.nlm.nls.lexCheck.Lib.VerbEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simplenlg.features.Inflection;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.WordElement;

/**
 * This class gets Words from the NIH Specialist Lexicon
 * 
 * @author ereiter
 * 
 */
public class NIHDBLexicon extends Lexicon {

	// default DB parameters
	private static String DB_HSQL_DRIVER = "org.hsqldb.jdbc.JDBCDriver"; // DB driver
	private static String DB_HQSL_JDBC = "jdbc:hsqldb:"; // JDBC specifier for
	// HSQL
	private static String DB_DEFAULT_USERNAME = "sa"; // DB username
	private static String DB_DEFAULT_PASSWORD = ""; // DB password
	private static String DB_HSQL_EXTENSION = ".data"; // filename extension for
	// HSQL DB

	// class variables
	private Connection conn = null; // DB connection
	private LexAccessApi lexdb = null; // Lexicon access object

	// if false, don't keep standard inflections in the Word object
	private boolean keepStandardInflections = false;

	/****************************************************************************/
	// constructors
	/****************************************************************************/

	/**
	 * set up lexicon using file which contains downloaded lexAccess HSQL DB and
	 * default passwords
	 * 
	 * @param filename
	 *            of HSQL DB
	 */
	public NIHDBLexicon(String filename) {
		super();
		// get rid of .data at end of filename if necessary
		String dbfilename = filename;
		if (dbfilename.endsWith(DB_HSQL_EXTENSION))
			dbfilename = dbfilename.substring(0, dbfilename.length()
					- DB_HSQL_EXTENSION.length());

		// try to open DB and set up lexicon
		try {
			Class.forName(DB_HSQL_DRIVER);
			conn = DriverManager.getConnection(DB_HQSL_JDBC + dbfilename,
					DB_DEFAULT_USERNAME, DB_DEFAULT_PASSWORD);
			// now set up lexical access object
			lexdb = new LexAccessApi(conn);

		} catch (Exception ex) {
			System.out.println("Cannot open lexical db: " + ex.toString());
			// probably should thrown an exception
		}
	}

	/**
	 * set up lexicon using general DB parameters; DB must be NIH specialist
	 * lexicon from lexAccess
	 * 
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 */
	public NIHDBLexicon(String driver, String url, String username,
			String password) {

		super();

		// try to open DB and set up lexicon
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			// now set up lexical access object
			lexdb = new LexAccessApi(conn);
		} catch (Exception ex) {
			System.out.println("Cannot open lexical db: " + ex.toString());
			// probably should thrown an exception
		}
	}

	// need more constructors for general case...

	/***************** methods to set global parameters ****************************/

	/**
	 * reports whether Words include standard (derivable) inflections
	 * 
	 * @return true if standard inflections are kept
	 */
	public boolean isKeepStandardInflections() {
		return keepStandardInflections;
	}

	/**
	 * set whether Words should include standard (derivable) inflections
	 * 
	 * @param keepStandardInflections
	 *            - if true, standard inflections are kept
	 */
	public void setKeepStandardInflections(boolean keepStandardInflections) {
		this.keepStandardInflections = keepStandardInflections;
	}

	/****************************************************************************/
	// core methods to retrieve words from DB
	/****************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#getWords(java.lang.String,
	 * simplenlg.features.LexicalCategory)
	 */
	@Override
	public synchronized List<WordElement> getWords(String baseForm, LexicalCategory category) {
		// get words from DB
		try {
			LexAccessApiResult lexResult = lexdb.GetLexRecordsByBase(baseForm,
					LexAccessApi.B_EXACT);			
			return getWordsFromLexResult(category, lexResult);
		} catch (SQLException ex) {
			System.out.println("Lexical DB error: " + ex.toString());
			// probably should thrown an exception
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#getWordsByID(java.lang.String)
	 */
	@Override
	public synchronized List<WordElement> getWordsByID(String id) {
		// get words from DB
		try {
			LexAccessApiResult lexResult = lexdb.GetLexRecords(id);
			return getWordsFromLexResult(LexicalCategory.ANY, lexResult);
		} catch (SQLException ex) {
			System.out.println("Lexical DB error: " + ex.toString());
			// probably should thrown an exception
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#getWordsFromVariant(java.lang.String,
	 * simplenlg.features.LexicalCategory)
	 */
	@Override
	public synchronized List<WordElement> getWordsFromVariant(String variant,
			LexicalCategory category) {
		// get words from DB
		try {
			LexAccessApiResult lexResult = lexdb.GetLexRecords(variant);
			return getWordsFromLexResult(category, lexResult);
		} catch (SQLException ex) {
			System.out.println("Lexical DB error: " + ex.toString());
			// probably should thrown an exception
		}
		return null;
	}

	/****************************************************************************/
	// other methods
	/****************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#close()
	 */
	@Override
	public void close() {
		if (lexdb != null)
			lexdb.CleanUp();
	}

	/**
	 * make a WordElement from a lexical record. Currently just specifies basic
	 * params and inflections Should do more in the future!
	 * 
	 * @param record
	 * @return
	 */
	private WordElement makeWord(LexRecord record) {
		// get basic data
		String baseForm = record.GetBase();		
		LexicalCategory category = getSimplenlgCategory(record);
		String id = record.GetEui();

		// create word class
		WordElement wordElement = new WordElement(baseForm, category, id);

		// now add type information
		switch (category) {
		case ADJECTIVE:
			addAdjectiveInfo(wordElement, record.GetCatEntry().GetAdjEntry());
			break;
		case ADVERB:
			addAdverbInfo(wordElement, record.GetCatEntry().GetAdvEntry());
			break;
		case NOUN:
			addNounInfo(wordElement, record.GetCatEntry().GetNounEntry());
			break;
		case VERB:
			addVerbInfo(wordElement, record.GetCatEntry().GetVerbEntry());
			break;
		// ignore closed class words
		}

		Inflection defaultInfl = (Inflection) wordElement
				.getDefaultInflectionalVariant();

		// now add inflected forms
		// if (keepStandardInflections || !standardInflections(record,
		// category)) {
		for (InflVar inflection : record.GetInflVarsAndAgreements()
				.GetInflValues()) {
			String simplenlgInflection = getSimplenlgInflection(inflection
					.GetInflection());

			if (simplenlgInflection != null) {
				String inflectedForm = inflection.GetVar();
				Inflection inflType = Inflection.getInflCode(inflection
						.GetType());

				// store all inflectional variants, except for regular ones
				// unless explicitly set
				if (inflType != null
						&& !(Inflection.REGULAR.equals(inflType) && !this.keepStandardInflections)) {
					wordElement.addInflectionalVariant(inflType,
							simplenlgInflection, inflectedForm);
				}

				// if the infl variant is the default, also set this feature on
				// the word
				if (defaultInfl == null
						|| (defaultInfl.equals(inflType) && !(Inflection.REGULAR
								.equals(inflType) && !this.keepStandardInflections))) {
					wordElement.setFeature(simplenlgInflection, inflectedForm);
				}

				// wordElement
				// .setFeature(simplenlgInflection, inflection.GetVar());
			}
		}
		// }

		// add acronym info
		addAcronymInfo(wordElement, record);

		// now add spelling variants
		addSpellingVariants(wordElement, record);

		return wordElement;
	}

	/**
	 * return list of WordElement from LexAccessApiResult
	 * 
	 * @param category
	 *            - desired category (eg, NOUN) (this filters list)
	 * @param lexResult
	 *            - the LexAccessApiResult
	 * @return list of WordElement
	 */
	private List<WordElement> getWordsFromLexResult(LexicalCategory category,
			LexAccessApiResult lexResult) {
		List<LexRecord> records = lexResult.GetJavaObjs();

		// set up array of words to return
		List<WordElement> wordElements = new ArrayList<WordElement>();

		// iterate through result records, adding to words as appropriate
		for (LexRecord record : records) {

			if (category == LexicalCategory.ANY
					|| category == getSimplenlgCategory(record))
				wordElements.add(makeWord(record));
		}
		return wordElements;
	}

	/**
	 * check if this record has a standard (regular) inflection
	 * 
	 * @param record
	 * @param simplenlg
	 *            syntactic category
	 * @return true if standard (regular) inflection
	 */
	@SuppressWarnings("unused")
	private boolean standardInflections(LexRecord record,
			LexicalCategory category) {
		List<String> variants = null;
		switch (category) {
		case NOUN:
			variants = record.GetCatEntry().GetNounEntry().GetVariants();
			break;
		case ADJECTIVE:
			variants = record.GetCatEntry().GetAdjEntry().GetVariants();
			break;
		case ADVERB:
			variants = record.GetCatEntry().GetAdvEntry().GetVariants();
			break;
		case MODAL:
			variants = record.GetCatEntry().GetModalEntry().GetVariant();
			break;
		case VERB:
			if (record.GetCatEntry().GetVerbEntry() != null) // aux verbs (eg
				// be) won't
				// have verb
				// entries
				variants = record.GetCatEntry().GetVerbEntry().GetVariants();
			break;
		}

		return notEmpty(variants) && variants.contains("reg");
	}

	/***********************************************************************************/
	// The following methods map codes in the NIH Specialist Lexicon
	// into the codes used in simplenlg
	/***********************************************************************************/

	/**
	 * get the simplenlg LexicalCategory of a record
	 * 
	 * @param cat
	 * @return
	 */
	private LexicalCategory getSimplenlgCategory(LexRecord record) {
		String cat = record.GetCategory();
		if (cat == null)
			return LexicalCategory.ANY;
		else if (cat.equalsIgnoreCase("noun"))
			return LexicalCategory.NOUN;
		else if (cat.equalsIgnoreCase("verb"))
			return LexicalCategory.VERB;
		else if (cat.equalsIgnoreCase("aux")
				&& record.GetBase().equalsIgnoreCase("be")) // return aux "be"
			// as a VERB
			// not needed for other aux "have" and "do", they have a verb entry
			return LexicalCategory.VERB;
		else if (cat.equalsIgnoreCase("adj"))
			return LexicalCategory.ADJECTIVE;
		else if (cat.equalsIgnoreCase("adv"))
			return LexicalCategory.ADVERB;
		else if (cat.equalsIgnoreCase("pron"))
			return LexicalCategory.PRONOUN;
		else if (cat.equalsIgnoreCase("det"))
			return LexicalCategory.DETERMINER;
		else if (cat.equalsIgnoreCase("prep"))
			return LexicalCategory.PREPOSITION;
		else if (cat.equalsIgnoreCase("conj"))
			return LexicalCategory.CONJUNCTION;
		else if (cat.equalsIgnoreCase("compl"))
			return LexicalCategory.COMPLEMENTISER;
		else if (cat.equalsIgnoreCase("modal"))
			return LexicalCategory.MODAL;

		// return ANY for other cats
		else
			return LexicalCategory.ANY;
	}

	/**
	 * convert an inflection type in NIH lexicon into one used by simplenlg
	 * return null if no simplenlg equivalent to NIH inflection type
	 * 
	 * @param NIHInflection
	 *            - inflection type in NIH lexicon
	 * @return inflection type in simplenlg
	 */
	private String getSimplenlgInflection(String NIHInflection) {
		if (NIHInflection == null)
			return null;
		else if (NIHInflection.equalsIgnoreCase("comparative"))
			return LexicalFeature.COMPARATIVE;
		else if (NIHInflection.equalsIgnoreCase("superlative"))
			return LexicalFeature.SUPERLATIVE;
		else if (NIHInflection.equalsIgnoreCase("plural"))
			return LexicalFeature.PLURAL;
		else if (NIHInflection.equalsIgnoreCase("pres3s"))
			return LexicalFeature.PRESENT3S;
		else if (NIHInflection.equalsIgnoreCase("past"))
			return LexicalFeature.PAST;
		else if (NIHInflection.equalsIgnoreCase("pastPart"))
			return LexicalFeature.PAST_PARTICIPLE;
		else if (NIHInflection.equalsIgnoreCase("presPart"))
			return LexicalFeature.PRESENT_PARTICIPLE;
		else
			// no equvalent in simplenlg, eg clitic or negative
			return null;
	}

	/**
	 * extract adj information from NIH AdjEntry record, and add to a simplenlg
	 * WordElement For now just extract position info
	 * 
	 * @param wordElement
	 * @param AdjEntry
	 */
	private void addAdjectiveInfo(WordElement wordElement, AdjEntry adjEntry) {
		boolean qualitativeAdj = false;
		boolean colourAdj = false;
		boolean classifyingAdj = false;
		boolean predicativeAdj = false;
		List<String> positions = adjEntry.GetPosition();
		for (String position : positions) {
			if (position.startsWith("attrib(1)"))
				qualitativeAdj = true;
			else if (position.startsWith("attrib(2)"))
				colourAdj = true;
			else if (position.startsWith("attrib(3)"))
				classifyingAdj = true;
			else if (position.startsWith("pred"))
				predicativeAdj = true;
			// ignore other positions
		}
		// ignore (for now) other info in record
		wordElement.setFeature(LexicalFeature.QUALITATIVE, qualitativeAdj);
		wordElement.setFeature(LexicalFeature.COLOUR, colourAdj);
		wordElement.setFeature(LexicalFeature.CLASSIFYING, classifyingAdj);
		wordElement.setFeature(LexicalFeature.PREDICATIVE, predicativeAdj);
		return;
	}

	/**
	 * extract adv information from NIH AdvEntry record, and add to a simplenlg
	 * WordElement For now just extract modifier type
	 * 
	 * @param wordElement
	 * @param AdvEntry
	 */
	private void addAdverbInfo(WordElement wordElement, AdvEntry advEntry) {
		boolean verbModifier = false;
		boolean sentenceModifier = false;
		boolean intensifier = false;

		List<String> modifications = advEntry.GetModification();
		for (String modification : modifications) {
			if (modification.startsWith("verb_modifier"))
				verbModifier = true;
			else if (modification.startsWith("sentence_modifier"))
				sentenceModifier = true;
			else if (modification.startsWith("intensifier"))
				intensifier = true;
			// ignore other modification types
		}
		// ignore (for now) other info in record
		wordElement.setFeature(LexicalFeature.VERB_MODIFIER, verbModifier);
		wordElement.setFeature(LexicalFeature.SENTENCE_MODIFIER,
				sentenceModifier);
		wordElement.setFeature(LexicalFeature.INTENSIFIER, intensifier);
		return;
	}

	/**
	 * extract noun information from NIH NounEntry record, and add to a
	 * simplenlg WordElement For now just extract whether count/non-count and
	 * whether proper or not
	 * 
	 * @param wordElement
	 * @param nounEntry
	 */
	private void addNounInfo(WordElement wordElement, NounEntry nounEntry) {
		boolean proper = nounEntry.IsProper();
		// boolean nonCountVariant = false;
		// boolean regVariant = false;

		// add the inflectional variants
		List<String> variants = nounEntry.GetVariants();

		if (!variants.isEmpty()) {
			List<Inflection> wordVariants = new ArrayList<Inflection>();

			for (String v : variants) {
				int index = v.indexOf("|");
				String code;

				if (index > -1) {
					code = v.substring(0, index).toLowerCase().trim();

				} else {
					code = v.toLowerCase().trim();
				}

				Inflection infl = Inflection.getInflCode(code);

				if (infl != null) {
					wordVariants.add(infl);
					wordElement.addInflectionalVariant(infl);
				}
			}

			// if the variants include "reg", this is the default, otherwise
			// just a random pick
			Inflection defaultVariant = wordVariants
					.contains(Inflection.REGULAR)
					|| wordVariants.isEmpty() ? Inflection.REGULAR
					: wordVariants.get(0);			
			wordElement.setFeature(LexicalFeature.DEFAULT_INFL, defaultVariant);
			wordElement.setDefaultInflectionalVariant(defaultVariant);
		}

		// for (String variant : variants) {
		// if (variant.startsWith("uncount")
		// || variant.startsWith("groupuncount"))
		// nonCountVariant = true;
		//
		// if (variant.startsWith("reg"))
		// regVariant = true;
		// // ignore other variant info
		// }

		// lots of words have both "reg" and "unCount", indicating they
		// can be used in either way. Regard such words as normal,
		// only flag as nonCount if unambiguous
		// wordElement.setFeature(LexicalFeature.NON_COUNT, nonCountVariant
		// && !regVariant);
		wordElement.setFeature(LexicalFeature.PROPER, proper);
		// ignore (for now) other info in record

		return;
	}

	/**
	 * extract verb information from NIH VerbEntry record, and add to a
	 * simplenlg WordElement For now just extract transitive, instransitive,
	 * and/or ditransitive
	 * 
	 * @param wordElement
	 * @param verbEntry
	 */
	private void addVerbInfo(WordElement wordElement, VerbEntry verbEntry) {
		if (verbEntry == null) { // should only happen for aux verbs, which have
			// auxEntry instead of verbEntry in NIH Lex
			// just flag as transitive and return
			wordElement.setFeature(LexicalFeature.INTRANSITIVE, false);
			wordElement.setFeature(LexicalFeature.TRANSITIVE, true);
			wordElement.setFeature(LexicalFeature.DITRANSITIVE, false);
			return;
		}

		boolean intransitiveVerb = notEmpty(verbEntry.GetIntran());
		boolean transitiveVerb = notEmpty(verbEntry.GetTran())
				|| notEmpty(verbEntry.GetCplxtran());
		boolean ditransitiveVerb = notEmpty(verbEntry.GetDitran());

		wordElement.setFeature(LexicalFeature.INTRANSITIVE, intransitiveVerb);
		wordElement.setFeature(LexicalFeature.TRANSITIVE, transitiveVerb);
		wordElement.setFeature(LexicalFeature.DITRANSITIVE, ditransitiveVerb);

		// add the inflectional variants
		List<String> variants = verbEntry.GetVariants();

		if (!variants.isEmpty()) {
			List<Inflection> wordVariants = new ArrayList<Inflection>();

			for (String v : variants) {
				int index = v.indexOf("|");
				String code;
				Inflection infl;

				if (index > -1) {
					code = v.substring(0, index).toLowerCase().trim();
					infl = Inflection.getInflCode(code);					

				} else {
					infl = Inflection.getInflCode(v.toLowerCase().trim());
				}

				if (infl != null) {
					wordElement.addInflectionalVariant(infl);
					wordVariants.add(infl);
				}
			}

			// if the variants include "reg", this is the default, otherwise
			// just a random pick
			Inflection defaultVariant = wordVariants
					.contains(Inflection.REGULAR)
					|| wordVariants.isEmpty() ? Inflection.REGULAR
					: wordVariants.get(0);
//			wordElement.setFeature(LexicalFeature.INFLECTIONS, wordVariants);
//			wordElement.setFeature(LexicalFeature.DEFAULT_INFL, defaultVariant);
			wordElement.setDefaultInflectionalVariant(defaultVariant);
		}

		// ignore (for now) other info in record
		return;
	}

	/**
	 * convenience method to test that a list is not null and not empty
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean notEmpty(List list) {
		return list != null && !list.isEmpty();
	}

	/**
	 * extract information about acronyms from NIH record, and add to a
	 * simplenlg WordElement.
	 * 
	 * <P>
	 * Acronyms are represented as lists of word elements. Any acronym will have
	 * a list of full form word elements, retrievable via
	 * {@link LexicalFeature#ACRONYM_OF}
	 * 
	 * @param wordElement
	 * @param record
	 */
	private void addAcronymInfo(WordElement wordElement, LexRecord record) {
		// NB: the acronyms are actually the full forms of which the word is an
		// acronym
		List<String> acronyms = record.GetAcronyms();

		if (!acronyms.isEmpty()) {
			// the list of full forms of which this word is an acronym
			List<NLGElement> acronymOf = wordElement
					.getFeatureAsElementList(LexicalFeature.ACRONYM_OF);

			// keep all acronym full forms and set them up as wordElements
			for (String fullForm : acronyms) {
				if (fullForm.contains("|")) {
					// get the acronym id
					String acronymID = fullForm.substring(
							fullForm.indexOf("|") + 1, fullForm.length());
					// create the full form element
					WordElement fullFormWE = this.getWordByID(acronymID);

					if (fullForm != null) {
						// add as full form of this acronym
						acronymOf.add(fullFormWE);

						// List<NLGElement> fullFormAcronyms = fullFormWE
						// .getFeatureAsElementList(LexicalFeature.ACRONYMS);
						// fullFormAcronyms.add(wordElement);
						// fullFormWE.setFeature(LexicalFeature.ACRONYMS,
						// fullFormAcronyms);
					}
				}
			}

			// set all the full forms for this acronym
			wordElement.setFeature(LexicalFeature.ACRONYM_OF, acronymOf);
		}

		// if (!acronyms.isEmpty()) {
		//
		// String acronym = acronyms.get(0);
		// // remove anything after a |, this will be an NIH ID
		// if (acronym.contains("|"))
		// acronym = acronym.substring(0, acronym.indexOf("|"));
		// wordElement.setFeature(LexicalFeature.ACRONYM_OF, acronym);
		// }

		return;
	}

	/**
	 * Extract info about the spelling variants of a word from an NIH record,
	 * and add to the simplenlg Woordelement.
	 * 
	 * <P>
	 * Spelling variants are represented as lists of strings, retrievable via
	 * {@link LexicalFeature#SPELL_VARS}
	 * 
	 * @param wordElement
	 * @param record
	 */
	private void addSpellingVariants(WordElement wordElement, LexRecord record) {
		Vector<String> vars = record.GetSpellingVars();

		if (vars != null && !vars.isEmpty()) {
			List<String> wordVars = new ArrayList<String>();
			wordVars.addAll(vars);
			wordElement.setFeature(LexicalFeature.SPELL_VARS, wordVars);
		}

		// we set the default spelling var as the baseForm
		wordElement.setFeature(LexicalFeature.DEFAULT_SPELL, wordElement
				.getBaseForm());
	}

}
