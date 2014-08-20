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
package simplenlg.aggregation;

import java.util.Arrays;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;

public class FunctionalSet {

	private List<NLGElement> components;
	private DiscourseFunction function;
	private ElementCategory category;
	private Periphery periphery;

	public static FunctionalSet newInstance(DiscourseFunction func,
			ElementCategory category, Periphery periphery,
			NLGElement... components) {

		FunctionalSet pair = null;

		if (components.length >= 2) {
			pair = new FunctionalSet(func, category, periphery, components);
		}

		return pair;

	}

	FunctionalSet(DiscourseFunction func, ElementCategory category,
			Periphery periphery, NLGElement... components) {
		this.function = func;
		this.category = category;
		this.periphery = periphery;
		this.components = Arrays.asList(components);
	}

	public boolean formIdentical() {
		boolean ident = true;
		NLGElement firstElement = this.components.get(0);

		for (int i = 1; i < this.components.size() && ident; i++) {
			ident = firstElement.equals(components.get(i));
		}

		return ident;
	}

	public boolean lemmaIdentical() {
		return false;
	}

	public void elideLeftMost() {
		for(int i = 0; i < this.components.size()-1; i++) {
			recursiveElide(components.get(i));		
		}
	}

	public void elideRightMost() {
		for(int i = this.components.size()-1; i > 0; i--) {
			recursiveElide( components.get(i) );
			
		}
	}
	
	private void recursiveElide(NLGElement component) {
		if(component instanceof ListElement) {
			for(NLGElement subcomponent: component.getFeatureAsElementList(InternalFeature.COMPONENTS)) {
				recursiveElide(subcomponent);
			}
		} else {
			component.setFeature(Feature.ELIDED, true);
		}
	}

	public DiscourseFunction getFunction() {
		return function;
	}

	public ElementCategory getCategory() {
		return category;
	}

	public Periphery getPeriphery() {
		return periphery;
	}
	
	public List<NLGElement> getComponents() {
		return this.components;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		for(NLGElement elem: this.components) {
			buffer.append("ELEMENT: ").append(elem.toString()).append("\n");
		}
		
		return buffer.toString();
	}
	
}
