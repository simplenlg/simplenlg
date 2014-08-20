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

/**
 * <p>
 * This is the base interface for defining categories for the sub classes of 
 * <code>NLGElement</code>. Each type of category that is used must implement
 * this interface.
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public interface ElementCategory {

	/**
	 * Checks to see if this supplied object matches this particular category.
	 * 
	 * @param checkObject
	 *            the object to be checked against.
	 * @return <code>true</code> if the object matches, <code>false</code>
	 *         otherwise.
	 */
	public boolean equalTo(Object checkObject);
}
