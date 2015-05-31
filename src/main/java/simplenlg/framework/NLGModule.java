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

import java.util.List;

import simplenlg.lexicon.Lexicon;

/**
 * <p>
 * <code>NLGModule</code> is the base class that all processing modules extend
 * from. This is abstract and cannot therefore be instantiated itself. The
 * supplied processing modules that come pre-packaged in SimpleNLG can be found
 * within their appropriate packages.
 * </p>
 * 
 * <p>
 * All processing modules perform realisation on a tree of
 * <code>NLGElement</code>s. The modules can alter the tree in whichever way
 * they wish. For example, the syntax processor replaces phrase elements with
 * list elements consisting of inflected words while the morphology processor
 * replaces inflected words with string elements.
 * </p>
 * 
 * <p>
 * <b>N.B.</b> the use of <em>module</em>, <em>processing module</em> and
 * <em>processor</em> is interchangeable. They all mean an instance of this
 * class.
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public abstract class NLGModule {

	/** The lexicon that is to be used by this module. */
	protected Lexicon lexicon = null;

	/**
	 * Performs one-time initialisation of the module.
	 */
	abstract public void initialise();

	/**
	 * Realises the given element. This call is usually recursive as the call
	 * processes the child elements of the given element.
	 * 
	 * @param element
	 *            the <code>NLGElement</code> to be realised.
	 * @return the <code>NLGElement</code> representing the realised state. This
	 *         may be the initial element in a changed form or be a completely
	 *         new element.
	 */
	abstract public NLGElement realise(NLGElement element);

	/**
	 * Realises a <code>List</code> of <code>NLGElement</code>s usually by
	 * iteratively calling the <code>realise(NLGElement)</code> method on each
	 * element in the list and adding the result into a new a <code>List</code>
	 * 
	 * @param elements
	 *            the <code>List</code> of <code>NLGElement</code>s to be
	 *            realised.
	 * @return the <code>List</code> of realised <code>NLGElement</code>s.
	 */
	abstract public List<NLGElement> realise(List<NLGElement> elements);

	/**
	 * Sets the lexicon to be used by this module. Passing in <code>null</code>
	 * will remove the existing lexicon and no lexicon will be used.
	 * 
	 * @param newLexicon
	 *            the new <code>Lexicon</code> to be used.
	 */
	public void setLexicon(Lexicon newLexicon) {
		this.lexicon = newLexicon;
	}

	/**
	 * Retrieves the lexicon currently being used by this module.
	 * 
	 * @return the <code>Lexicon</code> in use. This will be <code>null</code>
	 *         if there is currently no lexicon associated with this module.
	 */
	public Lexicon getLexicon() {
		return this.lexicon;
	}
}
