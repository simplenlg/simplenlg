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
package simplenlg.xmlrealiser;

import java.io.Serializable;

/**
 * This class represents an exception thrown by the xml realiser framework.
 * 
 * @author Christopher Howell Agfa Healthcare Corporation
 * @author Albert Gatt, University of Malta
 */
public class XMLRealiserException extends Throwable implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new xML realiser exception.
	 */
	public XMLRealiserException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new xML realiser exception.
	 *
	 * @param arg0 the arg0
	 */
	public XMLRealiserException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new xML realiser exception.
	 *
	 * @param arg0 the arg0
	 */
	public XMLRealiserException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new xML realiser exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 */
	public XMLRealiserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
