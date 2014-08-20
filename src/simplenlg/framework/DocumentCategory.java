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
 * This enumerated type defines the different <i>types</i> of components found
 * in the structure of text. This deals exclusively with the structural format
 * of the text and not of the syntax of the language. Therefore, this
 * enumeration deals with documents, sections, paragraphs, sentences and lists.
 * </p>
 * <p>
 * The enumeration implements the <code>ElementCategory</code> interface, thus
 * making it compatible the SimpleNLG framework.
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public enum DocumentCategory implements ElementCategory {

	/** Definition for a document. */
	DOCUMENT,

	/** Definition for a section within a document. */
	SECTION,

	/** Definition for a paragraph. */
	PARAGRAPH,

	/** Definition for a sentence. */
	SENTENCE,

	/** Definition for creating a list of items. */
	LIST,

	/** Definition for creating a list of enumerated items. */
	ENUMERATED_LIST,

	/** Definition for an item in a list. */
	LIST_ITEM;

	/**
	 * <p>
	 * Checks to see if the given object is equal to this document category.
	 * This is done by checking the enumeration if the object is of the type
	 * <code>DocumentCategory</code> or by converting the object and the this
	 * category to strings and comparing the strings.
	 * </p>
	 * <p>
	 * For example, <code>DocumentCategory.LIST</code> will match another
	 * <code>DocumentCategory.LIST</code> but will also match the string
	 * <em>"list"</em> as well.
	 */
	public boolean equalTo(Object checkObject) {
		boolean match = false;

		if(checkObject != null) {
			if(checkObject instanceof DocumentCategory) {
				match = this.equals(checkObject);
			} else {
				match = this.toString().equalsIgnoreCase(checkObject.toString());
			}
		}
		return match;
	}

	/**
	 * <p>
	 * This method determines if the supplied elementCategory forms an immediate
	 * sub-part of <code>this</code> category. The allowed sub-parts for each
	 * <code>this</code> type are outlined below:
	 * </p>
	 * 
	 * <ul>
	 * <li><b>DOCUMENT</b>: can contain SECTIONs, PARAGRAPHs, SENTENCEs,
	 * LISTs and ENUMERATED_LISTs. It cannot contain other DOCUMENTs or LIST_ITEMs.</li>
	 * <li><b>SECTION</b>: can contain SECTIONs (referred to as subsections),
	 * PARAGRAPHs, SENTENCEs, LISTs and ENUMERATED_LISTs. It cannot contain DOCUMENTs or
	 * LIST_ITEMs.</li>
	 * <li><b>PARAGRAPH</b>: can contain SENTENCEs, LISTs and ENUMERATED_LISTs. It cannot contain
	 * DOCUMENTs, SECTIONs, other PARAGRAPHs or LIST_ITEMs.</li>
	 * <li><b>SENTENCE</b>: can only contain other forms of
	 * <code>NLGElement</code>s. It cannot contain DOCUMENTs, SECTIONs,
	 * PARAGRAPHs, other SENTENCEs, LISTs, ENUMERATED_LISTs or LIST_ITEMs.</li>
	 * <li><b>LIST</b>: can only contain LIST_ITEMs. It cannot contain
	 * DOCUMENTs, SECTIONs, PARAGRAPHs, SENTENCEs, other LISTs or ENUMERATED_LISTs.</li>
	 * <li><b>ENUMERATED_LIST</b>: can only contain LIST_ITEMs. It cannot contain
	 * DOCUMENTs, SECTIONs, PARAGRAPHs, SENTENCEs, LISTs or other ENUMERATED_LISTs.</li>
	 * <li><b>LIST_ITEMs</b>: can contain PARAGRAPHs, SENTENCEs, LISTs, ENUMERATED_LISTs or other
	 * forms of <code>NLGElement</code>s. It cannot contain DOCUMENTs, SECTIONs,
	 * or LIST_ITEMs.</li>
	 * </ul>
	 * 
	 * <p>
	 * For structuring text, this effectively becomes the test for relevant
	 * child types affecting the immediate children. For instance, it is
	 * possible for a DOCUMENT to contain LIST_ITEMs but only if the LIST_ITEMs
	 * are children of LISTs.
	 * </p>
	 * 
	 * <p>
	 * A more precise definition of SENTENCE would be that it only contains
	 * PHRASEs. However, this DocumentCategory does not consider these options
	 * as this crosses the boundary between orthographic structure and syntactic
	 * structure.
	 * </p>
	 * 
	 * <p>
	 * In pseudo-BNF this can be written as:
	 * </p>
	 * 
	 * <pre>
	 *    DOCUMENT 		 ::= DOCUMENT_PART*
	 *    DOCUMENT_PART  ::= SECTION | PARAGRAPH
	 *    SECTION 		 ::= DOCUMENT_PART*
	 *    PARAGRAPH 	 ::= PARAPGRAPH_PART*
	 *    PARAGRAPH_PART ::= SENTENCE | LIST | ENUMERATED_LIST
	 *    SENTENCE 	 	 ::= &lt;NLGElement&gt;*
	 *    LIST 			 ::= LIST_ITEM*
	 *    ENUMERATED_LIST::= LIST_ITEM*
	 *    LIST_ITEM 	 ::= PARAGRAPH | PARAGRAPH_PART | &lt;NLGElement&gt;
	 * </pre>
	 * <p>
	 * Ideally the '*' should be replaced with '+' to represent that one or more
	 * of the components must exist rather than 0 or more. However, the
	 * implementation permits creation of the <code>DocumentElement</code>s with
	 * no children or sub-parts added.
	 * </p>
	 * 
	 * @param elementCategory
	 *            the category we are checking against. If this is
	 *            <code>NULL</code> the method will return <code>false</code>.
	 * @return <code>true</code> if the supplied elementCategory is a sub-part
	 *         of <code>this</code> type of category, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasSubPart(ElementCategory elementCategory) {
		boolean subPart = false;
		if(elementCategory != null) {
			if(elementCategory instanceof DocumentCategory) {
				switch(this){
				case DOCUMENT :
					subPart = !(elementCategory.equals(DOCUMENT)) && !(elementCategory.equals(LIST_ITEM));
					break;

				case SECTION :
					subPart = elementCategory.equals(PARAGRAPH) || elementCategory.equals(SECTION);
					break;

				case PARAGRAPH :
					subPart = elementCategory.equals(SENTENCE) || elementCategory.equals(LIST);
					break;

				case LIST :
					subPart = elementCategory.equals(LIST_ITEM);
					break;
				case ENUMERATED_LIST :
					subPart = elementCategory.equals(LIST_ITEM);
					break;

				default :
					break;
				}
			} else {
				subPart = this.equals(SENTENCE) || this.equals(LIST_ITEM);
			}
		}
		return subPart;
	}
}
