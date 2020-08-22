package org.spaver.s4u.formula;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.s4u.term.Term;

public class ExistentialFormula implements UnaryTermFormula{

	Formula formula;
	
	public ExistentialFormula(Formula formula) {
		super();
		this.formula = formula;
	}

	SpatialContext spatialContext;

	public SpatialContext getContext() {
		return new SpatialContext(new SpatialContextFactory());
	}

	public boolean satisfaction(Term term) {
		return false;
	}


}
