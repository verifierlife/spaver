package org.spaver.s4u.formula;

import org.spaver.s4u.term.Term;

public interface UnaryTermFormula extends Formula {
	public boolean satisfaction(Term term);

}
