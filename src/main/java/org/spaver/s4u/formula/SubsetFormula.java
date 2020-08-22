package org.spaver.s4u.formula;

import java.util.ArrayList;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.distance.CartesianDistCalc;
import org.spaver.s4u.term.InteriorTerm;
import org.spaver.s4u.term.Term;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.shape.Shape;

public class SubsetFormula implements BinaryTermFormula{


	private boolean sat;
	
	public boolean isSat() {
		return sat;
	}

	public void setSat(boolean sat) {
		this.sat = sat;
	}

	public SpatialContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public PointSet getTerm(Term term1, Term term2) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean satisfaction(Term term1, Term term2) {
		int count = 0;
		CartesianDistCalc cartestian = new CartesianDistCalc();
		   
		if (term1 instanceof PointSet && term2 instanceof Shape) {
			PointSet pointSet = (PointSet)term1;
			Shape shape = (Shape) term2;
			ArrayList<Point> points = pointSet.getPoints();
			if (shape instanceof Circle) {
				double x = ((Circle)shape).getCenter().getX();
				double y = ((Circle)shape).getCenter().getY();
				double r = ((Circle)shape).getRadius();
				for (int i = 0; i < points.size(); i++) {
					if (!cartestian.pointInCircle(points.get(i), x, y, r)) {
						count++;
					}
				}
			}
		}
		if (count>0) {
			setSat(false);
			return false;
		}else {
			setSat(true);
			return true;
		}
	}
	
	public static void main(String[] args) {
		SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());
		Circle circle = spatialContext.makeCircle(1.0, 2.0, 5.0);
		Circle circle1 = spatialContext.makeCircle(1.0, 2.0, 5.0);
		SubsetFormula subset = new SubsetFormula();
		subset.satisfaction(circle, circle1);
		subset.isSat();
	}

}
