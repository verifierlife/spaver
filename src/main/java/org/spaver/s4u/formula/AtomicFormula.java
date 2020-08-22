package org.spaver.s4u.formula;

import org.spaver.context.SpatialContext;

public interface AtomicFormula extends Formula{
	
    public abstract SpatialContext getContext();
}
