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
 * 
 * @author tengf
 *
 */
public class UnionTerm implements BinaryTerm {

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

	
	public UnionTerm() {
		spatialContext = new SpatialContext(new SpatialContextFactory());
	}
	
	public UnionTerm(double x1, double y1, double x2, double y2, double r1, double r2) {
		this.spatialContext = new SpatialContext(new SpatialContextFactory());
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.r1 = r1;
		this.r2 = r2;
	}

	public PointSet getTerm(Term term1, Term term2) {
		ArrayList<Point> points = new ArrayList<Point>();

			if ((term1 instanceof Circle) && (term2 instanceof Circle)) {
				// The equation a*x^2+b*x+c=0
				Circle circle1 = (Circle) term1;
				Circle circle2 = (Circle) term2;
				x1 = circle1.getCenter().getX();
				y1 = circle1.getCenter().getY();
				r1 = circle1.getRadius();
				
				x2 = circle2.getCenter().getX();
				y2 = circle2.getCenter().getY();
				r2 = circle2.getRadius();
				
				double a, b, c;
				// Four solutions of x: x_11, x_12, x_13, x_14
				// Four solutions of y: y_21 , y_22, y_23, y_24
				double x_11 = 0, x_12 = 0, x_13 = 0, x_14 = 0, y_11 = 0, y_12 = 0, y_13 = 0, y_14 = 0;
				double x_21 = 0, x_22 = 0, x_23 = 0, x_24 = 0, y_21 = 0, y_22 = 0, y_23 = 0, y_24 = 0;
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
						// B(y-y2) = x - x2
						x_21 = x2 - B * y2
								+ B * (y2 / (B * B + 1) + r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1));
						y_21 = y2 / (B * B + 1) + r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						x_22 = (y2 + B * x2 - y2 / (B * B + 1) + B * r2 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y2) / (B * B + 1)) / B;
						y_22 = y2 / (B * B + 1) - B * r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						x_23 = -(y2 / (B * B + 1) - B * x2 - y2 + B * r2 * (1 / Math.sqrt(B * B + 1))
								+ (B * B * y2) / (B * B + 1)) / B;
						y_23 = y2 / (B * B + 1) + B * r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						// B(y-y1) = x- x1
						x_11 = x1 - B * y1
								+ B * (y1 / (B * B + 1) - r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1));
						y_11 = y1 / (B * B + 1) - r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						// y-y1 = -B(x-x1)
						x_12 = (y1 + B * x1 - y1 / (B * B + 1) + B * r1 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y1) / (B * B + 1)) / B;
						y_12 = y1 / (B * B + 1) - B * r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						x_13 = (y1 + B * x1 - y1 / (B * B + 1) + B * r1 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y1) / (B * B + 1)) / B;
						y_13 = y1 / (B * B + 1) - B * r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						points.add(spatialContext.makePoint(x_11, y_11));
						points.add(spatialContext.makePoint(x_12, y_12));
						points.add(spatialContext.makePoint(x_13, y_13));
						points.add(spatialContext.makePoint(x_21, y_21));
						points.add(spatialContext.makePoint(x_22, y_22));
						points.add(spatialContext.makePoint(x_23, y_23));

					} else if (delta == 0) {
						x_11 = x_12 = -b / (2 * a);
						y_11 = y_12 = A - B * x_11;
						x_21 = x2 - B * y2
								+ B * (y2 / (B * B + 1) + r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1));
						y_21 = y2 / (B * B + 1) + r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						x_22 = (y2 + B * x2 - y2 / (B * B + 1) + B * r2 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y2) / (B * B + 1)) / B;
						y_22 = y2 / (B * B + 1) - B * r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						x_23 = -(y2 / (B * B + 1) - B * x2 - y2 + B * r2 * (1 / Math.sqrt(B * B + 1))
								+ (B * B * y2) / (B * B + 1)) / B;
						y_23 = y2 / (B * B + 1) + B * r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						// B(y-y1) = x- x1
						x_11 = x1 - B * y1
								+ B * (y1 / (B * B + 1) - r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1));
						y_11 = y1 / (B * B + 1) - r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						// y-y1 = -B(x-x1)
						x_12 = (y1 + B * x1 - y1 / (B * B + 1) + B * r1 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y1) / (B * B + 1)) / B;
						y_12 = y1 / (B * B + 1) - B * r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						x_13 = (y1 + B * x1 - y1 / (B * B + 1) + B * r1 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y1) / (B * B + 1)) / B;
						y_13 = y1 / (B * B + 1) - B * r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						points.add(spatialContext.makePoint(x_11, y_11));
						points.add(spatialContext.makePoint(x_12, y_12));
						points.add(spatialContext.makePoint(x_13, y_13));
						points.add(spatialContext.makePoint(x_21, y_21));
						points.add(spatialContext.makePoint(x_22, y_22));
						points.add(spatialContext.makePoint(x_23, y_23));

						points.add(spatialContext.makePoint(x_11, y_11));
					} else {
						// B(y-y2) = x- x2
						x_21 = x2 - B * y2
								+ B * (y2 / (B * B + 1) + r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1));
						y_21 = y2 / (B * B + 1) + r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);
						x_24 = x2 - B * y2
								+ B * (y2 / (B * B + 1) - r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1));
						y_24 = y2 / (B * B + 1) - r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						// y-y2 = -B(x-x2)
						x_22 = (y2 + B * x2 - y2 / (B * B + 1) + B * r2 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y2) / (B * B + 1)) / B;
						y_22 = y2 / (B * B + 1) - B * r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						x_23 = -(y2 / (B * B + 1) - B * x2 - y2 + B * r2 * (1 / Math.sqrt(B * B + 1))
								+ (B * B * y2) / (B * B + 1)) / B;
						y_23 = y2 / (B * B + 1) + B * r2 * (1 / Math.sqrt(B * B + 1)) + (B * B * y2) / (B * B + 1);

						// B(y-y1) = x- x1
						x_11 = x1 - B * y1
								+ B * (y1 / (B * B + 1) + r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1));
						y_11 = y1 / (B * B + 1) + r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						x_14 = x1 - B * y1
								+ B * (y1 / (B * B + 1) - r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1));
						y_14 = y1 / (B * B + 1) - r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						// y-y1 = -B(x-x1)
						x_12 = (y1 + B * x1 - y1 / (B * B + 1) + B * r1 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y1) / (B * B + 1)) / B;
						y_12 = y1 / (B * B + 1) - B * r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						x_13 = (y1 + B * x1 - y1 / (B * B + 1) + B * r1 * (1 / Math.sqrt(B * B + 1))
								- (B * B * y1) / (B * B + 1)) / B;
						y_13 = y1 / (B * B + 1) - B * r1 * (1 / Math.sqrt(B * B + 1)) + (B * B * y1) / (B * B + 1);

						points.add(spatialContext.makePoint(x_11, y_11));
						points.add(spatialContext.makePoint(x_12, y_12));
						points.add(spatialContext.makePoint(x_13, y_13));
						points.add(spatialContext.makePoint(x_14, y_14));
						points.add(spatialContext.makePoint(x_21, y_21));
						points.add(spatialContext.makePoint(x_22, y_22));
						points.add(spatialContext.makePoint(x_23, y_23));
						points.add(spatialContext.makePoint(x_24, y_24));
					}
				} else if (x1 != x2) {
					// When y1=y2, the two solutions of x are equal
					x_11 = x_12 = x1;
					x_21 = x_22 = x2;
					a = 1;
					b = -2 * y1;
					c = y1 * y1 - r1 * r1 + (x_11 - x1) * (x_11 - x1);

					delta = b * b - 4 * a * c;
					if (delta > 0) {
						y_11 = y1 + r1;
						y_12 = y1 - r1;
						x_13 = x1 - r1;
						y_13 = y1;

						y_21 = y2 + r2;
						y_22 = y2 - r2;
						x_23 = x2 + r2;
						y_23 = y2;
						points.add(spatialContext.makePoint(x_11, y_11));
						points.add(spatialContext.makePoint(x_12, y_12));
						points.add(spatialContext.makePoint(x_13, y_13));
						points.add(spatialContext.makePoint(x_21, y_21));
						points.add(spatialContext.makePoint(x_22, y_22));
						points.add(spatialContext.makePoint(x_23, y_23));
					} else if (delta == 0) {
						y_11 = y1 + r1;
						y_12 = y1 - r1;
						x_13 = x1 - r1;
						y_13 = y1;

						y_21 = y2 + r2;
						y_22 = y2 - r2;
						x_23 = x2 + r2;
						y_23 = y2;

						x_14 = x1 + r1;
						y_14 = y1;

						points.add(spatialContext.makePoint(x_11, y_11));
						points.add(spatialContext.makePoint(x_12, y_12));
						points.add(spatialContext.makePoint(x_13, y_13));
						points.add(spatialContext.makePoint(x_14, y_14));
						points.add(spatialContext.makePoint(x_21, y_21));
						points.add(spatialContext.makePoint(x_22, y_22));
						points.add(spatialContext.makePoint(x_23, y_23));
					} else {
						y_11 = y1 + r1;
						y_12 = y1 - r1;
						x_13 = x1 - r1;
						y_13 = y1;

						y_21 = y2 + r2;
						y_22 = y2 - r2;
						x_23 = x2 + r2;
						y_23 = y2;

						x_14 = x1 + r1;
						y_14 = y1;

						x_24 = x2 - r2;
						y_24 = y2;

						points.add(spatialContext.makePoint(x_11, y_11));
						points.add(spatialContext.makePoint(x_12, y_12));
						points.add(spatialContext.makePoint(x_13, y_13));
						points.add(spatialContext.makePoint(x_14, y_14));
						points.add(spatialContext.makePoint(x_21, y_21));
						points.add(spatialContext.makePoint(x_22, y_22));
						points.add(spatialContext.makePoint(x_23, y_23));
						points.add(spatialContext.makePoint(x_24, y_24));
					}
				}
			}
		return new PointSet(points);

	}

	public PointSet satisfactionTerm(WeightedGraph weightedGraph, Term term1, Term term2) {
		// TODO Auto-generated method stub
		return null;
	}

}
