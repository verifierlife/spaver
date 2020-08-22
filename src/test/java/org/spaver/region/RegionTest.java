package org.spaver.region;

import java.util.ArrayList;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.s4u.formula.ConjunctionFormula;
import org.spaver.s4u.formula.SubsetFormula;
import org.spaver.s4u.term.InteriorTerm;
import org.spaver.s4u.term.UnionTerm;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;

public class RegionTest {

	public static void main(String[] args) {

	SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());
	Point point1 = spatialContext.makePoint(2.0, 2.0);//red
	Circle circle = spatialContext.makeCircle(point1, 1.2);
	Point point2 = spatialContext.makePoint(3.2, 2.0);	//green
	Circle circle2 = spatialContext.makeCircle(point2, 1.0);
	Point point3 = spatialContext.makePoint(2.2, 2.2);//blue
	Circle circle3 = spatialContext.makeCircle(point3, 0.8);
	
	UnionTerm unionTerm = new UnionTerm();
	PointSet pointSet = unionTerm.getTerm(circle2, circle3);
	 
	ArrayList<Point> points = pointSet.getPoints();
	SubsetFormula subsetFormula = new SubsetFormula();
	boolean left = subsetFormula.satisfaction(pointSet, circle);
	PointSet pointSet2 = new InteriorTerm().getTerm(circle2);
	boolean right =  subsetFormula.satisfaction(pointSet, circle2);
	
	boolean result = new ConjunctionFormula().satisfaction(left, right);
	
	System.out.println("The result is "+result);
	
	}

}
