package org.spaver.s4u.formula;

import org.spaver.s4u.term.Term;

/**
 * Binary term formulas is defined for spatial subset relations t1 <= t2 between two terms
 * @author tengf
 *
 */
public interface BinaryTermFormula extends Term {

	public boolean satisfaction(Term term1, Term term2);
}
