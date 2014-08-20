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

package simplenlg.features;

/**
 * <p>
 * This is an enumeration of the two different types of clauses used in the
 * SimplNLG package. Clauses can be either matrix or subordinate. Matrix clauses
 * are not contained within any other clause and frequently span an entire
 * sentence, whereas a subordinate clauses is contained within another clause.
 * </p>
 * 
 * <p>
 * As an example, take the phrase, <em><b>whoever said it</b> is wrong</em>.
 * This phrase has two clauses, one being the main clause and the other being a
 * subordinate clause. The section in <b>bold</b> type highlights the
 * subordinate clause. It is entirely contained within another clause. The
 * matrix clause is of the form <em>he is wrong</em> or to be more general
 * <em>X is wrong</em>. <em>X</em> can be replaced with a single subject or, as
 * is the case here, by a subordinate clause.
 * </p>
 * 
 * <p>
 * The clause status is recorded under the {@code Feature.CLAUSE_STATUS} feature
 * and applies only to clauses.
 * <hr>
 * <p>
 * Copyright (C) 2010, University of Aberdeen
 * </p>
 * 
 * 
 * @author A. Gatt and D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */

public enum ClauseStatus {

	/**
	 * This enumeration represents a matrix clause. A matrix clause is not
	 * subordinate to any other clause and therefore sits at the top-level of
	 * the clause hierarchy, typically spanning the whole sentence.
	 */
	MATRIX,

	/**
	 * The subordinate clauses are contained within a higher clause.
	 */
	SUBORDINATE;
}
