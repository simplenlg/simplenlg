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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;

/**
 * <p>
 * <code>NLGElement</code> is the base class that all elements extend from. This
 * is abstract and cannot therefore be instantiated itself. The additional
 * element classes should be used to correctly identify the type of element
 * required.
 * </p>
 * 
 * <p>
 * Realisation in SimpleNLG revolves around a tree structure. Each node in the
 * tree is represented by a <code>NLGElement</code>, which in turn may have
 * child nodes. The job of the processors is to replace various types of
 * elements with other elements. The eventual goal, once all the processors have
 * been run, is to produce a single string element representing the final
 * realisation.
 * </p>
 * 
 * <p>
 * The features are stored in a <code>Map</code> of <code>String</code> (the
 * feature name) and <code>Object</code> (the value of the feature).
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public abstract class NLGElement {

	/** The category of this element. */
	private ElementCategory category;

	/** The features of this element. */
	protected HashMap<String, Object> features = new HashMap<String, Object>();

	/** The parent of this element. */
	private NLGElement parent;

	/** The realisation of this element. */
	private String realisation;

	/** The NLGFactory which created this element */
	private NLGFactory factory;

	/**
	 * Sets the category of this element.
	 * 
	 * @param newCategory
	 *            the new <code>ElementCategory</code> for this element.
	 */
	public void setCategory(ElementCategory newCategory) {
		this.category = newCategory;
	}

	/**
	 * Retrieves the category for this element.
	 * 
	 * @return the category as a <code>ElementCategory</code>.
	 */
	public ElementCategory getCategory() {
		return this.category;
	}

	/**
	 * Adds a feature to the feature map. If the feature already exists then it
	 * is given the new value. If the value provided is <code>null</code> the
	 * feature is removed from the map.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @param featureValue
	 *            the new value of the feature or <code>null</code> if the
	 *            feature is to be removed.
	 */
	public void setFeature(String featureName, Object featureValue) {
		if (featureName != null) {
			if (featureValue == null) {
				this.features.remove(featureName);
			} else {
				this.features.put(featureName, featureValue);
			}
		}
	}

	/**
	 * A convenience method for setting boolean features.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @param featureValue
	 *            the <code>boolean</code> value of the feature.
	 */
	public void setFeature(String featureName, boolean featureValue) {
		if (featureName != null) {
			this.features.put(featureName, new Boolean(featureValue));
		}
	}

	/**
	 * A convenience method for setting integer features.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @param featureValue
	 *            the <code>int</code> value of the feature.
	 */
	public void setFeature(String featureName, int featureValue) {
		if (featureName != null) {
			this.features.put(featureName, new Integer(featureValue));
		}
	}

	/**
	 * A convenience method for setting long integer features.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @param featureValue
	 *            the <code>long</code> value of the feature.
	 */
	public void setFeature(String featureName, long featureValue) {
		if (featureName != null) {
			this.features.put(featureName, new Long(featureValue));
		}
	}

	/**
	 * A convenience method for setting floating point number features.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @param featureValue
	 *            the <code>float</code> value of the feature.
	 */
	public void setFeature(String featureName, float featureValue) {
		if (featureName != null) {
			this.features.put(featureName, new Float(featureValue));
		}
	}

	/**
	 * A convenience method for setting double precision floating point number
	 * features.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @param featureValue
	 *            the <code>double</code> value of the feature.
	 */
	public void setFeature(String featureName, double featureValue) {
		if (featureName != null) {
			this.features.put(featureName, new Double(featureValue));
		}
	}

	/**
	 * Retrieves the value of the feature.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>Object</code> value of the feature.
	 */
	public Object getFeature(String featureName) {
		return featureName != null ? this.features.get(featureName) : null;
	}

	/**
	 * Retrieves the value of the feature as a string. If the feature doesn't
	 * exist then <code>null</code> is returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>String</code> representation of the value. This is
	 *         taken by calling the object's <code>toString()</code> method.
	 */
	public String getFeatureAsString(String featureName) {
		Object value = getFeature(featureName);
		String stringValue = null;

		if (value != null) {
			stringValue = value.toString();
		}
		return stringValue;
	}

	/**
	 * <p>
	 * Retrieves the value of the feature as a list of elements. If the feature
	 * is a single <code>NLGElement</code> then it is wrapped in a list. If the
	 * feature is a <code>Collection</code> then each object in the collection
	 * is checked and only <code>NLGElement</code>s are returned in the list.
	 * </p>
	 * <p>
	 * If the feature does not exist then an empty list is returned.
	 * </p>
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>List</code> of <code>NLGElement</code>s
	 */
	public List<NLGElement> getFeatureAsElementList(String featureName) {
		List<NLGElement> list = new ArrayList<NLGElement>();

		Object value = getFeature(featureName);
		if (value instanceof NLGElement) {
			list.add((NLGElement) value);
		} else if (value instanceof Collection<?>) {
			Iterator<?> iterator = ((Collection<?>) value).iterator();
			Object nextObject = null;
			while (iterator.hasNext()) {
				nextObject = iterator.next();
				if (nextObject instanceof NLGElement) {
					list.add((NLGElement) nextObject);
				}
			}
		}
		return list;
	}
	
	/**
	 * <p>
	 * Retrieves the value of the feature as a list of java objects. If the feature
	 * is a single element, the list contains only this element.
	 * If the feature is a <code>Collection</code> each object in the collection is
	 * returned in the list.
	 * </p>
	 * <p>
	 * If the feature does not exist then an empty list is returned.
	 * </p>
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>List</code> of <code>Object</code>s
	 */
	public List<Object> getFeatureAsList(String featureName) {
		List<Object> values = new ArrayList<Object>();
		Object value = getFeature(featureName);
		
		if (value != null) {
			if (value instanceof Collection<?>) {
				Iterator<?> iterator = ((Collection<?>) value).iterator();
				Object nextObject = null;
				while (iterator.hasNext()) {
					nextObject = iterator.next();
					values.add(nextObject);
				}
			} else {
				values.add(value);
			}
		}
		
		return values;
	}

	/**
	 * <p>
	 * Retrieves the value of the feature as a list of strings. If the feature
	 * is a single element, then its <code>toString()</code> value is wrapped in
	 * a list. If the feature is a <code>Collection</code> then the
	 * <code>toString()</code> value of each object in the collection is
	 * returned in the list.
	 * </p>
	 * <p>
	 * If the feature does not exist then an empty list is returned.
	 * </p>
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>List</code> of <code>String</code>s
	 */
	public List<String> getFeatureAsStringList(String featureName) {
		List<String> values = new ArrayList<String>();
		Object value = getFeature(featureName);

		if (value != null) {
			if (value instanceof Collection<?>) {
				Iterator<?> iterator = ((Collection<?>) value).iterator();
				Object nextObject = null;
				while (iterator.hasNext()) {
					nextObject = iterator.next();
					values.add(nextObject.toString());
				}
			} else {
				values.add(value.toString());
			}
		}
		
		return values;
	}

	/**
	 * Retrieves the value of the feature as an <code>Integer</code>. If the
	 * feature does not exist or cannot be converted to an integer then
	 * <code>null</code> is returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>Integer</code> representation of the value. Numbers are
	 *         converted to integers while Strings are parsed for integer
	 *         values. Any other type will return <code>null</code>.
	 */
	public Integer getFeatureAsInteger(String featureName) {
		Object value = getFeature(featureName);
		Integer intValue = null;
		if (value instanceof Integer) {
			intValue = (Integer) value;
		} else if (value instanceof Number) {
			intValue = new Integer(((Number) value).intValue());
		} else if (value instanceof String) {
			try {
				intValue = new Integer((String) value);
			} catch (NumberFormatException exception) {
				intValue = null;
			}
		}
		return intValue;
	}

	/**
	 * Retrieves the value of the feature as a <code>Long</code>. If the feature
	 * does not exist or cannot be converted to a long then <code>null</code> is
	 * returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>Long</code> representation of the value. Numbers are
	 *         converted to longs while Strings are parsed for long values. Any
	 *         other type will return <code>null</code>.
	 */
	public Long getFeatureAsLong(String featureName) {
		Object value = getFeature(featureName);
		Long longValue = null;
		if (value instanceof Long) {
			longValue = (Long) value;
		} else if (value instanceof Number) {
			longValue = new Long(((Number) value).longValue());
		} else if (value instanceof String) {
			try {
				longValue = new Long((String) value);
			} catch (NumberFormatException exception) {
				longValue = null;
			}
		}
		return longValue;
	}

	/**
	 * Retrieves the value of the feature as a <code>Float</code>. If the
	 * feature does not exist or cannot be converted to a float then
	 * <code>null</code> is returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>Float</code> representation of the value. Numbers are
	 *         converted to floats while Strings are parsed for float values.
	 *         Any other type will return <code>null</code>.
	 */
	public Float getFeatureAsFloat(String featureName) {
		Object value = getFeature(featureName);
		Float floatValue = null;
		if (value instanceof Float) {
			floatValue = (Float) value;
		} else if (value instanceof Number) {
			floatValue = new Float(((Number) value).floatValue());
		} else if (value instanceof String) {
			try {
				floatValue = new Float((String) value);
			} catch (NumberFormatException exception) {
				floatValue = null;
			}
		}
		return floatValue;
	}

	/**
	 * Retrieves the value of the feature as a <code>Double</code>. If the
	 * feature does not exist or cannot be converted to a double then
	 * <code>null</code> is returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>Double</code> representation of the value. Numbers are
	 *         converted to doubles while Strings are parsed for double values.
	 *         Any other type will return <code>null</code>.
	 */
	public Double getFeatureAsDouble(String featureName) {
		Object value = getFeature(featureName);
		Double doubleValue = null;
		if (value instanceof Double) {
			doubleValue = (Double) value;
		} else if (value instanceof Number) {
			doubleValue = new Double(((Number) value).doubleValue());
		} else if (value instanceof String) {
			try {
				doubleValue = new Double((String) value);
			} catch (NumberFormatException exception) {
				doubleValue = null;
			}
		}
		return doubleValue;
	}

	/**
	 * Retrieves the value of the feature as a <code>Boolean</code>. If the
	 * feature does not exist or is not a boolean then
	 * <code>Boolean.FALSE</code> is returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>Boolean</code> representation of the value. Any
	 *         non-Boolean type will return <code>Boolean.FALSE</code>.
	 */
	public Boolean getFeatureAsBoolean(String featureName) {
		Object value = getFeature(featureName);
		Boolean boolValue = Boolean.FALSE;
		
		if (value instanceof Boolean) {
			boolValue = (Boolean) value;
		}
		
		return boolValue;
	}

	/**
	 * Retrieves the value of the feature as a <code>NLGElement</code>. If the
	 * value is a string then it is wrapped in a <code>StringElement</code>. If
	 * the feature does not exist or is of any other type then <code>null</code>
	 * is returned.
	 * 
	 * @param featureName
	 *            the name of the feature.
	 * @return the <code>NLGElement</code>.
	 */
	public NLGElement getFeatureAsElement(String featureName) {
		Object value = getFeature(featureName);
		NLGElement elementValue = null;

		if (value instanceof NLGElement) {
			elementValue = (NLGElement) value;
		} else if (value instanceof String) {
			elementValue = new StringElement((String) value);
		}
		return elementValue;
	}

	/**
	 * Retrieves the map containing all the features for this element.
	 * 
	 * @return a <code>Map</code> of <code>String</code>, <code>Object</code>.
	 */
	public Map<String, Object> getAllFeatures() {
		return this.features;
	}

	/**
	 * Checks the feature map to see if the named feature is present in the map.
	 * 
	 * @param featureName
	 *            the name of the feature to look for.
	 * @return <code>true</code> if the feature exists, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasFeature(String featureName) {
		return featureName != null ? this.features.containsKey(featureName)
				: false;
	}

	/**
	 * Deletes the named feature from the map.
	 * 
	 * @param featureName
	 *            the name of the feature to be removed.
	 */
	public void removeFeature(String featureName) {
		this.features.remove(featureName);
	}

	/**
	 * Deletes all the features in the map.
	 */
	public void clearAllFeatures() {
		this.features.clear();
	}

	/**
	 * Sets the parent element of this element.
	 * 
	 * @param newParent
	 *            the <code>NLGElement</code> that is the parent of this
	 *            element.
	 */
	public void setParent(NLGElement newParent) {
		this.parent = newParent;
	}

	/**
	 * Retrieves the parent of this element.
	 * 
	 * @return the <code>NLGElement</code> that is the parent of this element.
	 */
	public NLGElement getParent() {
		return this.parent;
	}

	/**
	 * Sets the realisation of this element.
	 * 
	 * @param realised
	 *            the <code>String</code> representing the final realisation for
	 *            this element.
	 */
	public void setRealisation(String realised) {
		this.realisation = realised;
	}

	/**
	 * Retrieves the final realisation of this element.
	 * 
	 * @return the <code>String</code> representing the final realisation for
	 *         this element.
	 */
	public String getRealisation() {
		int start = 0;
		int end = 0;
		if (null != this.realisation) {
			end = this.realisation.length();

			while (start < this.realisation.length()
					&& ' ' == this.realisation.charAt(start)) {
				start++;
			}
			if (start == this.realisation.length()) {
				this.realisation = null;
			} else {
				while (end > 0 && ' ' == this.realisation.charAt(end - 1)) {
					end--;
				}
			}
		}

		// AG: changed this to return the empty string if the realisation is
		// null
		// avoids spurious nulls appearing in output for empty phrases.
		return this.realisation == null ? "" : this.realisation.substring(
				start, end);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("{realisation=").append(this.realisation); //$NON-NLS-1$
		if (this.category != null) {
			buffer.append(", category=").append(this.category.toString()); //$NON-NLS-1$
		}
		if (this.features != null) {
			buffer.append(", features=").append(this.features.toString()); //$NON-NLS-1$
		}
		buffer.append('}');
		return buffer.toString();
	}

	public boolean isA(ElementCategory checkCategory) {
		boolean isA = false;

		if (this.category != null) {
			isA = this.category.equalTo(checkCategory);
		} else if (checkCategory == null) {
			isA = true;
		}
		return isA;
	}

	/**
	 * Retrieves the children for this element. This method needs to be
	 * overridden for each specific type of element. Each type of element will
	 * have its own way of determining the child elements.
	 * 
	 * @return a <code>List</code> of <code>NLGElement</code>s representing the
	 *         children of this element.
	 */
	public abstract List<NLGElement> getChildren();

	/**
	 * Retrieves the set of features currently contained in the feature map.
	 * 
	 * @return a <code>Set</code> of <code>String</code>s representing the
	 *         feature names. The set is unordered.
	 */
	public Set<String> getAllFeatureNames() {
		return this.features.keySet();
	}

	public String printTree(String indent) {
		String thisIndent = indent == null ? " |-" : indent + " |-"; //$NON-NLS-1$ //$NON-NLS-2$
		String childIndent = indent == null ? " |-" : indent + " |-"; //$NON-NLS-1$ //$NON-NLS-2$
		StringBuffer print = new StringBuffer();
		print.append("NLGElement: ").append(toString()).append('\n'); //$NON-NLS-1$

		List<NLGElement> children = getChildren();

		if (children != null) {
			for (NLGElement eachChild : getChildren()) {
				print.append(thisIndent).append(
						eachChild.printTree(childIndent));
			}
		}
		return print.toString();
	}

	/**
	 * Determines if this element has its realisation equal to the given string.
	 * 
	 * @param elementRealisation
	 *            the string to check against.
	 * @return <code>true</code> if the string matches the element's
	 *         realisation, <code>false</code> otherwise.
	 */
	public boolean equals(String elementRealisation) {
		boolean match = false;

		if (elementRealisation == null && this.realisation == null) {
			match = true;
		} else if (elementRealisation != null && this.realisation != null) {
			match = elementRealisation.equals(this.realisation);
		}
		return match;
	}

	/**
	 * Sets the number agreement on this element. This method is added for
	 * convenience and not all element types will make use of the number
	 * agreement feature. The method is identical to calling {@code
	 * setFeature(Feature.NUMBER, NumberAgreement.PLURAL)} for plurals or
	 * {@code setFeature(Feature.NUMBER, NumberAgreement.SINGULAR)} for the
	 * singular.
	 * 
	 * @param isPlural
	 *            <code>true</code> if this element is to be treated as a
	 *            plural, <code>false</code> otherwise.
	 */
	public void setPlural(boolean isPlural) {
		if (isPlural) {
			setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		} else {
			setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		}
	}

	/**
	 * Determines if this element is to be treated as a plural. This is a
	 * convenience method and not all element types make use of number
	 * agreement.
	 * 
	 * @return <code>true</code> if the <code>Feature.NUMBER</code> feature has
	 *         the value <code>NumberAgreement.PLURAL</code>, <code>false</code>
	 *         otherwise.
	 */
	public boolean isPlural() {
		return NumberAgreement.PLURAL.equals(getFeature(Feature.NUMBER));
	}

	// Following should be deleted at some point, as it makes more sense to have
	// them in SPhraseSpec
	/**
	 * Retrieves the tense for this element. The method is identical to calling
	 * {@code getFeature(Feature.TENSE)} and casting the result as
	 * <code>Tense<code>.
	 * *
	 * WARNING: You should use getFeature(Feature.TENSE)
	 * getTense will be dropped from simplenlg at some point
	 * 
	 * @return the <code>Tense</code> of this element.
	 */
	@Deprecated
	public Tense getTense() {
		Tense tense = Tense.PRESENT;
		Object tenseValue = getFeature(Feature.TENSE);
		if (tenseValue instanceof Tense) {
			tense = (Tense) tenseValue;
		}
		return tense;
	}

	/**
	 * Sets the tense on this element. The method is identical to calling
	 * {@code setFeature(Feature.TENSE, newTense)}.
	 * 
	 * WARNING: You should use setTense(Feature.TENSE, tense) setTense will be
	 * dropped from simplenlg at some point
	 * 
	 * @param newTense
	 *            the new tense for this element.
	 */
	@Deprecated
	public void setTense(Tense newTense) {
		setFeature(Feature.TENSE, newTense);
	}

	/**
	 * Sets the negation on this element. The method is identical to calling
	 * {@code setFeature(Feature.NEGATED, isNegated)}.
	 * 
	 * WARNING: You should use setFeature(Feature.NEGATED, isNegated) setNegated
	 * will be dropped from simplenlg at some point
	 * 
	 * @param isNegated
	 *            <code>true</code> if the element is to be negated,
	 *            <code>false</code> otherwise.
	 */
	@Deprecated
	public void setNegated(boolean isNegated) {
		setFeature(Feature.NEGATED, isNegated);
	}

	/**
	 * Determines if this element is to be treated as a negation. This method
	 * just examines the value of the NEGATED feature
	 * 
	 * WARNING: You should use getFeature(Feature.NEGATED) getNegated will be
	 * dropped from simplenlg at some point
	 * 
	 * @return <code>true</code> if the <code>Feature.NEGATED</code> feature
	 *         exists and has the value <code>Boolean.TRUE</code>,
	 *         <code>false</code> is returned otherwise.
	 */
	@Deprecated
	public boolean isNegated() {
		return getFeatureAsBoolean(Feature.NEGATED).booleanValue();
	}

	/**
	 * @return the NLG factory
	 */
	public NLGFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory
	 *            the NLG factory to set
	 */
	public void setFactory(NLGFactory factory) {
		this.factory = factory;
	}

	/**
	 * An NLG element is equal to some object if the object is an NLGElement,
	 * they have the same category and the same features.
	 */
	@Override
	public boolean equals(Object o) {
		boolean eq = false;

		if (o instanceof NLGElement) {
			NLGElement element = (NLGElement) o;
			eq = this.category == element.category
					&& this.features.equals(element.features);
		}

		return eq;
	}

}
