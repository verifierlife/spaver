package org.spaver.s4u.term;

import java.util.ArrayList;
import java.util.Set;

import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.shape.Rectangle;
import org.spaver.shape.Shape;
import org.spaver.space.WeightedGraph;

public class AtomicTerm implements Term {

	/**
	 * Region-based space
	 * @param term
	 * @return
	 */
	public PointSet getTerm(Term term) {
		ArrayList<Point> points = new ArrayList<Point>();
		
			if (term instanceof Circle) {
				Circle circle = (Circle) term;
				points.add(circle.getMaxX());
				points.add(circle.getMaxY());
				points.add(circle.getMinX());
				points.add(circle.getMinY());
			}else if (term instanceof Rectangle) {
				Rectangle rectangle = (Rectangle) term;
				points.add(rectangle.getLeftLower());
				points.add(rectangle.getLeftUpper());
				points.add(rectangle.getRightLower());
				points.add(rectangle.getRightUpper());
			}else if (term instanceof Point) {
				points.add((Point) term);
			}
		return new PointSet(points);
	}

	/**
	 * 
	 * @param weightedGraph
	 * @param term
	 * @return {@link PointSet}
	 */
	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term) {
		weightedGraph = new WeightedGraph();
		Set<Point> point = weightedGraph.getPoints();
		ArrayList<Point> resultPoint = new ArrayList<Point>();
		
		if (term instanceof PointSet) {
			PointSet pointSet = (PointSet) term;
			ArrayList<Point> points = pointSet.getPoints();
			for (int i = 0; i < points.size(); i++) {
				if (point.contains(points.get(i))) {
					resultPoint.add(points.get(i));	
				}
			}
		}else if (term instanceof Point) {
			Point point2 = (Point) term;
			if (point.contains(point2)) {
				resultPoint.add(point2);
			}
		}
		
		return new PointSet(resultPoint);
	}
}
