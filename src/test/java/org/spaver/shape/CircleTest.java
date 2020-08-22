package org.spaver.shape;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.s4u.formula.SubsetFormula;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;

public class CircleTest {

	public static void main(String[] args) {
		SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());
		Circle circle = spatialContext.makeCircle(1.0, 2.0, 5.0);
		Point point = spatialContext.makePoint(1.2, 3.4);
		SubsetFormula subsetFormula = new SubsetFormula();
		boolean result = subsetFormula.satisfaction(point, circle);
		System.out.println(result);
	}

}
