package org.spaver.s4u.formula;

import org.spaver.context.SpatialContext;

public abstract class UnaryFormula{
	
	public abstract boolean satisfaction(Formula formula);
	
    public abstract SpatialContext getContext();

}
