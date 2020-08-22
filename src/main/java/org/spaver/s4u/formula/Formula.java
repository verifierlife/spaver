package org.spaver.s4u.formula;

import org.spaver.s4u.term.Term;

public interface Formula {

	boolean satisfaction(Term term);

	
}
