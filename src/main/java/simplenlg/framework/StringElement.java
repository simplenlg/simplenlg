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
import java.util.List;
import java.util.Map;

import simplenlg.features.Feature;

/**
 * <p>
 * This class defines an element for representing canned text within the
 * SimpleNLG library. Once assigned a value, the string element should not be
 * changed by any other processors.
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class StringElement extends NLGElement {

	/**
	 * Constructs a new string element representing some canned text.
	 * 
	 * @param value
	 *            the text for this string element.
	 */
	public StringElement(String value) {
		setCategory(PhraseCategory.CANNED_TEXT);
		setFeature(Feature.ELIDED, false);
		setRealisation(value);
	}

	/**
	 * The string element contains no children so this method will always return
	 * an empty list.
	 */
	@Override
	public List<NLGElement> getChildren() {
		return new ArrayList<NLGElement>();
	}

	@Override
	public String toString() {
		return getRealisation();
	}

	/* (non-Javadoc)
	 * @see simplenlg.framework.NLGElement#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o) && (o instanceof StringElement) && realisationsMatch((StringElement) o);
	}

	private boolean realisationsMatch(StringElement o) {
		if  (getRealisation() == null) {
			return o.getRealisation() == null;
		}
		else
			return getRealisation().equals(o.getRealisation());
	}

	@Override
	public String printTree(String indent) {
		StringBuffer print = new StringBuffer();
		print
				.append("StringElement: content=\"").append(getRealisation()).append('\"'); //$NON-NLS-1$
		Map<String, Object> features = this.getAllFeatures();

		if (features != null) {
			print.append(", features=").append(features.toString()); //$NON-NLS-1$
		}
		print.append('\n');
		return print.toString();
	}
}
