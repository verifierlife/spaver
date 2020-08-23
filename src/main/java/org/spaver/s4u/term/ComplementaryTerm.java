package org.spaver.s4u.term;


import java.util.ArrayList;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.shape.Shape;
import org.spaver.space.WeightedGraph;


/**
 * For region-based term, a complementary term corresponds to a bigger term as the universal set. 
 * @author tengf
 *
 */
public class ComplementaryTerm implements BinaryTerm{

	SpatialContext spatialContext;

	/**
	 * Circle1: (x-x1)^2 + (y-y1)^2 = r1^2 Circle2: (x-x2)^2 + (y-y2)^2 = r2^2
	 */
	double x1;
	double y1;
	double x2;
	double y2;
	double r1;
	double r2;
	
	
	public ComplementaryTerm() {
		super();
		this.spatialContext = new SpatialContext(new SpatialContextFactory());
	}

	public ComplementaryTerm(Shape shape1, Shape shape2) {
		super();
		this.spatialContext = new SpatialContext(new SpatialContextFactory());
		this.x1 = shape1.getCenter().getX();
		this.y1 = shape1.getCenter().getY();
		this.x2 = shape2.getCenter().getX();
		this.y2 = shape2.getCenter().getY();
		this.r1 = ((Circle)shape1).getRadius();
		this.r2 = ((Circle)shape2).getRadius();
	}

	public SpatialContext getContext() {
		// TODO Auto-generated method stub
		return new SpatialContext(new SpatialContextFactory());
	}

	public PointSet getTerm(Term term, Term universalSet) {
		// TODO Auto-generated method stub
		ArrayList<Point> points = new ArrayList<Point>();
		if (term instanceof Circle && universalSet instanceof Circle) {
			Circle circle1 = (Circle) term;
			Circle circle2 = (Circle) universalSet;
			double x1 = circle1.getCenter().getX();
			double y1 = circle1.getCenter().getY();
			double r1 = circle1.getRadius();
			double x2 = circle2.getCenter().getX();
			double y2 = circle2.getCenter().getY();
			double r2 = circle2.getRadius();
			
			double A = (x1 * x1 - x2 * x2 + y1 * y1 - y2 * y2 + r2 * r2 - r1 * r1) / (2 * (y1 - y2));
			double B = (x1 - x2) / (y1 - y2);
			
			Point point = new Point(spatialContext);
			double x = point.getX();
			double y = point.getY();
			
			boolean guard1 = ((x - x1)*(x - x1) + (y - y1)*(y - y1) > r1*r1) ? true:false;
			boolean guard2 = ((x - x2)*(x - x2) + (y - y2)*(y - y2) <= r2*r2) ? true:false;

			if(guard1 && guard2)
				points.add(point);
			
			points.add(spatialContext.makePoint(x1 - r1, y1));
			points.add(spatialContext.makePoint(x1 + r1, y1));
			points.add(spatialContext.makePoint(x1, y1 - r1));
			points.add(spatialContext.makePoint(x1, y1 + r1));
			points.add(spatialContext.makePoint(x2 - r2, y2));
			points.add(spatialContext.makePoint(x2 + r2, y2));
			points.add(spatialContext.makePoint(x2, y2 - r2));
			points.add(spatialContext.makePoint(x2, y2 + r2));
		}
		
		return new PointSet(points);
	}


	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term1, Term term2) {
		// TODO Auto-generated method stub
		return null;
	}


}
