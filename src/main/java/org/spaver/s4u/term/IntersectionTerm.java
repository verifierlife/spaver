package org.spaver.s4u.term;

import java.util.ArrayList;

import org.spaver.context.SpatialContext;
import org.spaver.context.SpatialContextFactory;
import org.spaver.shape.Circle;
import org.spaver.shape.Point;
import org.spaver.shape.PointSet;
import org.spaver.shape.Rectangle;
import org.spaver.shape.Shape;
import org.spaver.space.WeightedGraph;

public class IntersectionTerm implements BinaryTerm {

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

	public IntersectionTerm(Shape shape1, Shape shape2) {
		spatialContext = new SpatialContext(new SpatialContextFactory());
		x1 = shape1.getCenter().getX();
		x2 = shape2.getCenter().getX();
		y1 = shape1.getCenter().getY();
		y2 = shape2.getCenter().getY();
		r1 = ((Circle) shape1).getRadius();
		r2 = ((Circle) shape2).getRadius();
	}
	
	/**
	 * Testing
	 * @param args
	 */
	public static void main(String[] args) {
		SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());
		Circle circle1 = spatialContext.makeCircle(1.0, 3.0, 3.0);
		Circle circle2 = spatialContext.makeCircle(3.0, 3.0, 4.0);
		IntersectionTerm intersectionTerm = new IntersectionTerm(circle1, circle2);
		PointSet pointSet = intersectionTerm.getTerm(circle1, circle2);
		ArrayList<Point> points = pointSet.getPoints();
		
		for (int i = 0; i < points.size(); i++) {
			System.out.println(points.get(i).getX()+", "+points.get(i).getY());
		}
		
	}

	public PointSet getTerm(Term term1, Term term2) {
		ArrayList<Point> points = new ArrayList<Point>();
		PointSet pointSet = new PointSet(points);
		if (term1 instanceof Shape && term2 instanceof Shape) {
			Shape shape1 = (Shape) term1;
			Shape shape2 = (Shape) term2;
			if ((shape1 instanceof Circle) && (shape2 instanceof Circle)) {
				// The equation a*x^2+b*x+c=0
				double a, b, c;
				// Two solutions of x: x_1 , x_2
				// Two solutions of y: y_1 , y_2
				double x_1 = 0, x_2 = 0, y_1 = 0, y_2 = 0;
				// Discriminant
				double delta = -1;

				if (y1 != y2) {
					// Define new parameters
					double A = (x1 * x1 - x2 * x2 + y1 * y1 - y2 * y2 + r2 * r2 - r1 * r1) / (2 * (y1 - y2));
					double B = (x1 - x2) / (y1 - y2);
					a = 1 + B * B;
					b = -2 * (x1 + (A - y1) * B);
					c = x1 * x1 + (A - y1) * (A - y1) - r1 * r1;

					// Use the discriminant below to determine whether there is a solution
					delta = b * b - 4 * a * c;

					if (delta > 0) {
						x_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
						x_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
						y_1 = A - B * x_1;
						y_2 = A - B * x_2;
						points.add(spatialContext.makePoint(x_1, y_1));
						points.add(spatialContext.makePoint(x_2, y_2));
					} else if (delta == 0) {
						x_1 = x_2 = -b / (2 * a);
						y_1 = y_2 = A - B * x_1;
						points.add(spatialContext.makePoint(x_1, y_1));
					} else {
						System.err.println("The spatial relation is Disconnected");
					}
				} else if (x1 != x2) {
					// When y1=y2, the two solutions of x are equal
					x_1 = x_2 = (x1 * x1 - x2 * x2 + r2 * r2 - r1 * r1) / (2 * (x1 - x2));
					a = 1;
					b = -2 * y1;
					c = y1 * y1 - r1 * r1 + (x_1 - x1) * (x_1 - x1);

					delta = b * b - 4 * a * c;
					if (delta > 0) {
						y_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
						y_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
						points.add(spatialContext.makePoint(x_1, y_1));
						points.add(spatialContext.makePoint(x_2, y_2));
					} else if (delta == 0) {
						y_1 = y_2 = -b / (2 * a);
						points.add(spatialContext.makePoint(x_1, y_1));
					} else {
						System.err.println("The spatial relation is Disconnected");
					}
				}
			} else if ((shape1 instanceof Rectangle) && (shape2 instanceof Rectangle)) {
				double int_xMin, int_yMin, int_xMax, int_yMax, ext_xMin, ext_yMin, ext_xMax, ext_yMax;
				int_xMin = ((Rectangle) shape1).getMinX();
				int_yMin = ((Rectangle) shape1).getMinY();
				int_xMax = ((Rectangle) shape1).getMaxX();
				int_yMax = ((Rectangle) shape1).getMaxY();

				ext_xMin = ((Rectangle) shape2).getMinX();
				ext_yMin = ((Rectangle) shape2).getMinY();
				ext_xMax = ((Rectangle) shape2).getMaxX();
				ext_yMax = ((Rectangle) shape2).getMaxY();
			}
		}
		return pointSet;
	}

	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term1, Term term2) {
		// TODO Auto-generated method stub
		return null;
	}

}
