package org.spaver.s4u.formula;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;

public class NegativeFormula extends UnaryFormula{


	@Override
	public SpatialContext getContext() {
		return new SpatialContext(new SpatialContextFactory());
	}

	@Override
	public boolean satisfaction(Formula formula) {
		boolean resultFormula= false;
		if (formula instanceof SubsetFormula) {
			resultFormula = ((SubsetFormula)formula).isSat();
		}
		return !resultFormula;
	}

}
