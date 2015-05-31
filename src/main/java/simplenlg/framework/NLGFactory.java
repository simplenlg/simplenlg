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
package simplenlg.framework;

import java.util.Arrays;
import java.util.List;

import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * <p>
 * This class contains methods for creating syntactic phrases. These methods
 * should be used instead of directly invoking new on SPhraseSpec, etc.
 * 
 * The phrase factory should be linked to s lexicon if possible (although it
 * will work without one)
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class NLGFactory {

	/***
	 * CODING COMMENTS The original version of phraseFactory created a crude
	 * realisation of the phrase in the BASE_FORM feature. This was just for
	 * debugging purposes (note BASE_FORM on a WordElement is meaningful), I've
	 * zapped this as it was makig things too complex
	 * 
	 * This version of phraseFactory replicates WordElements (instead of reusing
	 * them). I think this is because elemente are linked to their parent
	 * phrases, via the parent data member. May be good to check if this is
	 * actually necessary
	 * 
	 * The explicit list of pronouns below should be replaced by a reference to
	 * the lexicon
	 * 
	 * Things to sort out at some point...
	 * 
	 */
	/** The lexicon to be used with this factory. */
	private Lexicon                   lexicon;

	/** The list of English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> PRONOUNS               = Arrays.asList("I",
	                                                                         "you",
	                                                                         "he",
	                                                                         "she",
	                                                                         "it",
	                                                                         "me",
	                                                                         "you",
	                                                                         "him",
	                                                                         "her",
	                                                                         "it",
	                                                                         "myself",
	                                                                         "yourself",
	                                                                         "himself",
	                                                                         "herself",
	                                                                         "itself",
	                                                                         "mine",
	                                                                         "yours",
	                                                                         "his",
	                                                                         "hers",
	                                                                         "its",
	                                                                         "we",
	                                                                         "you",
	                                                                         "they",
	                                                                         "they",
	                                                                         "they",
	                                                                         "us",
	                                                                         "you",
	                                                                         "them",
	                                                                         "them",
	                                                                         "them",
	                                                                         "ourselves",
	                                                                         "yourselves",
	                                                                         "themselves",
	                                                                         "themselves",
	                                                                         "themselves",
	                                                                         "ours",
	                                                                         "yours",
	                                                                         "theirs",
	                                                                         "theirs",
	                                                                         "theirs",
	                                                                         "there");

	/** The list of first-person English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> FIRST_PRONOUNS         = Arrays.asList("I",
	                                                                         "me",
	                                                                         "myself",
	                                                                         "we",
	                                                                         "us",
	                                                                         "ourselves",
	                                                                         "mine",
	                                                                         "my",
	                                                                         "ours",
	                                                                         "our");

	/** The list of second person English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> SECOND_PRONOUNS        = Arrays.asList("you",
	                                                                         "yourself",
	                                                                         "yourselves",
	                                                                         "yours",
	                                                                         "your");

	/** The list of reflexive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> REFLEXIVE_PRONOUNS     = Arrays.asList("myself",
	                                                                         "yourself",
	                                                                         "himself",
	                                                                         "herself",
	                                                                         "itself",
	                                                                         "ourselves",
	                                                                         "yourselves",
	                                                                         "themselves");

	/** The list of masculine English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> MASCULINE_PRONOUNS     = Arrays.asList("he", "him", "himself", "his");

	/** The list of feminine English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> FEMININE_PRONOUNS      = Arrays.asList("she", "her", "herself", "hers");

	/** The list of possessive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> POSSESSIVE_PRONOUNS    = Arrays.asList("mine",
	                                                                         "ours",
	                                                                         "yours",
	                                                                         "his",
	                                                                         "hers",
	                                                                         "its",
	                                                                         "theirs",
	                                                                         "my",
	                                                                         "our",
	                                                                         "your",
	                                                                         "her",
	                                                                         "their");

	/** The list of plural English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> PLURAL_PRONOUNS        = Arrays.asList("we",
	                                                                         "us",
	                                                                         "ourselves",
	                                                                         "ours",
	                                                                         "our",
	                                                                         "they",
	                                                                         "them",
	                                                                         "theirs",
	                                                                         "their");

	/** The list of English pronouns that can be singular or plural. */
	@SuppressWarnings("nls")
	private static final List<String> EITHER_NUMBER_PRONOUNS = Arrays.asList("there");

	/** The list of expletive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> EXPLETIVE_PRONOUNS     = Arrays.asList("there");

	/** regex for determining if a string is a single word or not **/
	private static final String       WORD_REGEX             = "\\w*";

	/**
	 * Creates a new phrase factory with no associated lexicon.
	 */
	public NLGFactory() {
		this(null);
	}

	/**
	 * Creates a new phrase factory with the associated lexicon.
	 * 
	 * @param newLexicon
	 *            the <code>Lexicon</code> to be used with this factory.
	 */
	public NLGFactory(Lexicon newLexicon) {
		setLexicon(newLexicon);
	}

	/**
	 * Sets the lexicon to be used by this factory. Passing a parameter of
	 * <code>null</code> will remove any existing lexicon from the factory.
	 * 
	 * @param newLexicon
	 *            the new <code>Lexicon</code> to be used.
	 */
	public void setLexicon(Lexicon newLexicon) {
		this.lexicon = newLexicon;
	}

	/**
	 * Creates a new element representing a word. If the word passed is already
	 * an <code>NLGElement</code> then that is returned unchanged. If a
	 * <code>String</code> is passed as the word then the factory will look up
	 * the <code>Lexicon</code> if one exists and use the details found to
	 * create a new <code>WordElement</code>.
	 * 
	 * @param word
	 *            the base word for the new element. This can be a
	 *            <code>NLGElement</code>, which is returned unchanged, or a
	 *            <code>String</code>, which is used to construct a new
	 *            <code>WordElement</code>.
	 * @param category
	 *            the <code>LexicalCategory</code> for the word.
	 * 
	 * @return an <code>NLGElement</code> representing the word.
	 */
	public NLGElement createWord(Object word, LexicalCategory category) {
		NLGElement wordElement = null;
		if(word instanceof NLGElement) {
			wordElement = (NLGElement) word;

		} else if(word instanceof String && this.lexicon != null) {
			// AG: change: should create a WordElement, not an
			// InflectedWordElement
			// wordElement = new InflectedWordElement(
			// (String) word, category);
			// if (this.lexicon != null) {
			// doLexiconLookUp(category, (String) word, wordElement);
			// }
			// wordElement = lexicon.getWord((String) word, category);
			wordElement = lexicon.lookupWord((String) word, category);
			if(PRONOUNS.contains(word)) {
				setPronounFeatures(wordElement, (String) word);
			}
		}

		return wordElement;
	}

	/**
	 * Create an inflected word element. InflectedWordElement represents a word
	 * that already specifies the morphological and other features that it
	 * should exhibit in a realisation. While normally, phrases are constructed
	 * using <code>WordElement</code>s, and features are set on phrases, it is
	 * sometimes desirable to set features directly on words (for example, when
	 * one wants to elide a specific word, but not its parent phrase).
	 * 
	 * <P>
	 * If the object passed is already a <code>WordElement</code>, then a new
	 * 
	 * <code>InflectedWordElement<code> is returned which wraps this <code>WordElement</code>
	 * . If the object is a <code>String</code>, then the
	 * <code>WordElement</code> representing this <code>String</code> is looked
	 * up, and a new
	 * <code>InflectedWordElement<code> wrapping this is returned. If no such <code>WordElement</code>
	 * is found, the element returned is an <code>InflectedWordElement</code>
	 * with the supplied string as baseform and no base <code>WordElement</code>
	 * . If an <code>NLGElement</code> is passed, this is returned unchanged.
	 * 
	 * @param word
	 *            the word
	 * @param category
	 *            the category
	 * @return an <code>InflectedWordElement</code>, or the original supplied
	 *         object if it is an <code>NLGElement</code>.
	 */
	public NLGElement createInflectedWord(Object word, LexicalCategory category) {
		// first get the word element
		NLGElement inflElement = null;

		if(word instanceof WordElement) {
			inflElement = new InflectedWordElement((WordElement) word);

		} else if(word instanceof String) {
			NLGElement baseword = createWord((String) word, category);

			if(baseword != null && baseword instanceof WordElement) {
				inflElement = new InflectedWordElement((WordElement) baseword);
			} else {
				inflElement = new InflectedWordElement((String) word, category);
			}

		} else if(word instanceof NLGElement) {
			inflElement = (NLGElement) word;
		}

		return inflElement;

	}

	/**
	 * A helper method to set the features on newly created pronoun words.
	 * 
	 * @param wordElement
	 *            the created element representing the pronoun.
	 * @param word
	 *            the base word for the pronoun.
	 */
	private void setPronounFeatures(NLGElement wordElement, String word) {
		wordElement.setCategory(LexicalCategory.PRONOUN);
		if(FIRST_PRONOUNS.contains(word)) {
			wordElement.setFeature(Feature.PERSON, Person.FIRST);
		} else if(SECOND_PRONOUNS.contains(word)) {
			wordElement.setFeature(Feature.PERSON, Person.SECOND);

			if("yourself".equalsIgnoreCase(word)) { //$NON-NLS-1$
				wordElement.setPlural(false);
			} else if("yourselves".equalsIgnoreCase(word)) { //$NON-NLS-1$
				wordElement.setPlural(true);
			} else {
				wordElement.setFeature(Feature.NUMBER, NumberAgreement.BOTH);
			}
		} else {
			wordElement.setFeature(Feature.PERSON, Person.THIRD);
		}
		if(REFLEXIVE_PRONOUNS.contains(word)) {
			wordElement.setFeature(LexicalFeature.REFLEXIVE, true);
		} else {
			wordElement.setFeature(LexicalFeature.REFLEXIVE, false);
		}
		if(MASCULINE_PRONOUNS.contains(word)) {
			wordElement.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
		} else if(FEMININE_PRONOUNS.contains(word)) {
			wordElement.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		} else {
			wordElement.setFeature(LexicalFeature.GENDER, Gender.NEUTER);
		}

		if(POSSESSIVE_PRONOUNS.contains(word)) {
			wordElement.setFeature(Feature.POSSESSIVE, true);
		} else {
			wordElement.setFeature(Feature.POSSESSIVE, false);
		}

		if(PLURAL_PRONOUNS.contains(word) && !SECOND_PRONOUNS.contains(word)) {
			wordElement.setPlural(true);
		} else if(!EITHER_NUMBER_PRONOUNS.contains(word)) {
			wordElement.setPlural(false);
		}

		if(EXPLETIVE_PRONOUNS.contains(word)) {
			wordElement.setFeature(InternalFeature.NON_MORPH, true);
			wordElement.setFeature(LexicalFeature.EXPLETIVE_SUBJECT, true);
		}
	}

	/**
	 * A helper method to look up the lexicon for the given word.
	 * 
	 * @param category
	 *            the <code>LexicalCategory</code> of the word.
	 * @param word
	 *            the base form of the word.
	 * @param wordElement
	 *            the created element representing the word.
	 */
	@SuppressWarnings("unused")
	private void doLexiconLookUp(LexicalCategory category, String word, NLGElement wordElement) {
		WordElement baseWord = null;

		if(LexicalCategory.NOUN.equals(category) && this.lexicon.hasWord(word, LexicalCategory.PRONOUN)) {
			baseWord = this.lexicon.lookupWord(word, LexicalCategory.PRONOUN);

			if(baseWord != null) {
				wordElement.setFeature(InternalFeature.BASE_WORD, baseWord);
				wordElement.setCategory(LexicalCategory.PRONOUN);
				if(!PRONOUNS.contains(word)) {
					wordElement.setFeature(InternalFeature.NON_MORPH, true);
				}
			}
		} else {
			baseWord = this.lexicon.lookupWord(word, category);
			wordElement.setFeature(InternalFeature.BASE_WORD, baseWord);
		}
	}

	/**
	 * Creates a blank preposition phrase with no preposition or complements.
	 * 
	 * @return a <code>PPPhraseSpec</code> representing this phrase.
	 */
	public PPPhraseSpec createPrepositionPhrase() {
		return createPrepositionPhrase(null, null);
	}

	/**
	 * Creates a preposition phrase with the given preposition.
	 * 
	 * @param preposition
	 *            the preposition to be used.
	 * @return a <code>PPPhraseSpec</code> representing this phrase.
	 */
	public PPPhraseSpec createPrepositionPhrase(Object preposition) {
		return createPrepositionPhrase(preposition, null);
	}

	/**
	 * Creates a preposition phrase with the given preposition and complement.
	 * An <code>NLGElement</code> representing the preposition is added as the
	 * head feature of this phrase while the complement is added as a normal
	 * phrase complement.
	 * 
	 * @param preposition
	 *            the preposition to be used.
	 * @param complement
	 *            the complement of the phrase.
	 * @return a <code>PPPhraseSpec</code> representing this phrase.
	 */
	public PPPhraseSpec createPrepositionPhrase(Object preposition, Object complement) {

		PPPhraseSpec phraseElement = new PPPhraseSpec(this);

		NLGElement prepositionalElement = createNLGElement(preposition, LexicalCategory.PREPOSITION);
		setPhraseHead(phraseElement, prepositionalElement);

		if(complement != null) {
			setComplement(phraseElement, complement);
		}
		return phraseElement;
	}

	/**
	 * A helper method for setting the complement of a phrase.
	 * 
	 * @param phraseElement
	 *            the created element representing this phrase.
	 * @param complement
	 *            the complement to be added.
	 */
	private void setComplement(PhraseElement phraseElement, Object complement) {
		NLGElement complementElement = createNLGElement(complement);
		phraseElement.addComplement(complementElement);
	}

	/**
	 * this method creates an NLGElement from an object If object is null,
	 * return null If the object is already an NLGElement, it is returned
	 * unchanged Exception: if it is an InflectedWordElement, return underlying
	 * WordElement If it is a String which matches a lexicon entry or pronoun,
	 * the relevant WordElement is returned If it is a different String, a
	 * wordElement is created if the string is a single word Otherwise a
	 * StringElement is returned Otherwise throw an exception
	 * 
	 * @param element
	 *            - object to look up
	 * @param category
	 *            - default lexical category of object
	 * @return NLGelement
	 */
	public NLGElement createNLGElement(Object element, LexicalCategory category) {
		if(element == null)
			return null;

		// InflectedWordElement - return underlying word
		else if(element instanceof InflectedWordElement)
			return ((InflectedWordElement) element).getBaseWord();

		// StringElement - look up in lexicon if it is a word
		// otherwise return element
		else if(element instanceof StringElement) {
			if(stringIsWord(((StringElement) element).getRealisation(), category))
				return createWord(((StringElement) element).getRealisation(), category);
			else
				return (StringElement) element;
		}

		// other NLGElement - return element
		else if(element instanceof NLGElement)
			return (NLGElement) element;

		// String - look up in lexicon if a word, otherwise return StringElement
		else if(element instanceof String) {
			if(stringIsWord((String) element, category))
				return createWord(element, category);
			else
				return new StringElement((String) element);
		}

		throw new IllegalArgumentException(element.toString() + " is not a valid type");
	}

	/**
	 * return true if string is a word
	 * 
	 * @param string
	 * @param category
	 * @return
	 */
	private boolean stringIsWord(String string, LexicalCategory category) {
		return lexicon != null
		       && (lexicon.hasWord(string, category) || PRONOUNS.contains(string) || (string.matches(WORD_REGEX)));
	}

	/**
	 * create an NLGElement from the element, no default lexical category
	 * 
	 * @param element
	 * @return NLGelement
	 */
	public NLGElement createNLGElement(Object element) {
		return createNLGElement(element, LexicalCategory.ANY);
	}

	/**
	 * Creates a blank noun phrase with no subject or specifier.
	 * 
	 * @return a <code>NPPhraseSpec</code> representing this phrase.
	 */
	public NPPhraseSpec createNounPhrase() {
		return createNounPhrase(null, null);
	}

	/**
	 * Creates a noun phrase with the given subject but no specifier.
	 * 
	 * @param noun
	 *            the subject of the phrase.
	 * @return a <code>NPPhraseSpec</code> representing this phrase.
	 */
	public NPPhraseSpec createNounPhrase(Object noun) {
		if(noun instanceof NPPhraseSpec)
			return (NPPhraseSpec) noun;
		else
			return createNounPhrase(null, noun);
	}

	/**
	 * Creates a noun phrase with the given specifier and subject.
	 * 
	 * @param specifier
	 *            the specifier or determiner for the noun phrase.
	 * @param noun
	 *            the subject of the phrase.
	 * @return a <code>NPPhraseSpec</code> representing this phrase.
	 */
	public NPPhraseSpec createNounPhrase(Object specifier, Object noun) {
		if(noun instanceof NPPhraseSpec)
			return (NPPhraseSpec) noun;

		NPPhraseSpec phraseElement = new NPPhraseSpec(this);
		NLGElement nounElement = createNLGElement(noun, LexicalCategory.NOUN);
		setPhraseHead(phraseElement, nounElement);

		if(specifier != null)
			phraseElement.setSpecifier(specifier);

		return phraseElement;
	}

	/**
	 * A helper method to set the head feature of the phrase.
	 * 
	 * @param phraseElement
	 *            the phrase element.
	 * @param headElement
	 *            the head element.
	 */
	private void setPhraseHead(PhraseElement phraseElement, NLGElement headElement) {
		if(headElement != null) {
			phraseElement.setHead(headElement);
			headElement.setParent(phraseElement);
		}
	}

	/**
	 * Creates a blank adjective phrase with no base adjective set.
	 * 
	 * @return a <code>AdjPhraseSpec</code> representing this phrase.
	 */
	public AdjPhraseSpec createAdjectivePhrase() {
		return createAdjectivePhrase(null);
	}

	/**
	 * Creates an adjective phrase wrapping the given adjective.
	 * 
	 * @param adjective
	 *            the main adjective for this phrase.
	 * @return a <code>AdjPhraseSpec</code> representing this phrase.
	 */
	public AdjPhraseSpec createAdjectivePhrase(Object adjective) {
		AdjPhraseSpec phraseElement = new AdjPhraseSpec(this);

		NLGElement adjectiveElement = createNLGElement(adjective, LexicalCategory.ADJECTIVE);
		setPhraseHead(phraseElement, adjectiveElement);

		return phraseElement;
	}

	/**
	 * Creates a blank verb phrase with no main verb.
	 * 
	 * @return a <code>VPPhraseSpec</code> representing this phrase.
	 */
	public VPPhraseSpec createVerbPhrase() {
		return createVerbPhrase(null);
	}

	/**
	 * Creates a verb phrase wrapping the main verb given. If a
	 * <code>String</code> is passed in then some parsing is done to see if the
	 * verb contains a particle, for example <em>fall down</em>. The first word
	 * is taken to be the verb while all other words are assumed to form the
	 * particle.
	 * 
	 * @param verb
	 *            the verb to be wrapped.
	 * @return a <code>VPPhraseSpec</code> representing this phrase.
	 */
	public VPPhraseSpec createVerbPhrase(Object verb) {
		VPPhraseSpec phraseElement = new VPPhraseSpec(this);
		phraseElement.setVerb(verb);
		setPhraseHead(phraseElement, phraseElement.getVerb());
		return phraseElement;
	}

	/**
	 * Creates a blank adverb phrase that has no adverb.
	 * 
	 * @return a <code>AdvPhraseSpec</code> representing this phrase.
	 */
	public AdvPhraseSpec createAdverbPhrase() {
		return createAdverbPhrase(null);
	}

	/**
	 * Creates an adverb phrase wrapping the given adverb.
	 * 
	 * @param adverb
	 *            the adverb for this phrase.
	 * @return a <code>AdvPhraseSpec</code> representing this phrase.
	 */
	public AdvPhraseSpec createAdverbPhrase(String adverb) {
		AdvPhraseSpec phraseElement = new AdvPhraseSpec(this);

		NLGElement adverbElement = createNLGElement(adverb, LexicalCategory.ADVERB);
		setPhraseHead(phraseElement, adverbElement);
		return phraseElement;
	}

	/**
	 * Creates a blank clause with no subject, verb or objects.
	 * 
	 * @return a <code>SPhraseSpec</code> representing this phrase.
	 */
	public SPhraseSpec createClause() {
		return createClause(null, null, null);
	}

	/**
	 * Creates a clause with the given subject and verb but no objects.
	 * 
	 * @param subject
	 *            the subject for the clause as a <code>NLGElement</code> or
	 *            <code>String</code>. This forms a noun phrase.
	 * @param verb
	 *            the verb for the clause as a <code>NLGElement</code> or
	 *            <code>String</code>. This forms a verb phrase.
	 * @return a <code>SPhraseSpec</code> representing this phrase.
	 */
	public SPhraseSpec createClause(Object subject, Object verb) {
		return createClause(subject, verb, null);
	}

	/**
	 * Creates a clause with the given subject, verb or verb phrase and direct
	 * object but no indirect object.
	 * 
	 * @param subject
	 *            the subject for the clause as a <code>NLGElement</code> or
	 *            <code>String</code>. This forms a noun phrase.
	 * @param verb
	 *            the verb for the clause as a <code>NLGElement</code> or
	 *            <code>String</code>. This forms a verb phrase.
	 * @param directObject
	 *            the direct object for the clause as a <code>NLGElement</code>
	 *            or <code>String</code>. This forms a complement for the
	 *            clause.
	 * @return a <code>SPhraseSpec</code> representing this phrase.
	 */
	public SPhraseSpec createClause(Object subject, Object verb, Object directObject) {

		SPhraseSpec phraseElement = new SPhraseSpec(this);

		if(verb != null) {
			// AG: fix here: check if "verb" is a VPPhraseSpec or a Verb
			if(verb instanceof PhraseElement) {
				phraseElement.setVerbPhrase((PhraseElement) verb);
			} else {
				phraseElement.setVerb(verb);
			}
		}

		if(subject != null)
			phraseElement.setSubject(subject);

		if(directObject != null) {
			phraseElement.setObject(directObject);
		}

		return phraseElement;
	}

	/*	*//**
	      * A helper method to set the verb phrase for a clause.
	      * 
	      * @param baseForm
	      *            the base form of the clause.
	      * @param verbPhrase
	      *            the verb phrase to be used in the clause.
	      * @param phraseElement
	      *            the current representation of the clause.
	      */
	/*
	 * private void setVerbPhrase(StringBuffer baseForm, NLGElement verbPhrase,
	 * PhraseElement phraseElement) { if (baseForm.length() > 0) {
	 * baseForm.append(' '); }
	 * baseForm.append(verbPhrase.getFeatureAsString(Feature.BASE_FORM));
	 * phraseElement.setFeature(Feature.VERB_PHRASE, verbPhrase);
	 * verbPhrase.setParent(phraseElement);
	 * verbPhrase.setFeature(Feature.DISCOURSE_FUNCTION,
	 * DiscourseFunction.VERB_PHRASE); if
	 * (phraseElement.hasFeature(Feature.GENDER)) {
	 * verbPhrase.setFeature(Feature.GENDER, phraseElement
	 * .getFeature(Feature.GENDER)); } if
	 * (phraseElement.hasFeature(Feature.NUMBER)) {
	 * verbPhrase.setFeature(Feature.NUMBER, phraseElement
	 * .getFeature(Feature.NUMBER)); } if
	 * (phraseElement.hasFeature(Feature.PERSON)) {
	 * verbPhrase.setFeature(Feature.PERSON, phraseElement
	 * .getFeature(Feature.PERSON)); } }
	 *//**
	   * A helper method to add the direct object to the clause.
	   * 
	   * @param baseForm
	   *            the base form for the clause.
	   * @param directObject
	   *            the direct object to be added.
	   * @param phraseElement
	   *            the current representation of this clause.
	   * @param function
	   *            the discourse function for this object.
	   */
	/*
	 * private void setObject(StringBuffer baseForm, Object object,
	 * PhraseElement phraseElement, DiscourseFunction function) { if
	 * (baseForm.length() > 0) { baseForm.append(' '); } if (object instanceof
	 * NLGElement) { phraseElement.addComplement((NLGElement) object);
	 * baseForm.append(((NLGElement) object)
	 * .getFeatureAsString(Feature.BASE_FORM));
	 * 
	 * ((NLGElement) object).setFeature(Feature.DISCOURSE_FUNCTION, function);
	 * 
	 * if (((NLGElement) object).hasFeature(Feature.NUMBER)) {
	 * phraseElement.setFeature(Feature.NUMBER, ((NLGElement) object)
	 * .getFeature(Feature.NUMBER)); } } else if (object instanceof String) {
	 * NLGElement complementElement = createNounPhrase(object);
	 * phraseElement.addComplement(complementElement);
	 * complementElement.setFeature(Feature.DISCOURSE_FUNCTION, function);
	 * baseForm.append((String) object); } }
	 */
	/*	*//**
	      * A helper method that sets the subjects on a clause.
	      * 
	      * @param phraseElement
	      *            the element representing the clause.
	      * @param subjectPhrase
	      *            the subject phrase for the clause.
	      * @param baseForm
	      *            the base form for the clause.
	      */
	/*
	 * private void setPhraseSubjects(PhraseElement phraseElement, NLGElement
	 * subjectPhrase, StringBuffer baseForm) {
	 * subjectPhrase.setParent(phraseElement); List<NLGElement> allSubjects =
	 * new ArrayList<NLGElement>(); allSubjects.add(subjectPhrase);
	 * phraseElement.setFeature(Feature.SUBJECTS, allSubjects);
	 * baseForm.append(subjectPhrase.getFeatureAsString(Feature.BASE_FORM));
	 * subjectPhrase.setFeature(Feature.DISCOURSE_FUNCTION,
	 * DiscourseFunction.SUBJECT);
	 * 
	 * if (subjectPhrase.hasFeature(Feature.GENDER)) {
	 * phraseElement.setFeature(Feature.GENDER, subjectPhrase
	 * .getFeature(Feature.GENDER)); } if
	 * (subjectPhrase.hasFeature(Feature.NUMBER)) {
	 * phraseElement.setFeature(Feature.NUMBER, subjectPhrase
	 * .getFeature(Feature.NUMBER));
	 * 
	 * } if (subjectPhrase.hasFeature(Feature.PERSON)) {
	 * phraseElement.setFeature(Feature.PERSON, subjectPhrase
	 * .getFeature(Feature.PERSON)); } }
	 */
	/**
	 * Creates a blank canned text phrase with no text.
	 * 
	 * @return a <code>PhraseElement</code> representing this phrase.
	 */
	public NLGElement createStringElement() {
		return createStringElement(null);
	}

	/**
	 * Creates a canned text phrase with the given text.
	 * 
	 * @param text
	 *            the canned text.
	 * @return a <code>PhraseElement</code> representing this phrase.
	 */
	public NLGElement createStringElement(String text) {
		return new StringElement(text);
	}

	/**
	 * Creates a new (empty) coordinated phrase
	 * 
	 * @return empty <code>CoordinatedPhraseElement</code>
	 */
	public CoordinatedPhraseElement createCoordinatedPhrase() {
		return new CoordinatedPhraseElement();
	}

	/**
	 * Creates a new coordinated phrase with two elements (initially)
	 * 
	 * @param coord1
	 *            - first phrase to be coordinated
	 * @param coord2
	 *            = second phrase to be coordinated
	 * @return <code>CoordinatedPhraseElement</code> for the two given elements
	 */
	public CoordinatedPhraseElement createCoordinatedPhrase(Object coord1, Object coord2) {
		return new CoordinatedPhraseElement(coord1, coord2);
	}

	/***********************************************************************************
	 * Document level stuff
	 ***********************************************************************************/

	/**
	 * Creates a new document element with no title.
	 * 
	 * @return a <code>DocumentElement</code>
	 */
	public DocumentElement createDocument() {
		return createDocument(null);
	}

	/**
	 * Creates a new document element with the given title.
	 * 
	 * @param title
	 *            the title for this element.
	 * @return a <code>DocumentElement</code>.
	 */
	public DocumentElement createDocument(String title) {
		return new DocumentElement(DocumentCategory.DOCUMENT, title);
	}

	/**
	 * Creates a new document element with the given title and adds all of the
	 * given components in the list
	 * 
	 * @param title
	 *            the title of this element.
	 * @param components
	 *            a <code>List</code> of <code>NLGElement</code>s that form the
	 *            components of this element.
	 * @return a <code>DocumentElement</code>
	 */
	public DocumentElement createDocument(String title, List<DocumentElement> components) {

		DocumentElement document = new DocumentElement(DocumentCategory.DOCUMENT, title);
		if(components != null) {
			document.addComponents(components);
		}
		return document;
	}

	/**
	 * Creates a new document element with the given title and adds the given
	 * component.
	 * 
	 * @param title
	 *            the title for this element.
	 * @param component
	 *            an <code>NLGElement</code> that becomes the first component of
	 *            this document element.
	 * @return a <code>DocumentElement</code>
	 */
	public DocumentElement createDocument(String title, NLGElement component) {
		DocumentElement element = new DocumentElement(DocumentCategory.DOCUMENT, title);

		if(component != null) {
			element.addComponent(component);
		}
		return element;
	}

	/**
	 * Creates a new list element with no components.
	 * 
	 * @return a <code>DocumentElement</code> representing the list.
	 */
	public DocumentElement createList() {
		return new DocumentElement(DocumentCategory.LIST, null);
	}

	/**
	 * Creates a new list element and adds all of the given components in the
	 * list
	 * 
	 * @param textComponents
	 *            a <code>List</code> of <code>NLGElement</code>s that form the
	 *            components of this element.
	 * @return a <code>DocumentElement</code> representing the list.
	 */
	public DocumentElement createList(List<DocumentElement> textComponents) {
		DocumentElement list = new DocumentElement(DocumentCategory.LIST, null);
		list.addComponents(textComponents);
		return list;
	}

	/**
	 * Creates a new section element with the given title and adds the given
	 * component.
	 * 
	 * @param component
	 *            an <code>NLGElement</code> that becomes the first component of
	 *            this document element.
	 * @return a <code>DocumentElement</code> representing the section.
	 */
	public DocumentElement createList(NLGElement component) {
		DocumentElement list = new DocumentElement(DocumentCategory.LIST, null);
		list.addComponent(component);
		return list;
	}

	/**
	 * Creates a new enumerated list element with no components.
	 * 
	 * @return a <code>DocumentElement</code> representing the list.
	 * @author Rodrigo de Oliveira - Data2Text Ltd
	 */
	public DocumentElement createEnumeratedList() {
		return new DocumentElement(DocumentCategory.ENUMERATED_LIST, null);
	}

	/**
	 * Creates a new enumerated list element and adds all of the given components in the
	 * list
	 * 
	 * @param textComponents
	 *            a <code>List</code> of <code>NLGElement</code>s that form the
	 *            components of this element.
	 * @return a <code>DocumentElement</code> representing the list.
	 * @author Rodrigo de Oliveira - Data2Text Ltd
	 */
	public DocumentElement createEnumeratedList(List<DocumentElement> textComponents) {
		DocumentElement list = new DocumentElement(DocumentCategory.ENUMERATED_LIST, null);
		list.addComponents(textComponents);
		return list;
	}

	/**
	 * Creates a new section element with the given title and adds the given
	 * component.
	 * 
	 * @param component
	 *            an <code>NLGElement</code> that becomes the first component of
	 *            this document element.
	 * @return a <code>DocumentElement</code> representing the section.
	 * @author Rodrigo de Oliveira - Data2Text Ltd
	 */
	public DocumentElement createEnumeratedList(NLGElement component) {
		DocumentElement list = new DocumentElement(DocumentCategory.ENUMERATED_LIST, null);
		list.addComponent(component);
		return list;
	}

	/**
	 * Creates a list item for adding to a list element.
	 * 
	 * @return a <code>DocumentElement</code> representing the list item.
	 */
	public DocumentElement createListItem() {
		return new DocumentElement(DocumentCategory.LIST_ITEM, null);
	}

	/**
	 * Creates a list item for adding to a list element. The list item has the
	 * given component.
	 * 
	 * @return a <code>DocumentElement</code> representing the list item.
	 */
	public DocumentElement createListItem(NLGElement component) {
		DocumentElement listItem = new DocumentElement(DocumentCategory.LIST_ITEM, null);
		listItem.addComponent(component);
		return listItem;
	}

	/**
	 * Creates a new paragraph element with no components.
	 * 
	 * @return a <code>DocumentElement</code> representing this paragraph
	 */
	public DocumentElement createParagraph() {
		return new DocumentElement(DocumentCategory.PARAGRAPH, null);
	}

	/**
	 * Creates a new paragraph element and adds all of the given components in
	 * the list
	 * 
	 * @param components
	 *            a <code>List</code> of <code>NLGElement</code>s that form the
	 *            components of this element.
	 * @return a <code>DocumentElement</code> representing this paragraph
	 */
	public DocumentElement createParagraph(List<DocumentElement> components) {
		DocumentElement paragraph = new DocumentElement(DocumentCategory.PARAGRAPH, null);
		if(components != null) {
			paragraph.addComponents(components);
		}
		return paragraph;
	}

	/**
	 * Creates a new paragraph element and adds the given component
	 * 
	 * @param component
	 *            an <code>NLGElement</code> that becomes the first component of
	 *            this document element.
	 * @return a <code>DocumentElement</code> representing this paragraph
	 */
	public DocumentElement createParagraph(NLGElement component) {
		DocumentElement paragraph = new DocumentElement(DocumentCategory.PARAGRAPH, null);
		if(component != null) {
			paragraph.addComponent(component);
		}
		return paragraph;
	}

	/**
	 * Creates a new section element.
	 * 
	 * @return a <code>DocumentElement</code> representing the section.
	 */
	public DocumentElement createSection() {
		return new DocumentElement(DocumentCategory.SECTION, null);
	}

	/**
	 * Creates a new section element with the given title.
	 * 
	 * @param title
	 *            the title of the section.
	 * @return a <code>DocumentElement</code> representing the section.
	 */
	public DocumentElement createSection(String title) {
		return new DocumentElement(DocumentCategory.SECTION, title);
	}

	/**
	 * Creates a new section element with the given title and adds all of the
	 * given components in the list
	 * 
	 * @param title
	 *            the title of this element.
	 * @param components
	 *            a <code>List</code> of <code>NLGElement</code>s that form the
	 *            components of this element.
	 * @return a <code>DocumentElement</code> representing the section.
	 */
	public DocumentElement createSection(String title, List<DocumentElement> components) {

		DocumentElement section = new DocumentElement(DocumentCategory.SECTION, title);
		if(components != null) {
			section.addComponents(components);
		}
		return section;
	}

	/**
	 * Creates a new section element with the given title and adds the given
	 * component.
	 * 
	 * @param title
	 *            the title for this element.
	 * @param component
	 *            an <code>NLGElement</code> that becomes the first component of
	 *            this document element.
	 * @return a <code>DocumentElement</code> representing the section.
	 */
	public DocumentElement createSection(String title, NLGElement component) {
		DocumentElement section = new DocumentElement(DocumentCategory.SECTION, title);
		if(component != null) {
			section.addComponent(component);
		}
		return section;
	}

	/**
	 * Creates a new sentence element with no components.
	 * 
	 * @return a <code>DocumentElement</code> representing this sentence
	 */
	public DocumentElement createSentence() {
		return new DocumentElement(DocumentCategory.SENTENCE, null);
	}

	/**
	 * Creates a new sentence element and adds all of the given components.
	 * 
	 * @param components
	 *            a <code>List</code> of <code>NLGElement</code>s that form the
	 *            components of this element.
	 * @return a <code>DocumentElement</code> representing this sentence
	 */
	public DocumentElement createSentence(List<NLGElement> components) {
		DocumentElement sentence = new DocumentElement(DocumentCategory.SENTENCE, null);
		sentence.addComponents(components);
		return sentence;
	}

	/**
	 * Creates a new sentence element
	 * 
	 * @param components
	 *            an <code>NLGElement</code> that becomes the first component of
	 *            this document element.
	 * @return a <code>DocumentElement</code> representing this sentence
	 */
	public DocumentElement createSentence(NLGElement components) {
		DocumentElement sentence = new DocumentElement(DocumentCategory.SENTENCE, null);
		sentence.addComponent(components);
		return sentence;
	}

	/**
	 * Creates a sentence with the given subject and verb. The phrase factory is
	 * used to construct a clause that then forms the components of the
	 * sentence.
	 * 
	 * @param subject
	 *            the subject of the sentence.
	 * @param verb
	 *            the verb of the sentence.
	 * @return a <code>DocumentElement</code> representing this sentence
	 */
	public DocumentElement createSentence(Object subject, Object verb) {
		return createSentence(subject, verb, null);
	}

	/**
	 * Creates a sentence with the given subject, verb and direct object. The
	 * phrase factory is used to construct a clause that then forms the
	 * components of the sentence.
	 * 
	 * @param subject
	 *            the subject of the sentence.
	 * @param verb
	 *            the verb of the sentence.
	 * @param complement
	 *            the object of the sentence.
	 * @return a <code>DocumentElement</code> representing this sentence
	 */
	public DocumentElement createSentence(Object subject, Object verb, Object complement) {

		DocumentElement sentence = new DocumentElement(DocumentCategory.SENTENCE, null);
		sentence.addComponent(createClause(subject, verb, complement));
		return sentence;
	}

	/**
	 * Creates a new sentence with the given canned text. The canned text is
	 * used to form a canned phrase (from the phrase factory) which is then
	 * added as the component to sentence element.
	 * 
	 * @param cannedSentence
	 *            the canned text as a <code>String</code>.
	 * @return a <code>DocumentElement</code> representing this sentence
	 */
	public DocumentElement createSentence(String cannedSentence) {
		DocumentElement sentence = new DocumentElement(DocumentCategory.SENTENCE, null);

		if(cannedSentence != null) {
			sentence.addComponent(createStringElement(cannedSentence));
		}
		return sentence;
	}
}
