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

import java.util.ArrayList;
import java.util.List;

import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.NLGModule;

/**
 * An Aggregator performs aggregation on clauses, by applying a set of
 * prespecified rules on them and returning the result.
 * 
 * @author Albert Gatt, University of Malya & University of Aberdeen
 * 
 */
public class Aggregator extends NLGModule {

	private List<AggregationRule> _rules;
	private NLGFactory _factory;

	/**
	 * Creates an instance of Aggregator
	 */
	public Aggregator() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialise() {
		this._rules = new ArrayList<AggregationRule>();
		this._factory = new NLGFactory();
	}

	/**
	 * Set the factory that this aggregator should use to create phrases. The
	 * factory will be passed on to all the component rules.
	 * 
	 * @param factory
	 *            the phrase factory
	 */
	public void setFactory(NLGFactory factory) {
		this._factory = factory;

		for (AggregationRule rule : this._rules) {
			rule.setFactory(this._factory);
		}
	}

	/**
	 * Add a rule to this aggregator. Aggregation rules are applied in the order
	 * in which they are supplied.
	 * 
	 * @param rule
	 *            the rule
	 */
	public void addRule(AggregationRule rule) {
		rule.setFactory(this._factory);
		this._rules.add(rule);
	}

	/**
	 * Get the rules in this aggregator.
	 * 
	 * @return the rules
	 */
	public List<AggregationRule> getRules() {
		return this._rules;
	}

	/**
	 * Apply aggregation to a single phrase. This will only work if the phrase
	 * is a coordinated phrase, whose children can be further aggregated.
	 * 
	 */
	@Override
	public NLGElement realise(NLGElement element) {
		NLGElement result = element;

		for (AggregationRule rule : this._rules) {
			NLGElement intermediate = rule.apply(result);

			if (intermediate != null) {
				result = intermediate;
			}
		}

		return result;
	}

	/**
	 * Apply aggregation to a list of elements. This method iterates through the
	 * rules supplied via {@link #addRule(AggregationRule)} and applies them to
	 * the elements.
	 * 
	 * @param elements
	 *            the list of elements to aggregate
	 * @return a list of the elements that remain after the aggregation rules
	 *         have been applied
	 */
	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		for (AggregationRule rule : this._rules) {
			elements = rule.apply(elements);
		}

		return elements;
	}

}
