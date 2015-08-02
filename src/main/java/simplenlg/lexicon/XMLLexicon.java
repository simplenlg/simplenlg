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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simplenlg.features.Inflection;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;

/**
 * This class loads words from an XML lexicon. All features specified in the
 * lexicon are loaded
 * 
 * @author ereiter
 * 
 */
public class XMLLexicon extends Lexicon {

	// node names in lexicon XML files
	private static final String XML_BASE = "base"; // base form of Word
	private static final String XML_CATEGORY = "category"; // base form of Word
	private static final String XML_ID = "id"; // base form of Word
	private static final String XML_WORD = "word"; // node defining a word

	// lexicon
	private Set<WordElement> words; // set of words
	private Map<String, WordElement> indexByID; // map from ID to word
	private Map<String, List<WordElement>> indexByBase; // map from base to set
	// of words with this
	// baseform
	private Map<String, List<WordElement>> indexByVariant; // map from variants

	// to set of words
	// with this variant

	/**********************************************************************/
	// constructors
	/**********************************************************************/

	/**
	 * Load an XML Lexicon from a named file
	 * 
	 * @param filename
	 */
	public XMLLexicon(String filename) {
		super();
		File file = new File(filename);
		createLexicon(file.toURI());
	}

	/**
	 * Load an XML Lexicon from a File
	 * 
	 * @param file
	 */
	public XMLLexicon(File file) {
		super();
		createLexicon(file.toURI());
	}

	/**
	 * Load an XML Lexicon from a URI
	 * 
	 * @param lexiconURI
	 */
	public XMLLexicon(URI lexiconURI) {
		super();
		createLexicon(lexiconURI);
	}

	public XMLLexicon() {
		try {
			
			URL defaultLexicon = this.getClass().getClassLoader().getResource("default-lexicon.xml");
			
			if(null != defaultLexicon) {
				createLexicon(defaultLexicon.toURI());
			} else {
				createLexicon(this.getClass().getResource(
						"/simplenlg/lexicon/default-lexicon.xml").toURI());
			}
			
		} catch (URISyntaxException ex) {
			System.out.println(ex.toString());
		}
	}

	/**
	 * method to actually load and index the lexicon from a URI
	 * 
	 * @param uri
	 */
	private void createLexicon(URI lexiconURI) {
		// initialise objects
		words = new HashSet<WordElement>();
		indexByID = new HashMap<String, WordElement>();
		indexByBase = new HashMap<String, List<WordElement>>();
		indexByVariant = new HashMap<String, List<WordElement>>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(lexiconURI.toString());

			if (doc != null) {
				Element lexRoot = doc.getDocumentElement();
				NodeList wordNodes = lexRoot.getChildNodes();
				for (int i = 0; i < wordNodes.getLength(); i++) {
					Node wordNode = wordNodes.item(i);
					// ignore things that aren't elements
					if (wordNode.getNodeType() == Node.ELEMENT_NODE) {
						WordElement word = convertNodeToWord(wordNode);
						if (word != null) {
							words.add(word);
							IndexWord(word);
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		addSpecialCases();
	}

	/**
	 * add special cases to lexicon
	 * 
	 */
	private void addSpecialCases() {
		// add variants of "be"
		WordElement be = getWord("be", LexicalCategory.VERB);
		if (be != null) {
			updateIndex(be, "is", indexByVariant);
			updateIndex(be, "am", indexByVariant);
			updateIndex(be, "are", indexByVariant);
			updateIndex(be, "was", indexByVariant);
			updateIndex(be, "were", indexByVariant);
		}
	}

	/**
	 * create a simplenlg WordElement from a Word node in a lexicon XML file
	 * 
	 * @param wordNode
	 * @return
	 * @throws XPathUtilException
	 */
	private WordElement convertNodeToWord(Node wordNode) {
		// if this isn't a Word node, ignore it
		if (!wordNode.getNodeName().equalsIgnoreCase(XML_WORD))
			return null;

		// // if there is no base, flag an error and return null
		// String base = XPathUtil.extractValue(wordNode, Constants.XML_BASE);
		// if (base == null) {
		// System.out.println("Error in loading XML lexicon: Word with no base");
		// return null;
		// }

		// create word
		WordElement word = new WordElement();
		List<Inflection> inflections = new ArrayList<Inflection>();

		// now copy features
		NodeList nodes = wordNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node featureNode = nodes.item(i);

			if (featureNode.getNodeType() == Node.ELEMENT_NODE) {
				String feature = featureNode.getNodeName().trim();
				String value = featureNode.getTextContent();

				if (value != null)
					value = value.trim();

				if (feature == null) {
					System.err.println("Error in XML lexicon node for "
							+ word.toString());
					break;
				}

				if (feature.equalsIgnoreCase(XML_BASE)) {
					word.setBaseForm(value);
				} else if (feature.equalsIgnoreCase(XML_CATEGORY))
					word.setCategory(LexicalCategory.valueOf(value
							.toUpperCase()));
				else if (feature.equalsIgnoreCase(XML_ID))
					word.setId(value);

				else if (value == null || value.equals("")) {
					// if this is an infl code, add it to inflections
					Inflection infl = Inflection.getInflCode(feature);

					if (infl != null) {
						inflections.add(infl);
					} else {
						// otherwise assume it's a boolean feature
						word.setFeature(feature, true);
					}
				} else
					word.setFeature(feature, value);
			}

		}

		// if no infl specified, assume regular
		if (inflections.isEmpty()) {
			inflections.add(Inflection.REGULAR);
		}

		// default inflection code is "reg" if we have it, else random pick form
		// infl codes available
		Inflection defaultInfl = inflections.contains(Inflection.REGULAR) ? Inflection.REGULAR
				: inflections.get(0);		
		
		word.setFeature(LexicalFeature.DEFAULT_INFL, defaultInfl);
		word.setDefaultInflectionalVariant(defaultInfl);
		
		for(Inflection infl: inflections) {
			word.addInflectionalVariant(infl);
		}

		// done, return word
		return word;
	}

	/**
	 * add word to internal indices
	 * 
	 * @param word
	 */
	private void IndexWord(WordElement word) {
		// first index by base form
		String base = word.getBaseForm();
		// shouldn't really need is, as all words have base forms
		if (base != null) {
			updateIndex(word, base, indexByBase);
		}

		// now index by ID, which should be unique (if present)
		String id = word.getId();
		if (id != null) {
			if (indexByID.containsKey(id))
				System.out.println("Lexicon error: ID " + id
						+ " occurs more than once");
			indexByID.put(id, word);
		}

		// now index by variant
		for (String variant : getVariants(word)) {
			updateIndex(word, variant, indexByVariant);
		}

		// done
	}

	/**
	 * convenience method to update an index
	 * 
	 * @param word
	 * @param base
	 * @param index
	 */
	private void updateIndex(WordElement word, String base,
			Map<String, List<WordElement>> index) {
		if (!index.containsKey(base))
			index.put(base, new ArrayList<WordElement>());
		index.get(base).add(word);
	}

	/******************************************************************************************/
	// main methods to get data from lexicon
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#getWords(java.lang.String,
	 * simplenlg.features.LexicalCategory)
	 */
	@Override
	public List<WordElement> getWords(String baseForm, LexicalCategory category) {
		return getWordsFromIndex(baseForm, category, indexByBase);
	}

	/**
	 * get matching keys from an index map
	 * 
	 * @param indexKey
	 * @param category
	 * @param indexMap
	 * @return
	 */
	private List<WordElement> getWordsFromIndex(String indexKey,
			LexicalCategory category, Map<String, List<WordElement>> indexMap) {
		List<WordElement> result = new ArrayList<WordElement>();

		// case 1: unknown, return empty list
		if (!indexMap.containsKey(indexKey)) {
			return result;
		}

		// case 2: category is ANY, return everything
		if (category == LexicalCategory.ANY) {
			for(WordElement word : indexMap.get(indexKey)) {
				result.add(new WordElement(word));
			}
			return result;
		}
		else {
			// case 3: other category, search for match
			for (WordElement word : indexMap.get(indexKey)) {
				if (word.getCategory() == category) {
					result.add(new WordElement(word));
				}
			}
		}	
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#getWordsByID(java.lang.String)
	 */
	@Override
	public List<WordElement> getWordsByID(String id) {
		List<WordElement> result = new ArrayList<WordElement>();
		if (indexByID.containsKey(id)) {
			result.add(new WordElement(indexByID.get(id)));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.Lexicon#getWordsFromVariant(java.lang.String,
	 * simplenlg.features.LexicalCategory)
	 */
	@Override
	public List<WordElement> getWordsFromVariant(String variant,
			LexicalCategory category) {
		return getWordsFromIndex(variant, category, indexByVariant);
	}

	/**
	 * quick-and-dirty routine for getting morph variants should be replaced by
	 * something better!
	 * 
	 * @param word
	 * @return
	 */
	private Set<String> getVariants(WordElement word) {
		Set<String> variants = new HashSet<String>();
		variants.add(word.getBaseForm());
		ElementCategory category = word.getCategory();
		if (category instanceof LexicalCategory) {
			switch ((LexicalCategory) category) {
			case NOUN:
				variants.add(getVariant(word, LexicalFeature.PLURAL, "s"));
				break;

			case ADJECTIVE:
				variants
						.add(getVariant(word, LexicalFeature.COMPARATIVE, "er"));
				variants
						.add(getVariant(word, LexicalFeature.SUPERLATIVE, "est"));
				break;

			case VERB:
				variants.add(getVariant(word, LexicalFeature.PRESENT3S, "s"));
				variants.add(getVariant(word, LexicalFeature.PAST, "ed"));
				variants.add(getVariant(word, LexicalFeature.PAST_PARTICIPLE,
						"ed"));
				variants.add(getVariant(word,
						LexicalFeature.PRESENT_PARTICIPLE, "ing"));
				break;

			default:
				// only base needed for other forms
				break;
			}
		}
		return variants;
	}

	/**
	 * quick-and-dirty routine for computing morph forms Should be replaced by
	 * something better!
	 * 
	 * @param word
	 * @param feature
	 * @param string
	 * @return
	 */
	private String getVariant(WordElement word, String feature, String suffix) {
		if (word.hasFeature(feature))
			return word.getFeatureAsString(feature);
		else
			return getForm(word.getBaseForm(), suffix);
	}

	/**
	 * quick-and-dirty routine for standard orthographic changes Should be
	 * replaced by something better!
	 * 
	 * @param base
	 * @param suffix
	 * @return
	 */
	private String getForm(String base, String suffix) {
		// add a suffix to a base form, with orthographic changes

		// rule 1 - convert final "y" to "ie" if suffix does not start with "i"
		// eg, cry + s = cries , not crys
		if (base.endsWith("y") && !suffix.startsWith("i"))
			base = base.substring(0, base.length() - 1) + "ie";

		// rule 2 - drop final "e" if suffix starts with "e" or "i"
		// eg, like+ed = liked, not likeed
		if (base.endsWith("e")
				&& (suffix.startsWith("e") || suffix.startsWith("i")))
			base = base.substring(0, base.length() - 1);

		// rule 3 - insert "e" if suffix is "s" and base ends in s, x, z, ch, sh
		// eg, watch+s -> watches, not watchs
		if (suffix.startsWith("s")
				&& (base.endsWith("s") || base.endsWith("x")
						|| base.endsWith("z") || base.endsWith("ch") || base
						.endsWith("sh")))
			base = base + "e";

		// have made changes, now append and return
		return base + suffix; // eg, want + s = wants
	}
}
