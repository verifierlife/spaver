package org.spaver.shape;

import java.util.ArrayList;

import org.spaver.s4u.term.Term;

public class PointSet implements Term {
	ArrayList<Point> points;

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	/**
	 * Constructing a set of points
	 * @param points
	 */
	public PointSet(ArrayList<Point> points) {
		super();
		this.points = points;
	}
	
	/**
	 * First move all the elements of points1 from points2, and add all the remaining elements to points1 
	 * We hope that the union of two terms returns a new term, instead of the former.
	 * @param term1
	 * @param term2
	 * @return
	 */
	public PointSet union(Term term1, Term term2) {
		PointSet pointSet1 = (PointSet) term1;
		PointSet pointSet2 = (PointSet) term2;
		ArrayList<Point> points1 = pointSet1.getPoints();
		ArrayList<Point> points2 = pointSet2.getPoints();
		points2.removeAll(points1);
		points1.addAll(points2);
		
		return new PointSet(points1);
	}
	
	/**
	 * Make an intersection of terma1 and terms2
	 * @param term1
	 * @param term2
	 * @return
	 */
	public PointSet intersection(Term term1, Term term2) {
		PointSet pointSet1 = (PointSet) term1;
		PointSet pointSet2 = (PointSet) term2;
		
		ArrayList<Point> points1 = pointSet1.getPoints();
		ArrayList<Point> points2 = pointSet2.getPoints();
		points1.retainAll(points2);
		return new PointSet(points1);
	}
	
	/**
	 * Make a difference points2 from points1
	 * @param term1
	 * @param term2
	 * @return
	 */
	public PointSet difference(Term term1, Term term2) {
		PointSet pointSet1 = (PointSet) term1;
		PointSet pointSet2 = (PointSet) term2;
		
		ArrayList<Point> points1 = pointSet1.getPoints();
		ArrayList<Point> points2 = pointSet2.getPoints();
		points1.remove(points2);
		return new PointSet(points1);
	}
	
}
