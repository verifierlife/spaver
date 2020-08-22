package org.spaver.s4u.formula;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.s4u.term.Term;

public class UniversalFormula implements UnaryTermFormula, AtomicFormula{

	Formula formula;
	SpatialContext spatialContext;
	
	public UniversalFormula(Formula formula) {
		super();
		this.formula = formula;
		spatialContext = getContext();
	}

	public boolean satisfaction(Term term) {
		return false;
	}

	public SpatialContext getContext() {
		return new SpatialContext(new SpatialContextFactory());
	}

}
