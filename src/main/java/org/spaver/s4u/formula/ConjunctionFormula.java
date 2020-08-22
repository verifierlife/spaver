package org.spaver.s4u.formula;

public class ConjunctionFormula extends BinaryFormula{

//	@Override
//	public boolean satisfaction(Formula formula1, Formula formula2) {
//		boolean resultFormulas1= false, resultFlormula2 = false;
//		if (formula1 instanceof SubsetFormula && formula2 instanceof SubsetFormula) {
//			resultFormulas1 = ((SubsetFormula)formula1).isSat();
//			resultFlormula2 = ((SubsetFormula)formula2).isSat();
//		}
//		
//		return resultFormulas1&&resultFlormula2;
//	}

	@Override
	public boolean satisfaction(boolean left, boolean right) {
		return left && right;
	}

}
