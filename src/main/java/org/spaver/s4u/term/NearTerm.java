package org.spaver.s4u.term;

import java.util.ArrayList;
import java.util.Set;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.space.WeightedGraph;

public class NearTerm implements UnaryTerm {

	public SpatialContext getContext() {
		return new SpatialContext(new SpatialContextFactory());
	}

	/**
	 * Get near points of the term
	 */
	public PointSet getTerm(Term term) {
		ArrayList<Point> points = new ArrayList<Point>();
		if (term instanceof Point) {
			Point point = (Point) term;
			points = point.getNeighbourd();
		} else if (term instanceof PointSet) {
			PointSet pointSet = (PointSet) term;
			ArrayList<Point> points2 = pointSet.getPoints();
			for (int i = 0; i < points2.size(); i++) {
				Point point = points2.get(i);
				points.addAll(points2.get(i).getNeighbourd());
				points.removeAll(points2);
			}
		}
		return new PointSet(points);
	}
	/**
	 * N(red): when red is a point, it returns its neighbors, when red is a point set, it returns its neighbors difference the point set
	 * @param weightedGraph
	 * @param term
	 * @return {@link PointSet}
	 */
	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term) {
		NearTerm nearTerm = new NearTerm();
		Set<Point> pointsGraph = weightedGraph.getPoints();
		ArrayList<Point> points = new ArrayList<Point>();
		ArrayList<Point> pointsNeighbors = new ArrayList<Point>();// To store the neighbors of some point
		if (term instanceof Point) {
			Point point = (Point) term;
			pointsNeighbors = point.getNeighbourd();
			for (int i = 0; i < pointsNeighbors.size(); i++) {
				if (pointsGraph.contains(pointsNeighbors.get(i))) {
					points.add(pointsNeighbors.get(i));
				}
			}
		} else if (term instanceof PointSet) {
			PointSet pointNearSet = nearTerm.getTerm(term);
			pointsNeighbors = pointNearSet.getPoints();
			for (int i = 0; i < pointsNeighbors.size(); i++) {
				if (pointsGraph.contains(pointsNeighbors.get(i))) {
					points.add(pointsNeighbors.get(i));
				}
			}
		}
		return new PointSet(points);
	}
}
