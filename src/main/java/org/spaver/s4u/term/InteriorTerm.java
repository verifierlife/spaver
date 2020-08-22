package org.spaver.s4u.term;

import java.util.ArrayList;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.shape.Rectangle;
import org.spaver.shape.Shape;
import org.spaver.shape.SpatialRelation;
import org.spaver.space.WeightedGraph;

public class InteriorTerm implements UnaryTerm{
	
	
	public SpatialContext getContext() {
		return new SpatialContext(new SpatialContextFactory());
	}
	
	public static void main(String[] args) {
		ArrayList<Point> termPoints = new ArrayList<Point>();
		PointSet pointSet = new PointSet(termPoints);
		InteriorTerm interiorTerm = new InteriorTerm();
		SpatialContext spatialContext = interiorTerm.getContext();
		Point point = spatialContext.makePoint(1.0, 2.0);
		spatialContext.makeCircle(point, 5.0);
	}

	
	public PointSet getTerm(Term term) {
		ArrayList<Point> points = new ArrayList<Point>();
		PointSet pointSet = new PointSet(points);
		
		if (term instanceof Shape) {
			Shape shape = (Shape) term;
			if (shape instanceof Circle) {
				points.add(((Circle) shape).getMaxX());
				points.add(((Circle) shape).getMaxY());
				points.add(((Circle) shape).getMinX());
				points.add(((Circle) shape).getMinY());
			}else if(shape instanceof Rectangle){
				points.add(((Rectangle) shape).getLeftLower());
				points.add(((Rectangle) shape).getLeftUpper());
				points.add(((Rectangle) shape).getRightLower());
				points.add(((Rectangle) shape).getRightUpper());
			}
		}
		return pointSet;
	}

	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term) {
		// TODO Auto-generated method stub
		return null;
	}

}
