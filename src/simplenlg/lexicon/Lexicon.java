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
package simplenlg.lexicon;

import java.util.List;

import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;

/**
 * This is the generic abstract class for a Lexicon. In simplenlg V4, a
 * <code>Lexicon</code> is a collection of
 * {@link simplenlg.framework.WordElement} objects; it does not do any
 * morphological processing (as was the case in simplenlg V3). Information about
 * <code>WordElement</code> can be obtained from a database (
 * {@link simplenlg.lexicon.NIHDBLexicon}) or from an XML file (
 * {@link simplenlg.lexicon.XMLLexicon}). Simplenlg V4 comes with a default
 * (XML) lexicon, which is retrieved by the <code>getDefaultLexicon</code>
 * method.
 * 
 * There are several ways of retrieving words. If in doubt, use
 * <code>lookupWord</code>. More control is available from the
 * <code>getXXXX</code> methods, which allow words to retrieved in several ways
 * <OL>
 * <LI>baseform and {@link simplenlg.framework.LexicalCategory}; for example
 * "university" and <code>Noun</code>
 * <LI>just baseform; for example, "university"
 * <LI>ID string (if this is supported by the underlying DB or XML file); for
 * example "E0063257" is the ID for "university" in the NIH Specialist lexicon
 * <LI>variant; this looks for a word given a form of the word which may be
 * inflected (eg, "universities") or a spelling variant (eg, "color" for
 * "colour"). Acronyms are not considered to be variants (eg, "UK" and
 * "United Kingdom" are regarded as different words). <br>
 * <I>Note:</I> variant lookup is not guaranteed, this is a feature which
 * hopefully will develop over time
 * <LI>variant and {@link simplenlg.framework.LexicalCategory}; for example
 * "universities" and <code>Noun</code>
 * </OL>
 * 
 * For each type of lookup, there are three methods
 * <UL>
 * <LI> <code>getWords</code>: get all matching
 * {@link simplenlg.framework.WordElement} in the Lexicon. For example,
 * <code>getWords("dog")</code> would return a <code>List</code> of two
 * <code>WordElement</code>, one for the noun "dog" and one for the verb "dog".
 * If there are no matching entries in the lexicon, this method returns an empty
 * collection
 * <LI> <code>getWord</code>: get a single matching
 * {@link simplenlg.framework.WordElement} in the Lexicon. For example,
 * <code>getWord("dog")</code> would a <code> for either the noun "dog" or the
 * verb "dog" (unpredictable).   If there are no matching entries in
 * the lexicon, this method will create a default <code>WordElement</code> based
 * on the information specified.
 * <LI> <code>hasWord</code>: returns <code>true</code> if the Lexicon contains
 * at least one matching <code>WordElement</code>
 * </UL>
 * 
 * @author Albert Gatt (simplenlg v3 lexicon)
 * @author Ehud Reiter (simplenlg v4 lexicon)
 */

public abstract class Lexicon {

	/****************************************************************************/
	// constructors and related
	/****************************************************************************/

	/**
	 * returns the default built-in lexicon
	 * 
	 * @return default lexicon
	 */
	public static Lexicon getDefaultLexicon() {
		return new XMLLexicon();
	}

	/**
	 * create a default WordElement. May be overridden by specific types of
	 * lexicon
	 * 
	 * @param baseForm
	 *            - base form of word
	 * @param category
	 *            - category of word
	 * @return WordElement entry for specified info
	 */
	protected WordElement createWord(String baseForm, LexicalCategory category) {
		return new WordElement(baseForm, category); // return default
		// WordElement of this
		// baseForm, category
	}

	/**
	 * create a default WordElement. May be overridden by specific types of
	 * lexicon
	 * 
	 * @param baseForm
	 *            - base form of word
	 * @return WordElement entry for specified info
	 */
	protected WordElement createWord(String baseForm) {
		return new WordElement(baseForm); // return default WordElement of this
		// baseForm
	}

	/***************************************************************************/
	// default methods for looking up words
	// These try the following (in this order)
	// 1) word with matching base
	// 2) word with matching variant
	// 3) word with matching ID
	// 4) create a new workd
	/***************************************************************************/

	/**
	 * General word lookup method, tries base form, variant, ID (in this order)
	 * Creates new word if can't find existing word
	 * 
	 * @param baseForm
	 * @param category
	 * @return word
	 */
	public WordElement lookupWord(String baseForm, LexicalCategory category) {
		if (hasWord(baseForm, category))
			return getWord(baseForm, category);
		else if (hasWordFromVariant(baseForm, category))
			return getWordFromVariant(baseForm, category);
		else if (hasWordByID(baseForm))
			return getWordByID(baseForm);
		else
			return createWord(baseForm, category);
	}

	/**
	 * General word lookup method, tries base form, variant, ID (in this order)
	 * Creates new word if can't find existing word
	 * 
	 * @param baseForm
	 * @return word
	 */
	public WordElement lookupWord(String baseForm) {
		return lookupWord(baseForm, LexicalCategory.ANY);
	}

	/****************************************************************************/
	// get words by baseform and category
	// fundamental version is getWords(String baseForm, Category category),
	// this must be defined by subclasses. Other versions are convenience
	// methods. These may be overriden for efficiency, but this is not required.
	/****************************************************************************/

	/**
	 * returns all Words which have the specified base form and category
	 * 
	 * @param baseForm
	 *            - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @param category
	 *            - syntactic category of word (ANY for unknown)
	 * @return collection of all matching Words (may be empty)
	 */
	abstract public List<WordElement> getWords(String baseForm,
			LexicalCategory category);

	/**
	 * get a WordElement which has the specified base form and category
	 * 
	 * @param baseForm
	 *            - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @param category
	 *            - syntactic category of word (ANY for unknown)
	 * @return if Lexicon contains such a WordElement, it is returned (the first
	 *         match is returned if there are several matches). If the Lexicon
	 *         does not contain such a WordElement, a new WordElement is created
	 *         and returned
	 */
	public WordElement getWord(String baseForm, LexicalCategory category) {// convenience
		// method
		// derived
		// from
		// other
		// methods
		List<WordElement> wordElements = getWords(baseForm, category);
		if (wordElements.isEmpty())
			return createWord(baseForm, category); // return default WordElement
		// of this baseForm,
		// category
		else
			return selectMatchingWord(wordElements, baseForm);
	}

	/** choose a single WordElement from a list of WordElements.  Prefer one
	 * which exactly matches the baseForm
	 * @param wordElements
	 *           - list of WordElements retrieved from lexicon
	 * @param baseForm
	             - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @return single WordElement (from list)
	 */
	private WordElement selectMatchingWord(List<WordElement> wordElements, String baseForm) {
		// EHUD REITER  - this method added because some DBs are case-insensitive,
		// so a query on "man" returns both "man" and "MAN".  In such cases, the
		// exact match (eg, "man") should be returned
		
		// below check is redundant, since caller should check this
		if (wordElements == null || wordElements.isEmpty())
			return createWord(baseForm);
		
		// look for exact match in base form
		for (WordElement wordElement: wordElements)
			if (wordElement.getBaseForm().equals(baseForm))
				return wordElement;
		
		// else return first element in list
		return wordElements.get(0);
	}

	/**
	 * return <code>true</code> if the lexicon contains a WordElement which has
	 * the specified base form and category
	 * 
	 * @param baseForm
	 *            - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @param category
	 *            - syntactic category of word (ANY for unknown)
	 * @return <code>true</code> if Lexicon contains such a WordElement
	 */
	public boolean hasWord(String baseForm, LexicalCategory category) {// convenience
		// method
		// derived
		// from
		// other
		// methods)
		// {
		return !getWords(baseForm, category).isEmpty();
	}

	/**
	 * returns all Words which have the specified base form
	 * 
	 * @param baseForm
	 *            - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @return collection of all matching Words (may be empty)
	 */
	public List<WordElement> getWords(String baseForm) { // convenience method
		// derived from
		// other methods
		return getWords(baseForm, LexicalCategory.ANY);
	}

	/**
	 * get a WordElement which has the specified base form (of any category)
	 * 
	 * @param baseForm
	 *            - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @return if Lexicon contains such a WordElement, it is returned (the first
	 *         match is returned if there are several matches). If the Lexicon
	 *         does not contain such a WordElement, a new WordElement is created
	 *         and returned
	 */
	public WordElement getWord(String baseForm) { // convenience method derived
		// from other methods
		List<WordElement> wordElements = getWords(baseForm);

		if (wordElements.isEmpty())
			return createWord(baseForm); // return default WordElement of this
		// baseForm
		else
			return selectMatchingWord(wordElements, baseForm);
	}

	/**
	 * return <code>true</code> if the lexicon contains a WordElement which has
	 * the specified base form (in any category)
	 * 
	 * @param baseForm
	 *            - base form of word, eg "be" or "dog" (not "is" or "dogs")
	 * @return <code>true</code> if Lexicon contains such a WordElement
	 */
	public boolean hasWord(String baseForm) {// convenience method derived from
		// other methods) {
		return !getWords(baseForm).isEmpty();
	}

	/****************************************************************************/
	// get words by ID
	// fundamental version is getWordsByID(String id),
	// this must be defined by subclasses.
	// Other versions are convenience methods
	// These may be overriden for efficiency, but this is not required.
	/****************************************************************************/

	/**
	 * returns a List of WordElement which have this ID. IDs are
	 * lexicon-dependent, and should be unique. Therefore the list should
	 * contain either zero elements (if no such word exists) or one element (if
	 * the word is found)
	 * 
	 * @param id
	 *            - internal lexicon ID for a word
	 * @return either empty list (if no word with this ID exists) or list
	 *         containing the matching word
	 */
	abstract public List<WordElement> getWordsByID(String id);

	/**
	 * get a WordElement with the specified ID
	 * 
	 * @param id
	 *            internal lexicon ID for a word
	 * @return WordElement with this ID if found; otherwise a new WordElement is
	 *         created with the ID as the base form
	 */
	public WordElement getWordByID(String id) {
		List<WordElement> wordElements = getWordsByID(id);
		if (wordElements.isEmpty())
			return createWord(id); // return WordElement based on ID; may help
		// in debugging...
		else
			return wordElements.get(0); // else return first match
	}

	/**
	 * return <code>true</code> if the lexicon contains a WordElement which the
	 * specified ID
	 * 
	 * @param id
	 *            - internal lexicon ID for a word
	 * @return <code>true</code> if Lexicon contains such a WordElement
	 */
	public boolean hasWordByID(String id) {// convenience method derived from
		// other methods) {
		return !getWordsByID(id).isEmpty();
	}

	/****************************************************************************/
	// get words by variant - try to return a WordElement given an inflectional
	// or spelling
	// variant. For the moment, acronyms are considered as separate words, not
	// variants
	// (this may change in the future)
	// fundamental version is getWordsFromVariant(String baseForm, Category
	// category),
	// this must be defined by subclasses. Other versions are convenience
	// methods. These may be overriden for efficiency, but this is not required.
	/****************************************************************************/

	/**
	 * returns Words which have an inflected form and/or spelling variant that
	 * matches the specified variant, and are in the specified category. <br>
	 * <I>Note:</I> the returned word list may not be complete, it depends on
	 * how it is implemented by the underlying lexicon
	 * 
	 * @param variant
	 *            - base form, inflected form, or spelling variant of word
	 * @param category
	 *            - syntactic category of word (ANY for unknown)
	 * @return list of all matching Words (empty list if no matching WordElement
	 *         found)
	 */
	abstract public List<WordElement> getWordsFromVariant(String variant,
			LexicalCategory category);

	/**
	 * returns a WordElement which has the specified inflected form and/or
	 * spelling variant that matches the specified variant, of the specified
	 * category
	 * 
	 * @param variant
	 *            - base form, inflected form, or spelling variant of word
	 * @param category
	 *            - syntactic category of word (ANY for unknown)
	 * @return a matching WordElement (if found), otherwise a new word is
	 *         created using thie variant as the base form
	 */
	public WordElement getWordFromVariant(String variant,
			LexicalCategory category) {
		List<WordElement> wordElements = getWordsFromVariant(variant, category);
		if (wordElements.isEmpty())
			return createWord(variant, category); // return default WordElement
		// using variant as base
		// form
		else
			return wordElements.get(0); // else return first match

	}

	/**
	 * return <code>true</code> if the lexicon contains a WordElement which
	 * matches the specified variant form and category
	 * 
	 * @param variant
	 *            - base form, inflected form, or spelling variant of word
	 * @param category
	 *            - syntactic category of word (ANY for unknown)
	 * @return <code>true</code> if Lexicon contains such a WordElement
	 */
	public boolean hasWordFromVariant(String variant, LexicalCategory category) {// convenience
		// method
		// derived
		// from
		// other
		// methods)
		// {
		return !getWordsFromVariant(variant, category).isEmpty();
	}

	/**
	 * returns Words which have an inflected form and/or spelling variant that
	 * matches the specified variant, of any category. <br>
	 * <I>Note:</I> the returned word list may not be complete, it depends on
	 * how it is implemented by the underlying lexicon
	 * 
	 * @param variant
	 *            - base form, inflected form, or spelling variant of word
	 * @return list of all matching Words (empty list if no matching WordElement
	 *         found)
	 */
	public List<WordElement> getWordsFromVariant(String variant) {
		return getWordsFromVariant(variant, LexicalCategory.ANY);
	}

	/**
	 * returns a WordElement which has the specified inflected form and/or
	 * spelling variant that matches the specified variant, of any category.
	 * 
	 * @param variant
	 *            - base form, inflected form, or spelling variant of word
	 * @return a matching WordElement (if found), otherwise a new word is
	 *         created using thie variant as the base form
	 */
	public WordElement getWordFromVariant(String variant) {
		List<WordElement> wordElements = getWordsFromVariant(variant);
		if (wordElements.isEmpty())
			return createWord(variant); // return default WordElement using
		// variant as base form
		else
			return wordElements.get(0); // else return first match
	}

	/**
	 * return <code>true</code> if the lexicon contains a WordElement which
	 * matches the specified variant form (in any category)
	 * 
	 * @param variant
	 *            - base form, inflected form, or spelling variant of word
	 * @return <code>true</code> if Lexicon contains such a WordElement
	 */
	public boolean hasWordFromVariant(String variant) {// convenience method
		// derived from other
		// methods) {
		return !getWordsFromVariant(variant).isEmpty();
	}

	/****************************************************************************/
	// other methods
	/****************************************************************************/

	/**
	 * close the lexicon (if necessary) if lexicon does not need to be closed,
	 * this does nothing
	 */
	public void close() {
		// default method does nothing
	}

}
