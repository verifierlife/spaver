package org.spaver.s4u.term;

import java.util.ArrayList;

import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.space.WeightedGraph;

/**
 * Until operator between two spatial terms
 * @author tengf
 *
 */
public class UntilTerm implements BinaryTerm {

	public PointSet getTerm(Term term1, Term term2) {

		return null;
	}
	
	/**
	 * t1 U t2: Removing all the points that holds t2, from which these points holds t1
	 * @param weightedGraph
	 * @param term1
	 * @param term2
	 * @return {@link PointSet}
	 */
	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term1, Term term2) {
		AtomicTerm atomicTerm = new AtomicTerm();
		PointSet pointSet1 = atomicTerm.satisfactionTerm(weightedGraph, term1);
		PointSet pointSet2 = atomicTerm.satisfactionTerm(weightedGraph, term2);

		ArrayList<Point> points = new ArrayList<Point>();
		PointSet pointSet3 = new PointSet(points);
		pointSet3.union(pointSet1, pointSet2); // The union of pointSet1 and pointSet2
		PointSet pointSet4;
		ArrayList<Point> temPoint = new ArrayList<Point>();
		PointSet tempPointSet = new PointSet(temPoint);

		while (!pointSet3.equals(null)) {
			pointSet4 = null;
			for (int i = 0; i < points.size(); i++) {
				Point x = points.get(i);
				tempPointSet.intersection((Term) x.getNeighbourd(), pointSet1);
				pointSet1.difference(pointSet1, tempPointSet);
				pointSet4.union(pointSet4, tempPointSet.difference(tempPointSet, pointSet2));
			}
			pointSet3 = pointSet4;
		}
		return pointSet1;
	}

}
