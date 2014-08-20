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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import simplenlg.features.Inflection;
import simplenlg.features.LexicalFeature;

/**
 * This is the class for a lexical entry (ie, a word). Words are stored in a
 * {@link simplenlg.lexicon.Lexicon}, and usually the developer retrieves a
 * WordElement via a lookup method in the lexicon
 * 
 * Words always have a base form, and usually have a
 * {@link simplenlg.framework.LexicalCategory}. They may also have a Lexicon ID.
 * 
 * Words also have features (which are retrieved from the Lexicon), these are
 * held in the standard NLGElement feature map
 * 
 * @author E. Reiter, University of Aberdeen.
 * @version 4.0
 */

public class WordElement extends NLGElement {

	/*
	 * Internal class. This maintains inflectional variants of the word, which
	 * may be available in the lexicon. For example, a word may have both a
	 * regular and an irregular variant. If the desired type is the irregular,
	 * it is convenient to have the set of irregular inflected forms available
	 * without necessitating a new call to the lexicon to get the forms.
	 */
	private class InflectionSet {
		// the infl type
		@SuppressWarnings("unused")
		Inflection infl;

		// the forms, mapping values of LexicalFeature to actual word forms
		Map<String, String> forms;

		InflectionSet(Inflection infl) {
			this.infl = infl;
			this.forms = new HashMap<String, String>();
		}

		/*
		 * set an inflectional form
		 * 
		 * @param feature
		 * 
		 * @param form
		 */
		void addForm(String feature, String form) {
			this.forms.put(feature, form);
		}

		/*
		 * get an inflectional form
		 */
		String getForm(String feature) {
			return this.forms.get(feature);
		}
	}

	// Words have baseForm, category, id, and features
	// features are inherited from NLGElement

	String baseForm; // base form, eg "dog". currently also in NLG Element, but
	// will be removed from there

	String id; // id in lexicon (may be null);

	Map<Inflection, InflectionSet> inflVars; // the inflectional variants

	Inflection defaultInfl; // the default inflectional variant

	// LexicalCategory category; // type of word

	/**********************************************************/
	// constructors
	/**********************************************************/

	/**
	 * empty constructor
	 * 
	 */
	public WordElement() {
		this(null, LexicalCategory.ANY, null);
		// this.baseForm = null;
		// setCategory(LexicalCategory.ANY);
		// this.id = null;
	}

	/**
	 * create a WordElement with the specified baseForm (no category or ID
	 * specified)
	 * 
	 * @param baseForm
	 *            - base form of WordElement
	 */
	public WordElement(String baseForm) {
		// super();
		// this.baseForm = baseForm;
		// setCategory(LexicalCategory.ANY);
		// this.id = null;
		this(baseForm, LexicalCategory.ANY, null);
	}

	/**
	 * create a WordElement with the specified baseForm and category
	 * 
	 * @param baseForm
	 *            - base form of WordElement
	 * @param category
	 *            - category of WordElement
	 */
	public WordElement(String baseForm, LexicalCategory category) {
		// super();
		// this.baseForm = baseForm;
		// setCategory(category);
		// this.id = null;
		this(baseForm, category, null);
	}

	/**
	 * create a WordElement with the specified baseForm, category, ID
	 * 
	 * @param baseForm
	 *            - base form of WordElement
	 * @param category
	 *            - category of WordElement
	 * @param id
	 *            - ID of word in lexicon
	 */
	public WordElement(String baseForm, LexicalCategory category, String id) {
		super();
		this.baseForm = baseForm;
		setCategory(category);
		this.id = id;
		this.inflVars = new HashMap<Inflection, InflectionSet>();
	}

	/**********************************************************/
	// getters and setters
	/**********************************************************/

	/**
	 * @return the baseForm
	 */
	public String getBaseForm() {
		return this.baseForm;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param baseForm
	 *            the baseForm to set
	 */
	public void setBaseForm(String baseForm) {
		this.baseForm = baseForm;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Set the default inflectional variant of a word. This is mostly relevant
	 * if the word has more than one possible inflectional variant (for example,
	 * it can be inflected in both a regular and irregular way).
	 * 
	 * <P>
	 * If the default inflectional variant is set, the inflectional forms of the
	 * word may change as a result. This depends on whether inflectional forms
	 * have been specifically associated with this variant, via
	 * {@link #addInflectionalVariant(Inflection, String, String)}.
	 * 
	 * <P>
	 * The <code>NIHDBLexicon</code> associates different inflectional variants
	 * with words, if they are so specified, and adds the correct forms.
	 * 
	 * @param variant
	 *            The variant
	 */
	public void setDefaultInflectionalVariant(Inflection variant) {
		setFeature(LexicalFeature.DEFAULT_INFL, variant);
		this.defaultInfl = variant;

		if (this.inflVars.containsKey(variant)) {
			InflectionSet set = inflVars.get(variant);
			String[] forms = LexicalFeature.getInflectionalFeatures(this
					.getCategory());

			if (forms != null) {
				for (String f : forms) {
					setFeature(f, set.getForm(f));
				}
			}
		}
	}

	/**
	 * @return the default inflectional variant
	 */
	public Object getDefaultInflectionalVariant() {
		// return getFeature(LexicalFeature.DEFAULT_INFL);
		return this.defaultInfl;
	}

	/*
	 * Convenience method to get all the inflectional forms of the word.
	 * Equivalent to
	 * <code>getFeatureAsStringList(LexicalFeature.INFLECTIONS)</code>.
	 * 
	 * @return the list of inflectional variants
	 */
	// public List<Object> getInflectionalVariants() {
	// return getFeatureAsList(LexicalFeature.INFLECTIONS);
	// }

	/*
	 * Convenience method to get all the spelling variants of the word.
	 * Equivalent to
	 * <code>getFeatureAsStringList(LexicalFeature.SPELL_VARS)</code>.
	 * 
	 * @return the list of spelling variants
	 */
	// public List<String> getSpellingVariants() {
	// return getFeatureAsStringList(LexicalFeature.SPELL_VARS);
	// }

	/**
	 * Convenience method to set the default spelling variant of a word.
	 * Equivalent to
	 * <code>setFeature(LexicalFeature.DEFAULT_SPELL, variant)</code>.
	 * 
	 * <P>
	 * By default, the spelling variant used is the base form. If otherwise set,
	 * this forces the realiser to always use the spelling variant specified.
	 * 
	 * @param variant
	 *            The spelling variant
	 */
	public void setDefaultSpellingVariant(String variant) {
		setFeature(LexicalFeature.DEFAULT_SPELL, variant);
	}

	/**
	 * Convenience method, equivalent to
	 * <code>getFeatureAsString(LexicalFeature.DEFAULT_SPELL)</code>. If this
	 * feature is not set, the baseform is returned.
	 * 
	 * @return the default inflectional variant
	 */
	public String getDefaultSpellingVariant() {
		String defSpell = getFeatureAsString(LexicalFeature.DEFAULT_SPELL);
		return defSpell == null ? this.getBaseForm() : defSpell;
	}

	/*
	 * @param category the category to set
	 */
	// public void setCategory(LexicalCategory category) {
	// this.category = category;
	// }

	/**
	 * Add an inflectional variant to this word element. This method is intended
	 * for use by a <code>Lexicon</code>. The idea is that words which have more
	 * than one inflectional variant (for example, a regular and an irregular
	 * form of the past tense), can have a default variant (for example, the
	 * regular), but also store information about the other variants. This comes
	 * in useful in case the default inflectional variant is reset to a new one.
	 * In that case, the stored forms for the new variant are used to inflect
	 * the word.
	 * 
	 * <P>
	 * <strong>An example:</strong> The verb <i>lie</i> has both a regular form
	 * (<I>lies, lied, lying</I>) and an irregular form (<I>lay, lain,</I> etc).
	 * Assume that the <code>Lexicon</code> provides this information and treats
	 * this as variant information of the same word (as does the
	 * <code>NIHDBLexicon</code>, for example). Typically, the default
	 * inflectional variant is the <code>Inflection.REGULAR</code>. This means
	 * that morphology proceeds to inflect the verb as <I>lies, lying</I> and so
	 * on. If the default inflectional variant is reset to
	 * <code>Inflection.IRREGULAR</code>, the stored irregular forms will be
	 * used instead.
	 * 
	 * @param infl
	 *            the Inflection pattern with which this form is associated
	 * @param lexicalFeature
	 *            the actual inflectional feature being set, for example
	 *            <code>LexicalFeature.PRESENT_3S</code>
	 * @param form
	 *            the actual inflected word form
	 */
	public void addInflectionalVariant(Inflection infl, String lexicalFeature,
			String form) {
		if (this.inflVars.containsKey(infl)) {
			this.inflVars.get(infl).addForm(lexicalFeature, form);
		} else {
			InflectionSet set = new InflectionSet(infl);
			set.addForm(lexicalFeature, form);
			this.inflVars.put(infl, set);
		}
	}

	/**
	 * Specify that this word has an inflectional variant (e.g. irregular)
	 * 
	 * @param infl
	 *            the variant
	 */
	public void addInflectionalVariant(Inflection infl) {
		this.inflVars.put(infl, new InflectionSet(infl));
	}

	/**
	 * Check whether this word has a particular inflectional variant
	 * 
	 * @param infl
	 *            the variant
	 * @return <code>true</code> if this word has the variant
	 */
	public boolean hasInflectionalVariant(Inflection infl) {
		return this.inflVars.containsKey(infl);
	}

	/**********************************************************/
	// other methods
	/**********************************************************/

	@Override
	public String toString() {
		ElementCategory _category = getCategory();
		StringBuffer buffer = new StringBuffer("WordElement["); //$NON-NLS-1$
		buffer.append(getBaseForm()).append(':');
		if (_category != null) {
			buffer.append(_category.toString());
		} else {
			buffer.append("no category"); //$NON-NLS-1$
		}
		buffer.append(']');
		return buffer.toString();
	}

	public String toXML() {
		String xml = String.format("<word>%n"); //$NON-NLS-1$
		if (getBaseForm() != null)
			xml = xml + String.format("  <base>%s</base>%n", getBaseForm()); //$NON-NLS-1$
		if (getCategory() != LexicalCategory.ANY)
			xml = xml + String.format("  <category>%s</category>%n", //$NON-NLS-1$
					getCategory().toString().toLowerCase());
		if (getId() != null)
			xml = xml + String.format("  <id>%s</id>%n", getId()); //$NON-NLS-1$

		SortedSet<String> featureNames = new TreeSet<String>(
				getAllFeatureNames()); // list features in alpha order
		for (String feature : featureNames) {
			Object value = getFeature(feature);
			if (value != null) { // ignore null features
				if (value instanceof Boolean) { // booleans ignored if false,
					// shown as <XX/> if true
					boolean bvalue = ((Boolean) value).booleanValue();
					if (bvalue)
						xml = xml + String.format("  <%s/>%n", feature); //$NON-NLS-1$
				} else { // otherwise include feature and value
					xml = xml + String.format("  <%s>%s</%s>%n", feature, value //$NON-NLS-1$
							.toString(), feature);
				}
			}

		}
		xml = xml + String.format("</word>%n"); //$NON-NLS-1$
		return xml;
	}

	/**
	 * This method returns an empty <code>List</code> as words do not have child
	 * elements.
	 */
	@Override
	public List<NLGElement> getChildren() {
		return new ArrayList<NLGElement>();
	}

	@Override
	public String printTree(String indent) {
		StringBuffer print = new StringBuffer();
		print.append("WordElement: base=").append(getBaseForm()) //$NON-NLS-1$
				.append(", category=").append(getCategory().toString()) //$NON-NLS-1$
				.append(", ").append(super.toString()).append('\n'); //$NON-NLS-1$
		return print.toString();
	}

	/**
	 * Check if this WordElement is equal to an object.
	 * 
	 * @param o
	 *            the object
	 * @return <code>true</code> iff the object is a word element with the same
	 *         id and the same baseform and the same features.
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof WordElement) {
			WordElement we = (WordElement) o;

			return (this.baseForm == we.baseForm || this.baseForm
					.equals(we.baseForm))
					&& (this.id == we.id || this.id.equals(we.id))
					&& we.features.equals(this.features);
		}

		return false;
	}
}
